package com.appharbr.example.app.max;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxBannerBinding;
import com.appharbr.sdk.engine.AdSdk;
import com.appharbr.sdk.engine.AppHarbr;
import com.appharbr.sdk.engine.listeners.AHListener;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.sdk.AppLovinSdk;

import java.util.Arrays;

public class MaxBannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ActivityMaxBannerBinding binding = ActivityMaxBannerBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());

	//	**** (1) ****
	//Initialize AppLovinSdk
	AppLovinSdk.getInstance(this).setMediationProvider("max");
	AppLovinSdk.initializeSdk(this);

	//	**** (2) ****
	//Set your ad listener for Max events
	binding.maxAdViewBanner.setListener(mAdListener);

	//      **** (3) ****
	//Add Max's banner adView instance for Monitoring
	AppHarbr.addBannerView(AdSdk.MAX, binding.maxAdViewBanner, getLifecycle(), ahListener);

	//      **** (4) ****
	//Star loading Ads
	binding.maxAdViewBanner.loadAd();

	//Optionally set auto refresh of ads
	binding.maxAdViewBanner.startAutoRefresh();
    }

    private final AHListener ahListener = (view, unitId, adFormat, reasons)
	    -> Log.d("LOG", "AppHarbr - onAdBlocked for: " + unitId + ", reason: " + Arrays.toString(reasons));

    private final MaxAdViewAdListener mAdListener = new MaxAdViewAdListener() {

	@Override
	public void onAdExpanded(MaxAd ad) {
	    Log.d("LOG", "Max - onAdExpanded");
	}

	@Override
	public void onAdCollapsed(MaxAd ad) {
	    Log.d("LOG", "Max - onAdCollapsed");
	}

	@Override
	public void onAdLoaded(MaxAd ad) {
	    Log.d("LOG", "Max - onAdLoaded");
	}

	@Override
	public void onAdDisplayed(MaxAd ad) {
	    Log.d("LOG", "Max - onAdDisplayed");
	}

	@Override
	public void onAdHidden(MaxAd ad) {
	    Log.d("LOG", "Max - onAdHidden");
	}

	@Override
	public void onAdClicked(MaxAd ad) {
	    Log.d("LOG", "Max - onAdClicked");
	}

	@Override
	public void onAdLoadFailed(String adUnitId, MaxError error) {
	    Log.d("LOG", "Max - onAdLoadFailed: " + error.getMessage() + " => " + error.getCode());
	}

	@Override
	public void onAdDisplayFailed(MaxAd ad, MaxError error) {
	    Log.d("LOG", "Max - onAdDisplayFailed");
	}
    };
}