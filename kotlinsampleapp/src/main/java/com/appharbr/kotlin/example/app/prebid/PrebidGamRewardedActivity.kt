package com.appharbr.kotlin.example.app.prebid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.appharbr.sdk.engine.adnetworks.inappbidding.InAppBidding
import com.appharbr.sdk.engine.mediators.gam.rewarded.AHGamRewardedAd
import com.appharbr.sdk.log.AHLog
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import org.prebid.mobile.RewardedVideoAdUnit
import org.prebid.mobile.Signals
import org.prebid.mobile.VideoParameters

class PrebidGamRewardedActivity : ComponentActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-app-original-api-video-interstitial"
        const val CONFIG_ID = "prebid-ita-video-rewarded-320-480-original-api"
    }

    private var adUnit: RewardedVideoAdUnit? = null
    private val ahRewardedAd = AHGamRewardedAd()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        Text(text = stringResource(id = R.string.admob_rewarded_screen))

                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    private fun createAd() {
        // 1. Create RewardedVideoAdUnit
        adUnit = RewardedVideoAdUnit(CONFIG_ID)

        // 2. Configure Video parameters
        adUnit?.videoParameters = configureVideoParameters()

        // 3. Make a bid request to Prebid Server
        val request = AdManagerAdRequest.Builder().build()
        adUnit?.fetchDemand(request) {

            // 4. Load a GAM Rewarded Ad
            RewardedAd.load(
                this,
                AD_UNIT_ID,
                request,
                monitorByAppHarbr()!!
                //defaultCallback()
            )
        }
    }

    private fun configureVideoParameters(): VideoParameters {
        return VideoParameters(listOf("video/mp4")).apply {
            protocols = listOf(Signals.Protocols.VAST_2_0)
            playbackMethod = listOf(Signals.PlaybackMethod.AutoPlaySoundOff)
        }
    }

    private fun monitorByAppHarbr(): RewardedAdLoadCallback? {
        return AppHarbr.addRewardedAd<RewardedAdLoadCallback>(
            AdSdk.GAM,
            ahRewardedAd,
            object : InAppBidding {
                override fun getPrebidObject(adFormat: AdFormat, mediationAdUnitId: String) = adUnit
                override fun getNimbusObject(adFormat: AdFormat, mediationAdUnitId: String) = null
            },
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    ahRewardedAd.setRewardedAd(rewardedAd)

                    Handler(Looper.getMainLooper()).postDelayed({
                        val state = AppHarbr.getRewardedState(ahRewardedAd)
                        if (state != AdStateResult.BLOCKED) {
                            ahRewardedAd.gamRewardedAd?.show(this@PrebidGamRewardedActivity) {}
                        } else {
                            "AppHarbr Blocked Ad, please reload Ad".let { message ->
                                AHLog.e(message)
                            }
                        }
                    }, 100)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    ahRewardedAd.setRewardedAd(null)

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
    }

    override fun onDestroy() {
        super.onDestroy()

        adUnit?.stopAutoRefresh()
    }

}