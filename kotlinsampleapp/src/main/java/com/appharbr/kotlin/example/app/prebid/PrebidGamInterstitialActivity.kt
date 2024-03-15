package com.appharbr.kotlin.example.app.prebid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.appharbr.sdk.engine.adnetworks.inappbidding.InAppBidding
import com.appharbr.sdk.engine.mediators.gam.interstitial.AHGamInterstitialAd
import com.appharbr.sdk.log.AHLog
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import org.prebid.mobile.AdUnit
import org.prebid.mobile.InterstitialAdUnit

class PrebidGamInterstitialActivity  : ComponentActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-app-original-api-display-interstitial"
        const val CONFIG_ID = "prebid-ita-display-interstitial-320-480"
    }

    private var adUnit: AdUnit? = null
    private val ahInterstitialAd = AHGamInterstitialAd()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*prepareAppHarbrWrapperListener()
        requestAd()*/

        createAd()

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
                        Text(text = "Prebid Gam Interstitial Sample")

                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    private fun createAd() {
        // 1. Create InterstitialAdUnit
        adUnit = InterstitialAdUnit(CONFIG_ID, 80, 60)
        adUnit?.setAutoRefreshInterval(30)

        // 2. Make a bid request to Prebid Server
        val request = AdManagerAdRequest.Builder().build()
        adUnit?.fetchDemand(request) {

            // 3. Load a GAM interstitial ad
            AdManagerInterstitialAd.load(
                this,
                AD_UNIT_ID,
                request,
                monitorByAppHarbr()!!,
            )
        }
    }

    private fun monitorByAppHarbr() =
        AppHarbr.addInterstitial<AdManagerInterstitialAdLoadCallback>(
            AdSdk.GAM,
            ahInterstitialAd,
            object : InAppBidding {
                override fun getPrebidObject(adFormat: AdFormat, mediationAdUnitId: String) = adUnit
                override fun getNimbusObject(adFormat: AdFormat, mediationAdUnitId: String) = null
            },
            object : AdManagerInterstitialAdLoadCallback() {

                override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    AHLog.d("Prebid Gam Ad was loaded.")
                    ahInterstitialAd.setInterstitialAd(interstitialAd)

                    Handler(Looper.getMainLooper()).postDelayed({
                        val state = AppHarbr.getInterstitialState(ahInterstitialAd)
                        if (state != AdStateResult.BLOCKED) {
                            interstitialAd.show(this@PrebidGamInterstitialActivity)
                        } else {
                            "AppHarbr Blocked Ad, please reload Ad".let { message ->
                                AHLog.e(message)
                            }
                        }
                    }, 100)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    ahInterstitialAd.setInterstitialAd(null)
                    super.onAdFailedToLoad(loadAdError)

                    "Failed to load Prebid: ${loadAdError.cause} | ${loadAdError.code} | ${loadAdError.message} | ${loadAdError.responseInfo}".apply {
                        AHLog.e("Failed to load Prebid: ${loadAdError.cause} | ${loadAdError.code} | ${loadAdError.message} | ${loadAdError.responseInfo}")
                    }
                }

            },
            lifecycle
        ) { view: Any?, unitId: String?, adFormat: AdFormat?, reasons: Array<AdBlockReason?>? ->
            val message =
                "AppHarbr - on Ad Blocked: view[${view?.javaClass?.simpleName}] unitId[$unitId] adFormat[$adFormat] reasons[${
                    reasons?.joinToString(
                        separator = ","
                    )
                }]"

            AHLog.d(message)
        }

    override fun onDestroy() {
        super.onDestroy()
        adUnit?.stopAutoRefresh()
    }

}