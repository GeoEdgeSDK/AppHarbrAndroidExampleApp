package com.appharbr.kotlin.example.app.gam

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import com.appharbr.kotlin.example.app.R
import com.appharbr.kotlin.example.app.ui.theme.AppHarbrExampleAppTheme
import com.appharbr.sdk.engine.AdBlockReason
import com.appharbr.sdk.engine.AdSdk
import com.appharbr.sdk.engine.AppHarbr
import com.appharbr.sdk.engine.adformat.AdFormat
import com.appharbr.sdk.engine.listeners.AHListener
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import java.util.*

class GamBannerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppHarbrExampleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val unitID = stringResource(R.string.gam_banner_ad_unit_id)
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            AdManagerAdView(context).apply {
                                adSize = AdSize.BANNER
                                adUnitId = unitID
                                adListener = mAdListener


                                //      **** (2) ****
                                //Add Max's banner adView instance for Monitoring
                                AppHarbr.addBannerView<Lifecycle>(
                                    AdSdk.GAM,
                                    this,
                                    lifecycle,
                                    ahListener
                                )

                                loadAd(AdManagerAdRequest.Builder().build())
                            }
                        }
                    )
                }
            }
        }
    }

    private val mAdListener: AdListener = object : AdListener() {
        override fun onAdImpression() {
            super.onAdImpression()
            Log.d("LOG", "GAM - onAdImpression")
        }

        override fun onAdLoaded() {
            Log.d("LOG", "GAM - onAdLoaded")
        }

        override fun onAdFailedToLoad(adError: LoadAdError) {
            Log.d("LOG", "GAM - onAdFailedToLoad: " + adError.message)
        }

        override fun onAdOpened() {
            Log.d("LOG", "GAM - onAdOpened")
        }

        override fun onAdClicked() {
            Log.d("LOG", "GAM - onAdClicked")
        }

        override fun onAdClosed() {
            Log.d("LOG", "GAM - onAdClosed")
        }
    }

    private val ahListener =
        AHListener { view: Any?, unitId: String, adFormat: AdFormat?, reasons: Array<AdBlockReason?>? ->
            Log.d(
                "LOG",
                "AppHarbr - onAdBlocked for: $unitId, reason: " + Arrays.toString(
                    reasons
                )
            )
        }

}