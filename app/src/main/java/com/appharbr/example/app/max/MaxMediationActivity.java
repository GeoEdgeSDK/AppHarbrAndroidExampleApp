package com.appharbr.example.app.max;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxMediationBinding;

public class MaxMediationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ActivityMaxMediationBinding binding = ActivityMaxMediationBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());

	binding.maxBanner.setOnClickListener((view) -> {
	    startActivity(new Intent(MaxMediationActivity.this, MaxBannerActivity.class));
	});

	binding.maxInterstitial.setOnClickListener((view) -> {
	    startActivity(new Intent(MaxMediationActivity.this, MaxInterstitialActivity.class));
	});

	binding.maxRewarded.setOnClickListener((view) -> {
	    startActivity(new Intent(MaxMediationActivity.this, MaxRewardedActivity.class));
	});

	binding.maxNative.setOnClickListener((view) -> {
	    startActivity(new Intent(MaxMediationActivity.this, MaxNativeAdActivity.class));
	});

	binding.maxRecyclerView.setOnClickListener((view) -> {
	    startActivity(new Intent(MaxMediationActivity.this, MaxRecyclerViewActivity.class));
	});

    }

}