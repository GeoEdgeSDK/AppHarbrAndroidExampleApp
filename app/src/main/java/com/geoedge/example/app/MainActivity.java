package com.geoedge.example.app;


import android.os.Bundle;

import com.geoedge.sdk.engine.GEEvents;
import com.geoedge.sdk.engine.GeoEdge;
import com.geoedge.sdk.engine.InitializationFailureReason;
import com.geoedge.sdk.engine.OnGeoEdgeInitializationCompleteListener;
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
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PublisherAdView publisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      **** (1) ****
//      Initialize Google Ad Manager
        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("AF7FF64A7B4DBED0EB448F3423068279"))
                .build());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//      **** (2) ****
//      Get the AdView
        publisherAdView = findViewById(R.id.ad_view);

//      **** (3) ****
//      Set your ad listener for Google Ad Manager events
        publisherAdView.setAdListener(mAdListener);

//      **** (4) ****
//      Initialize GeoEdge SDK
        GeoEdge.initialize(getApplicationContext(), "API_KEY", new OnGeoEdgeInitializationCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d("LOG", "onSuccess");
            }

            @Override
            public void onFailure(@NonNull InitializationFailureReason reason) {
                Log.d("LOG", "onFailure client Listener: " + reason.getReadableHumanReason());
            }
        });

//      **** (5) ****
//      Add the adView instance to GeoEdge for Monitoring
        GeoEdge.addView(publisherAdView, new GEEvents() {
            @Override
            public void onAdBlocked(@NonNull ViewGroup view) {
                Log.d("LOG", "onAdBlocked");
            }

            @Override
            public void onAdReported(@NonNull ViewGroup view) {
                Log.d("LOG", "onAdReported");
            }

            @Override
            public void onImpression(@NonNull ViewGroup view) {
                Log.d("LOG", "onImpression");
            }
        });

//      **** (6) ****
//      Request for the Ads
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        publisherAdView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//      **** (7) ****
//      Remove the view on Destroy
        GeoEdge.removeView(publisherAdView);
    }

    AdListener mAdListener = new AdListener() {

        @Override
        public void onAdImpression() {
            super.onAdImpression();
            Log.d("LOG", "onAdImpression");

        }

        @Override
        public void onAdLoaded() {
            Log.d("LOG", "onAdLoaded");
        }

        @Override
        public void onAdFailedToLoad(LoadAdError adError) {
            Log.d("LOG", "onAdFailedToLoad: " + adError.getMessage());
        }

        @Override
        public void onAdOpened() {
            Log.d("LOG", "onAdOpened");
        }

        @Override
        public void onAdClicked() {
            Log.d("LOG", "onAdClicked");
        }

        @Override
        public void onAdLeftApplication() {
            Log.d("LOG", "onAdLeftApplication");
        }

        @Override
        public void onAdClosed() {
            Log.d("LOG", "onAdClosed");
        }
    };
}
