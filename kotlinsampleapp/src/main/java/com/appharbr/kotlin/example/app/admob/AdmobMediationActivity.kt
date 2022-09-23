package com.appharbr.kotlin.example.app.admob

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.appharbr.kotlin.example.app.gam.GamBannerActivity
import com.appharbr.kotlin.example.app.gam.GamInterstitialActivity
import com.appharbr.kotlin.example.app.gam.GamNativeAdActivity
import com.appharbr.kotlin.example.app.gam.GamRewardedActivity
import com.appharbr.kotlin.example.app.ui.theme.AppHarbrExampleAppTheme

class AdmobMediationActivity : ComponentActivity() {

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
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            15.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AdFormatButton("Banner", GamBannerActivity::class.java)
                        AdFormatButton("Interstitial", GamInterstitialActivity::class.java)
                        AdFormatButton("Rewarded", GamRewardedActivity::class.java)
                        AdFormatButton("Native", GamNativeAdActivity::class.java)

                    }
                }
            }
        }
    }

    @Composable
    private fun AdFormatButton(
        formatName: String,
        activityClass: Class<*>
    ) {
        val context = LocalContext.current
        Button(
            onClick = {
                context.startActivity(
                    Intent(
                        context,
                        activityClass
                    )
                )
            }) {
            Text(text = formatName)
        }
    }

}