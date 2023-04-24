package com.appharbr.kotlin.example.app.gam

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
import com.appharbr.sdk.engine.mediators.gam.interstitial.AHGamInterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import java.util.*

class GamInterstitialActivity : ComponentActivity() {

    private val ahGamInterstitialAd = AHGamInterstitialAd()
    private var ahWrapperListener: AdManagerInterstitialAdLoadCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepareAppHarbrWrapperListener()
        requestAd()

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
                        Text(text = stringResource(id = R.string.gam_interstitial_screen))

                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    private fun prepareAppHarbrWrapperListener() {
        //      **** (1) ****
        // The publisher will initiate the listener wrapper and will use it when load the GAM Interstitial Ad.
        ahWrapperListener = AppHarbr.addInterstitial<AdManagerInterstitialAdLoadCallback>(
            AdSdk.GAM,
            ahGamInterstitialAd,
            adManagerInterstitialAdLoadCallback,
            ahListener
        )
    }

    private fun requestAd() {
        //      **** (2) ****
        //Request to load interstitial Ad and instead of AdManagerInterstitialAdLoadCallback we should use ahWrapperListener to monitor interstitial Ad
        ahWrapperListener?.let {
            AdManagerInterstitialAd.load(
                this,
                applicationContext.resources.getString(R.string.gam_interstitial_ad_unit_id),
                AdManagerAdRequest.Builder().build(),
                it
            )
        }
    }

    private val adManagerInterstitialAdLoadCallback: AdManagerInterstitialAdLoadCallback =
        object : AdManagerInterstitialAdLoadCallback() {

            override fun onAdLoaded(adManagerInterstitialAd: AdManagerInterstitialAd) {
                Log.d("LOG", "onAdLoaded")
                if (isDestroyed) {
                    return
                }
                showAd()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d("LOG", "onAdFailedToLoad: " + loadAdError.message)
            }
        }

    //      **** (3) ****
    //Check was interstitial Ad blocked or not
    private fun showAd() {
        ahGamInterstitialAd.gamInterstitialAd?.let {
            val interstitialState = AppHarbr.getInterstitialState(ahGamInterstitialAd)
            if (interstitialState != AdStateResult.BLOCKED) {
                Log.d(
                    "LOG",
                    "**************************** AppHarbr Permit to Display GAM Interstitial ****************************"
                )
                it.show(this)
            } else {
                Log.d(
                    "LOG",
                    "**************************** AppHarbr Blocked GAM Interstitial ****************************"
                )
                // You may call to reload interstitial
            }
        } ?: Log.d("TAG", "The GAM interstitial wasn't loaded yet.")
    }

    var ahListener =
        AHListener { view: Any?, unitId: String?, adFormat: AdFormat?, reasons: Array<AdBlockReason?>? ->
            Log.d(
                "LOG",
                "AppHarbr - onAdBlocked for: $unitId, reason: " + Arrays.toString(
                    reasons
                )
            )
        }

}