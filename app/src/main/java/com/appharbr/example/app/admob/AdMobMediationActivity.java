package com.appharbr.example.app.admob;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.R;
import com.google.android.gms.ads.MobileAds;

public class AdMobMediationActivity extends AppCompatActivity {

    private Button btnBanner;
    private Button btnInterstitial;
    private Button btnRewarded;
    private Button btnNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admob_mediation);

        initAdMobMediation();

        btnBanner = findViewById(R.id.admob_banner);
        btnBanner.setOnClickListener((view) ->{
            AdMobMediationActivity.this.startActivity(new Intent(AdMobMediationActivity.this, AdMobBannerActivity.class));
        });
        btnInterstitial = findViewById(R.id.admob_interstitial);
        btnInterstitial.setOnClickListener((view) ->{
            AdMobMediationActivity.this.startActivity(new Intent(AdMobMediationActivity.this, AdMobInterstitialActivity.class));
        });
        btnRewarded = findViewById(R.id.admob_rewarded);
        btnRewarded.setOnClickListener((view) ->{
            AdMobMediationActivity.this.startActivity(new Intent(AdMobMediationActivity.this, AdMobRewardedActivity.class));
        });
        btnNative = findViewById(R.id.admob_native);
        btnNative.setOnClickListener((view) ->{
            AdMobMediationActivity.this.startActivity(new Intent(AdMobMediationActivity.this, AdMobNativeAdActivity.class));
        });
    }

    private void initAdMobMediation () {
        MobileAds.initialize(this, initializationStatus -> {
            Log.d("LOG", "AdMob Mediation Initialized");
        });

    }


}
