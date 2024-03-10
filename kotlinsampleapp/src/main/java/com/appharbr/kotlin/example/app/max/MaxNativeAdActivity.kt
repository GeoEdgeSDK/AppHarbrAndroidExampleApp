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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.appharbr.kotlin.example.app.R
import com.appharbr.kotlin.example.app.ui.theme.AppHarbrExampleAppTheme
import com.appharbr.sdk.engine.AdSdk
import com.appharbr.sdk.engine.AdStateResult
import com.appharbr.sdk.engine.AppHarbr
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.sdk.AppLovinSdk

class MaxNativeAdActivity : ComponentActivity() {

    private val nativeAdState = mutableStateOf<MaxNativeAdView?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNativeAd()

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
                        DisplayNativeAd()
                    }
                }
            }
        }
    }

    private fun requestNativeAd() {
        //Initialize AppLovinSdk
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this)

        //	**** (1) ****
        //Create Max native ad loader
        val maxNativeAdLoader = MaxNativeAdLoader("YOUR_AD_UNIT_ID", this)

        //	**** (2) ****
        //Set listener to get results
        maxNativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(maxNativeAdView: MaxNativeAdView?, maxAd: MaxAd) {
                Log.d("LOG", "Max - onNativeAdLoaded")

                //	**** (3) ****
                //Check loaded Max native ad from AppHarbr if it needs to be blocked
                val adResult = AppHarbr.shouldBlockNativeAd(AdSdk.MAX, maxAd, "YOUR_AD_UNIT_ID")
                if (adResult.adStateResult != AdStateResult.BLOCKED) {
                    Log.d(
                        "LOG",
                        "**************************** AppHarbr Permit to Display Max Native Ad ****************************"
                    )
                    nativeAdState.value = maxNativeAdView
                } else {
                    Log.d(
                        "LOG",
                        "**************************** AppHarbr Blocked Max Native Ad ****************************"
                    )
                }
            }

            override fun onNativeAdLoadFailed(s: String, maxError: MaxError) {
                Log.d("LOG", "Max - onNativeAdLoadFailed")
            }
        })

        //	**** (4) ****
        //And finally load Max native Ad
        maxNativeAdLoader.loadAd()
    }

    @Composable
    private fun DisplayNativeAd() {
        nativeAdState.value?.let {
            AndroidView(modifier = Modifier
                .fillMaxSize(),
                factory = { _ ->
                    it //display MaxNativeAdView inside AndroidView
                }
            )
        } ?: kotlin.run {
            Text(
                text = stringResource(id = R.string.max_native_screen),
                fontSize = 20.sp
            )
            CircularProgressIndicator()
        }
    }
}