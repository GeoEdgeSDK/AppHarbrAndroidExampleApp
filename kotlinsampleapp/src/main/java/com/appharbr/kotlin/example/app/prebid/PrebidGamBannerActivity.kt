package com.appharbr.kotlin.example.app.prebid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.appharbr.kotlin.example.app.ui.theme.AppHarbrExampleAppTheme
import com.appharbr.sdk.engine.AdSdk
import com.appharbr.sdk.engine.AppHarbr
import com.appharbr.sdk.engine.adformat.AdFormat
import com.appharbr.sdk.engine.adnetworks.inappbidding.InAppBidding
import com.appharbr.sdk.log.AHLog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import org.prebid.mobile.BannerAdUnit
import org.prebid.mobile.BannerParameters
import org.prebid.mobile.Signals
import org.prebid.mobile.addendum.AdViewUtils
import org.prebid.mobile.addendum.PbFindSizeError

class PrebidGamBannerActivity : ComponentActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid_demo_app_original_api_banner_300x250_order"
        const val CONFIG_ID = "prebid-ita-banner-300-250"
        const val WIDTH = 300
        const val HEIGHT = 250
    }

    private var prebidBannerAdUnit: BannerAdUnit? = null

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

                        Text(text = "Prebid Gam Banner")

                        AddBanner()
                    }
                }
            }
        }
    }

    @Composable
    private fun AddBanner() {
        createPrebidBannerAdUnit()

        //We need AndroidView to add banner in Compose UI
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context ->

                AHLog.w("Adding view in to compose !!!!!!!!!!!!")

                //      **** (1) ****
                //      Add Banner View in compose with all necessary params, like unit id and ad listener
                AdManagerAdView(context).apply {
                    adUnitId = AD_UNIT_ID
                    setAdSizes(AdSize(WIDTH, HEIGHT))
                    adListener = createAdListener(this)

                    //integrateAppHarbr(this)

                    //      **** (3) ****
                    //      Request for the Ads
                    AHLog.w("Request and load Ad --------------------------")
                    loadAd(AdManagerAdRequest.Builder().build())
                }
            }
        )
    }

    private fun createPrebidBannerAdUnit() {
        // 1. Create BannerAdUnit
        prebidBannerAdUnit = BannerAdUnit(CONFIG_ID, WIDTH, HEIGHT)
        prebidBannerAdUnit?.setAutoRefreshInterval(30)

        // 2. Configure banner parameters
        val parameters = BannerParameters()
        parameters.api = listOf(Signals.Api.MRAID_3, Signals.Api.OMID_1)
        prebidBannerAdUnit?.bannerParameters = parameters
    }

    private fun createAdListener(adManagerAdView: AdManagerAdView) = object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            AHLog.w("Ad loaded G !!!!!!!!!!!!!!")
            AdViewUtils.findPrebidCreativeSize(
                adManagerAdView,
                object : AdViewUtils.PbFindSizeListener {
                    override fun success(width: Int, height: Int) {
                        adManagerAdView.setAdSizes(AdSize(width, height)) //TODO do not need i think
                        AHLog.w("Ad was loaded !!!!!!!!!!!!!!")
                    }

                    override fun failure(error: PbFindSizeError) {
                        AHLog.e("Ad failed to loaded " + error.description + " | " + error.code)
                    }
                })
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            AHLog.w("Ad failed G !!!!!!!!!!!!!! " + p0.message )
        }
    }


    private fun integrateAppHarbr(adManagerAdView: AdManagerAdView) {
        AppHarbr.addBannerView(
            AdSdk.GAM,
            adManagerAdView,
            object : InAppBidding {
                override fun getPrebidObject(adFormat: AdFormat, mediationAdUnitId: String) =
                    prebidBannerAdUnit

                override fun getNimbusObject(adFormat: AdFormat, mediationAdUnitId: String) = null
            },
            lifecycle,
            null
        ) { view, unitId, adFormat, reasons ->
            val message =
                "AppHarbr Blocked Banner: view[${view?.javaClass?.simpleName}] unitId[$unitId] adFormat[$adFormat] reasons[${
                    reasons.joinToString(
                        separator = ","
                    )
                }]"
            Log.w("LOG", message)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        prebidBannerAdUnit?.stopAutoRefresh()
        AHLog.e("Stopping prebid")
    }

}