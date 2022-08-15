package com.appharbr.example.app.gam;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.sdk.engine.AdSdk;
import com.appharbr.sdk.engine.AdStateResult;
import com.appharbr.sdk.engine.AppHarbr;
import com.appharbr.sdk.engine.listeners.AHListener;
import com.appharbr.sdk.engine.mediators.gam.rewarded.AHGamRewardedAd;
import com.appharbr.example.app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;

public class GamRewardedActivity extends AppCompatActivity {

    private Button btnDisplay;
    private AHGamRewardedAd ahGamRewardedAd = new AHGamRewardedAd();
    private RewardedAdLoadCallback ahWrapperListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gam_rewarded_layout);

        btnDisplay = findViewById(R.id.gam_rewarded_display);
        setDisplayClick();

        // The publisher will initiate once the listener wrapper and will use it when load the GAM Rewarded ad.
        ahWrapperListener = AppHarbr.addRewardedAd(AdSdk.GAM,
                ahGamRewardedAd,
                adManagerRewardedAdLoadCallback,
                ahListener);

        // The publisher load GAM (Google Ad Manager) Rewarded ad
        requestAd();
    }

    private void requestAd() {
        btnDisplay.setEnabled(false);
        RewardedAd.load(getApplicationContext(),
                getApplicationContext().getResources().getString(R.string.gam_rewarded_ad_unit_id),
                new AdRequest.Builder().build(),
                ahWrapperListener); //AppHarbr wrapper listener
    }

    private void setDisplayClick() {
        btnDisplay.setOnClickListener((view) -> {
            // The publisher display the GAM (Google Ad Manager) Rewarded
            if (isDestroyed()) {
                return;
            }
            if (ahGamRewardedAd.getGamRewardedAd() != null) {
                final AdStateResult interstitialState = AppHarbr.getRewardedState(ahGamRewardedAd);
                if (interstitialState != AdStateResult.BLOCKED) {
                    Log.d("LOG", "**************************** AppHarbr Permit to Display GAM Rewarded ****************************");
                    ahGamRewardedAd.getGamRewardedAd().show(GamRewardedActivity.this,
                            rewardItem -> Log.d("TAG",  "onUserEarnedReward: " + rewardItem));
                }
                else {
                    Log.d("LOG", "**************************** AppHarbr Blocked GAM Rewarded ****************************");
                    // You may call to reload Rewarded
                }
            } else {
                Log.d("TAG", "The GAM Rewarded wasn't loaded yet.");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//      **** (7) ****
//      Remove the view on Destroy
        AppHarbr.removeRewardedAd(ahGamRewardedAd);
    }

    private final AHListener ahListener = (view, unitId, adFormat, reasons)
            -> Log.d("LOG", "AppHarbr - onAdBlocked for: " + unitId + ", reason: " + Arrays.toString(reasons));

    private final RewardedAdLoadCallback adManagerRewardedAdLoadCallback =
            new RewardedAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    Log.d("LOG", "onAdLoaded");
                    super.onAdLoaded(rewardedAd);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Log.d("LOG", "onAdFailedToLoad: " + loadAdError.getMessage());
                    super.onAdFailedToLoad(loadAdError);
                }
            };
}