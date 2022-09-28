package com.appharbr.kotlin.example.app.max

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.appharbr.kotlin.example.app.R
import com.appharbr.kotlin.example.app.ui.theme.AppHarbrExampleAppTheme
import com.appharbr.sdk.engine.AdBlockReason
import com.appharbr.sdk.engine.AdSdk
import com.appharbr.sdk.engine.AdStateResult
import com.appharbr.sdk.engine.AppHarbr
import com.appharbr.sdk.engine.adformat.AdFormat
import com.appharbr.sdk.engine.listeners.AHListener
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdk
import java.util.*

class MaxInterstitialActivity : ComponentActivity() {

    private lateinit var maxInterstitialAd: MaxInterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAndLoadInterstitialAd()

        setContent {
            AppHarbrExampleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.max_interstitial_screen))

                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    private fun createAndLoadInterstitialAd() {
        //Initialize AppLovinSdk
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this)

        //	**** (1) ****
        //Initialize max interstitial Ad
        maxInterstitialAd = MaxInterstitialAd("YOUR_AD_UNIT_ID", this)

        //	**** (2) ****
        // The publisher will initiate once the listener wrapper and will use it when load the Max interstitial ad.
        val ahWrapperListener = AppHarbr.addInterstitial<MaxAdListener>(
            AdSdk.MAX,
            maxInterstitialAd,
            maxAdListener,
            lifecycle,
            ahListener
        )

        //	**** (3) ****
        //Set ahWrapperListener and load Ad
        maxInterstitialAd.setListener(ahWrapperListener)
        maxInterstitialAd.loadAd()
    }

    private val maxAdListener: MaxAdListener = object : MaxAdListener {
        override fun onAdLoaded(ad: MaxAd) {
            Log.d("LOG", "Max - onAdLoaded")
            if (maxInterstitialAd.isReady) {
                checkAd()
            }
        }

        private fun checkAd() {
            //	**** (4) ****
            //Check whether Ad was blocked or not
            val interstitialState = AppHarbr.getInterstitialState(maxInterstitialAd)
            if (interstitialState != AdStateResult.BLOCKED) {
                Log.d(
                    "LOG",
                    "**************************** AppHarbr Permit to Display Max Interstitial ****************************"
                )
                maxInterstitialAd.showAd()
            } else {
                Log.d(
                    "LOG",
                    "**************************** AppHarbr Blocked Max Interstitial ****************************"
                )
                // You may call to reload Max interstitial
            }
        }

        override fun onAdDisplayed(ad: MaxAd) {
            Log.d("LOG", "Max - onAdDisplayed")
        }

        override fun onAdHidden(ad: MaxAd) {
            Log.d("LOG", "Max - onAdHidden")
            finish()
        }

        override fun onAdClicked(ad: MaxAd) {
            Log.d("LOG", "Max - onAdClicked")
        }

        override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
            Log.d("LOG", "Max - onAdLoadFailed")
        }

        override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
            Log.d("LOG", "Max - onAdDisplayFailed")
        }
    }

    var ahListener =
        AHListener { view: Any?, unitId: String, adFormat: AdFormat?, reasons: Array<AdBlockReason?>? ->
            Log.d(
                "LOG",
                "AppHarbr - onAdBlocked for: $unitId, reason: " + Arrays.toString(
                    reasons
                )
            )
        }
}