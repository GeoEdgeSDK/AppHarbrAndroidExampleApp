package com.geoedge.example.app;


import android.os.Bundle;

import com.geoedge.sdk.configuration.GESdkConfiguration;
import com.geoedge.sdk.engine.AdBlockReason;
import com.geoedge.sdk.engine.AdSdk;
import com.geoedge.sdk.engine.GeoEdge;
import com.geoedge.sdk.engine.InitializationFailureReason;
import com.geoedge.sdk.engine.listeners.GEEvents;
import com.geoedge.sdk.engine.listeners.OnGeoEdgeInitializationCompleteListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // TODO: Publisher please write your GeoEdge API Key
    private final String API_KEY = "api_key";
    private PublisherAdView publisherAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      **** (1) ****
//      Initialize GeoEdge SDK
        GESdkConfiguration geSdkConfiguration = new GESdkConfiguration.Builder(API_KEY)
                .withAdNetworksToMonitor(new AdSdk[]{ AdSdk.MOPUB, AdSdk.ADMOB, AdSdk.GAM, AdSdk.FACEBOOK, AdSdk.CHARTBOOST })
                .build();

        GeoEdge.initialize(getApplicationContext(), geSdkConfiguration, new OnGeoEdgeInitializationCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d("LOG", "onSuccess");
            }

            @Override
            public void onFailure(@NonNull InitializationFailureReason initializationFailureReason) {
                Log.d("LOG", "onFailure client Listener: " + initializationFailureReason.getReadableHumanReason());
            }
        });

//      **** (2) ****
//      Get the GAM AdView
        publisherAdView = findViewById(R.id.ad_view);

//      **** (3) ****
//      Set your ad listener for Google Ad Manager events
        publisherAdView.setAdListener(mAdListener);

//      **** (4) ****
//      Add the adView instance to GeoEdge for Monitoring
        GeoEdge.addBannerView(AdSdk.GAM, publisherAdView, new GEEvents.GEBannerEvents() {
            @Override
            public void onBannerAdBlocked(@NonNull View view, @NonNull AdBlockReason[] adBlockReasons) {
                Log.d("LOG", "GeoEdge - onBannerAdBlocked - with reasons: " + Arrays.toString(adBlockReasons));
            }

            @Override
            public void onBannerAdReported(@NonNull View view, @NonNull AdBlockReason[] adBlockReasons) {
                Log.d("LOG", "GeoEdge - onBannerAdReported - with reasons: " + Arrays.toString(adBlockReasons));
            }
        });

//      **** (5) ****
//      Initialize Google Ad Manager
        MobileAds.initialize(this, initializationStatus -> {
//      **** (6) ****
//      Request for the Ads
            PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
            publisherAdView.loadAd(adRequest);

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//      **** (7) ****
//      Remove the view on Destroy
        GeoEdge.removeBannerView(publisherAdView);
        publisherAdView.destroy();
    }

    private final AdListener mAdListener = new AdListener() {

        @Override
        public void onAdImpression() {
            super.onAdImpression();
            Log.d("LOG", "GAM - onAdImpression");
        }

        @Override
        public void onAdLoaded() {
            Log.d("LOG", "GAM - onAdLoaded");
        }

        @Override
        public void onAdFailedToLoad(LoadAdError adError) {
            Log.d("LOG", "GAM - onAdFailedToLoad: " + adError.getMessage());
        }

        @Override
        public void onAdOpened() {
            Log.d("LOG", "GAM - onAdOpened");
        }

        @Override
        public void onAdClicked() {
            Log.d("LOG", "GAM - onAdClicked");
        }

        @Override
        public void onAdLeftApplication() {
            Log.d("LOG", "GAM - onAdLeftApplication");
        }

        @Override
        public void onAdClosed() {
            Log.d("LOG", "GAM - onAdClosed");
        }
    };
}
