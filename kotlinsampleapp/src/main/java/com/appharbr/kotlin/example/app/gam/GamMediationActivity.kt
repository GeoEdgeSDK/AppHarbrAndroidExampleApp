package com.appharbr.kotlin.example.app.gam

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.appharbr.kotlin.example.app.ui.theme.AppHarbrExampleAppTheme

class GamMediationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppHarbrExampleAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val context = LocalContext.current
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(
                            onClick = {
                                context.startActivity(
                                    Intent(
                                        context,
                                        GamBannerActivity::class.java
                                    )
                                )
                            }) {
                            Text(text = "Banner")
                        }

                        Button(
                            onClick = {
                                context.startActivity(
                                    Intent(
                                        context,
                                        GamNativeAdActivity::class.java
                                    )
                                )
                            }) {
                            Text(text = "Native")
                        }

                    }
                }
            }
        }
    }
}