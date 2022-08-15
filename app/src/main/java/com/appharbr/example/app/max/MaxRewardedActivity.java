package com.appharbr.example.app.max;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxRewardedBinding;
import com.appharbr.sdk.engine.AdSdk;
import com.appharbr.sdk.engine.AdStateResult;
import com.appharbr.sdk.engine.AppHarbr;
import com.appharbr.sdk.engine.listeners.AHListener;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;

import java.util.Arrays;

public class MaxRewardedActivity extends AppCompatActivity {

    private ActivityMaxRewardedBinding binding;
    private MaxRewardedAd maxRewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	binding = ActivityMaxRewardedBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());

	//Initialize AppLovinSdk
	AppLovinSdk.getInstance(this).setMediationProvider("max");
	AppLovinSdk.initializeSdk(this);

	//	**** (1) ****
	//Initialize max Rewarded Ad
	maxRewardedAd = MaxRewardedAd.getInstance("YOUR_AD_UNIT_ID", this);

	//	**** (2) ****
	// The publisher will initiate once the listener wrapper and will use it when load the Max rewarded ad.
	MaxRewardedAdListener ahWrapperListener = AppHarbr.addRewardedAd(AdSdk.MAX,
		maxRewardedAd,
		maxRewardedAdListener,
		getLifecycle(),
		ahListener);

	//	**** (3) ****
	//Set ahWrapperListener and load Ad
	maxRewardedAd.setListener(ahWrapperListener);
	maxRewardedAd.loadAd();
    }

    private final MaxRewardedAdListener maxRewardedAdListener = new MaxRewardedAdListener() {

	@Override
	public void onAdLoaded(MaxAd ad) {
	    Log.d("LOG", "Max - onAdLoaded");
	    binding.progressBar.setVisibility(View.GONE);
	    if (maxRewardedAd.isReady()) {
		checkAd();
	    }
	}

	private void checkAd() {
	    //	**** (4) ****
	    //Check whether Ad was blocked or not
	    final AdStateResult rewardedState = AppHarbr.getRewardedState(maxRewardedAd);
	    if (rewardedState != AdStateResult.BLOCKED) {
		Log.d("LOG", "**************************** AppHarbr Permit to Display Max Rewarded ****************************");
		maxRewardedAd.showAd();
	    } else {
		Log.d("LOG", "**************************** AppHarbr Blocked Max Rewarded ****************************");
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

	@Override
	public void onRewardedVideoStarted(MaxAd ad) {
	    Log.d("LOG", "Max - onRewardedVideoStarted");
	}

	@Override
	public void onRewardedVideoCompleted(MaxAd ad) {
	    Log.d("LOG", "Max - onRewardedVideoCompleted");
	}

	@Override
	public void onUserRewarded(MaxAd ad, MaxReward reward) {
	    Log.d("LOG", "Max - onUserRewarded");
	}
    };

    AHListener ahListener = (view, unitId, adFormat, reasons)
	    -> Log.d("LOG", "AppHarbr - onAdBlocked for: " + unitId + ", reason: " + Arrays.toString(reasons));

}