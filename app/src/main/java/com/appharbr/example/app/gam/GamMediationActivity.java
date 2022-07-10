package com.appharbr.example.app.gam;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.R;
import com.google.android.gms.ads.MobileAds;

public class GamMediationActivity extends AppCompatActivity {

    private Button btnBanner;
    private Button btnInterstitial;
    private Button btnRewarded;
    private Button btnNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gam_mediation);

        initGamMediation();

        btnBanner = findViewById(R.id.gam_banner);
        btnBanner.setOnClickListener((view) ->{
            GamMediationActivity.this.startActivity(new Intent(GamMediationActivity.this, GamBannerActivity.class));
        });
        btnInterstitial = findViewById(R.id.gam_interstitial);
        btnInterstitial.setOnClickListener((view) ->{
            GamMediationActivity.this.startActivity(new Intent(GamMediationActivity.this, GamInterstitialActivity.class));
        });
        btnRewarded = findViewById(R.id.gam_rewarded);
        btnRewarded.setOnClickListener((view) ->{
            GamMediationActivity.this.startActivity(new Intent(GamMediationActivity.this, GamRewardedActivity.class));
        });
        btnNative = findViewById(R.id.gam_native);
        btnNative.setOnClickListener((view) ->{
            GamMediationActivity.this.startActivity(new Intent(GamMediationActivity.this, GamNativeAdActivity.class));
        });
    }

    private void initGamMediation () {
        MobileAds.initialize(this, initializationStatus -> {
            Log.d("LOG", "GAM Mediation Initialized");
        });

    }


}
