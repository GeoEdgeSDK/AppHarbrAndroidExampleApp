package com.appharbr.kotlin.example.app.admob

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.appharbr.kotlin.example.app.R
import com.appharbr.kotlin.example.app.ui.theme.AppHarbrExampleAppTheme
import com.appharbr.sdk.engine.AdBlockReason
import com.appharbr.sdk.engine.AdSdk
import com.appharbr.sdk.engine.AppHarbr
import com.appharbr.sdk.engine.adformat.AdFormat
import com.appharbr.sdk.engine.listeners.AHListener
import com.google.android.gms.ads.*
import java.util.*

class AdmobBannerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppHarbrExampleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp, bottom = 50.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(text = stringResource(id = R.string.admob_banner_screen))

                        AddBanner()
                    }
                }
            }
        }
    }

    @Composable
    private fun AddBanner() {
        val unitID = stringResource(R.string.admob_banner_ad_unit_id)
        //We need AndroidView to add banner in Compose UI
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context ->

                //      **** (1) ****
                //      Add Banner View in compose with all necessary params, like unit id and ad listener
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = unitID
                    adListener = mAdListener

                    //      **** (2) ****
                    //Add Admob's banner adView instance for Monitoring
                    AppHarbr.addBannerView(
                        AdSdk.ADMOB,
                        this,
                        lifecycle,
                        ahListener
                    )
                    //      **** (3) ****
                    //      Request for the Ads
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }

    private val mAdListener: AdListener = object : AdListener() {
        override fun onAdImpression() {
            Log.d("LOG", "AdMob - onAdImpression")
        }

        override fun onAdLoaded() {
            Log.d("LOG", "AdMob - onAdLoaded")
        }

        override fun onAdFailedToLoad(adError: LoadAdError) {
            Log.d("LOG", "AdMob - onAdFailedToLoad: " + adError.message)
        }

        override fun onAdOpened() {
            Log.d("LOG", "AdMob - onAdOpened")
        }

        override fun onAdClicked() {
            Log.d("LOG", "AdMob - onAdClicked")
        }

        override fun onAdClosed() {
            Log.d("LOG", "AdMob - onAdClosed")
        }
    }

    private val ahListener =
        AHListener { view: Any?, unitId: String?, adFormat: AdFormat?, reasons: Array<AdBlockReason?>? ->
            Log.d(
                "LOG",
                "AppHarbr - onAdBlocked for: $unitId, reason: " + Arrays.toString(
                    reasons
                )
            )
        }

}