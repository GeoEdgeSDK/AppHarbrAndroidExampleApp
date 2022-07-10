package com.appharbr.example.app.gam;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.sdk.engine.AdSdk;
import com.appharbr.sdk.engine.AppHarbr;
import com.appharbr.sdk.engine.listeners.AHListener;
import com.appharbr.example.app.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

import java.util.Arrays;

public class GamBannerActivity extends AppCompatActivity {

    private AdManagerAdView adManagerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gam_banner_layout);

//      **** (1) ****
//      Get the AdView
        adManagerAdView = findViewById(R.id.gam_banner_ad_view);

//      **** (2) ****
//      Set your ad listener for Google Ad Manager events
        adManagerAdView.setAdListener(mAdListener);

//      **** (3) ****
//      Add the adView instance to GeoEdge for Monitoring
        AppHarbr.addBannerView(AdSdk.GAM, adManagerAdView, ahListener);

//      **** (4) ****
//      Request for the Ads
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        adManagerAdView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//      **** (5) ****
//      Remove the view on Destroy
        AppHarbr.removeBannerView(adManagerAdView);
    }

    private final AHListener ahListener = (view, unitId, adFormat, reasons)
            -> Log.d("LOG", "AppHarbr - onAdBlocked for: " + unitId + ", reason: " + Arrays.toString(reasons));

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
        public void onAdClosed() {
            Log.d("LOG", "GAM - onAdClosed");
        }

    };
}
