package com.appharbr.example.app.max;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxMediationBinding;

public class MaxMediationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActivityMaxMediationBinding binding = ActivityMaxMediationBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());

	/*btnBanner = findViewById(R.id.admob_banner);
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
	});*/
    }
}