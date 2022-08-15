package com.appharbr.example.app.max;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxInterstitialBinding;
import com.appharbr.sdk.engine.AdSdk;
import com.appharbr.sdk.engine.AdStateResult;
import com.appharbr.sdk.engine.AppHarbr;
import com.appharbr.sdk.engine.listeners.AHListener;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;

import java.util.Arrays;

public class MaxInterstitialActivity extends AppCompatActivity {

    private ActivityMaxInterstitialBinding binding;
    private MaxInterstitialAd maxInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	binding = ActivityMaxInterstitialBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());

	//	**** (1) ****
	//Initialize AppLovinSdk
	AppLovinSdk.getInstance(this).setMediationProvider("max");
	AppLovinSdk.initializeSdk(this);

	//	**** (2) ****
	//Initialize max interstitial Ad
	maxInterstitialAd = new MaxInterstitialAd("YOUR_AD_UNIT_ID", this);

	//	**** (3) ****
	// The publisher will initiate once the listener wrapper and will use it when load the Max interstitial ad.
	MaxAdListener ahWrapperListener = AppHarbr.addInterstitial(AdSdk.MAX,
		maxInterstitialAd,
		maxAdListener,
		getLifecycle(),
		ahListener);

	//	**** (4) ****
	//Set ahWrapperListener and load Ad
	maxInterstitialAd.setListener(ahWrapperListener);
	maxInterstitialAd.loadAd();
    }

    private final MaxAdListener maxAdListener = new MaxAdListener() {
	@Override
	public void onAdLoaded(MaxAd ad) {
	    Log.d("LOG", "Max - onAdLoaded");
	    binding.progressBar.setVisibility(View.GONE);
	    if (maxInterstitialAd.isReady()) {
		checkAd();
	    }
	}

	private void checkAd() {
	    //	**** (5) ****
	    //Check whether Ad was blocked or not
	    final AdStateResult interstitialState = AppHarbr.getInterstitialState(maxInterstitialAd);
	    if (interstitialState != AdStateResult.BLOCKED) {
		Log.d("LOG", "**************************** AppHarbr Permit to Display Max Interstitial ****************************");
		maxInterstitialAd.showAd();
	    } else {
		Log.d("LOG", "**************************** AppHarbr Blocked Max Interstitial ****************************");
		// You may call to reload Max interstitial
	    }
	}

	@Override
	public void onAdDisplayed(MaxAd ad) {
	    Log.d("LOG", "Max - onAdDisplayed");
	}

	@Override
	public void onAdHidden(MaxAd ad) {
	    Log.d("LOG", "Max - onAdHidden");
	    finish();
	}

	@Override
	public void onAdClicked(MaxAd ad) {
	    Log.d("LOG", "Max - onAdClicked");
	}

	@Override
	public void onAdLoadFailed(String adUnitId, MaxError error) {
	    Log.d("LOG", "Max - onAdLoadFailed");
	}

	@Override
	public void onAdDisplayFailed(MaxAd ad, MaxError error) {
	    Log.d("LOG", "Max - onAdDisplayFailed");
	}
    };

    AHListener ahListener = (view, unitId, adFormat, reasons)
	    -> Log.d("LOG", "AppHarbr - onAdBlocked for: " + unitId + ", reason: " + Arrays.toString(reasons));

}