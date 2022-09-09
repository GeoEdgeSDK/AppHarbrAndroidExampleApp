package com.appharbr.kotlin.example.app

import android.app.Application
import android.util.Log
import com.appharbr.sdk.configuration.AHSdkConfiguration
import com.appharbr.sdk.engine.AppHarbr
import com.appharbr.sdk.engine.InitializationFailureReason
import com.appharbr.sdk.engine.listeners.OnAppHarbrInitializationCompleteListener

class MainApplication : Application() {

    private val API_KEY = "api_key"

    override fun onCreate() {
        super.onCreate()
        initAppHarbr()
    }

    private fun initAppHarbr() {
        val ahSdkConfiguration: AHSdkConfiguration = AHSdkConfiguration.Builder(API_KEY).build()
        AppHarbr.initialize(
            applicationContext,
            ahSdkConfiguration,
            object : OnAppHarbrInitializationCompleteListener {

                override fun onSuccess() {
                    Log.d("LOG", "AppHarbr SDK Initialized Successfully")
                }

                override fun onFailure(reason: InitializationFailureReason) {
                    Log.e(
                        "LOG",
                        "AppHarbr SDK Initialization Failed: " + reason.readableHumanReason
                    )
                }
            })
    }

}