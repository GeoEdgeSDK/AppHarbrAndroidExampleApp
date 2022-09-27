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
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import com.applovin.sdk.AppLovinSdk
import java.util.*

class MaxRewardedActivity : ComponentActivity() {

    private lateinit var maxRewardedAd: MaxRewardedAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAndLoadRewardedAd()

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
                        Text(text = stringResource(id = R.string.max_rewarded_screen))

                        CircularProgressIndicator()

                    }
                }
            }
        }
    }

    private fun createAndLoadRewardedAd() {
        //Initialize AppLovinSdk
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this)

        //	**** (1) ****
        //Initialize max Rewarded Ad
        maxRewardedAd = MaxRewardedAd.getInstance("YOUR_AD_UNIT_ID", this)

        //	**** (2) ****
        // The publisher will initiate once the listener wrapper and will use it when load the Max rewarded ad.
        val ahWrapperListener = AppHarbr.addRewardedAd(
            AdSdk.MAX,
            maxRewardedAd,
            maxRewardedAdListener,
            lifecycle,
            ahListener
        )

        //	**** (3) ****
        //Set ahWrapperListener and load Ad
        maxRewardedAd.setListener(ahWrapperListener)
        maxRewardedAd.loadAd()
    }

    private val maxRewardedAdListener: MaxRewardedAdListener = object : MaxRewardedAdListener {
        override fun onAdLoaded(ad: MaxAd) {
            Log.d("LOG", "Max - onAdLoaded")
            if (maxRewardedAd.isReady) {
                checkAd()
            }
        }

        private fun checkAd() {
            //	**** (4) ****
            //Check whether Ad was blocked or not
            val rewardedState = AppHarbr.getRewardedState(maxRewardedAd)
            if (rewardedState != AdStateResult.BLOCKED) {
                Log.d(
                    "LOG",
                    "**************************** AppHarbr Permit to Display Max Rewarded ****************************"
                )
                maxRewardedAd.showAd()
            } else {
                Log.d(
                    "LOG",
                    "**************************** AppHarbr Blocked Max Rewarded ****************************"
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

        override fun onRewardedVideoStarted(ad: MaxAd) {
            Log.d("LOG", "Max - onRewardedVideoStarted")
        }

        override fun onRewardedVideoCompleted(ad: MaxAd) {
            Log.d("LOG", "Max - onRewardedVideoCompleted")
        }

        override fun onUserRewarded(ad: MaxAd, reward: MaxReward) {
            Log.d("LOG", "Max - onUserRewarded")
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