package com.appharbr.example.app.admob;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.R;
import com.appharbr.sdk.engine.AdSdk;
import com.appharbr.sdk.engine.AdStateResult;
import com.appharbr.sdk.engine.AppHarbr;
import com.appharbr.sdk.engine.listeners.AHListener;
import com.appharbr.sdk.engine.mediators.admob.rewarded.AHAdMobRewardedAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;

public class AdMobRewardedActivity extends AppCompatActivity {

    private Button btnDisplay;
    private AHAdMobRewardedAd ahAdMobRewardedAd = new AHAdMobRewardedAd();
    private RewardedAdLoadCallback ahWrapperListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admob_rewarded_layout);

        btnDisplay = findViewById(R.id.admob_rewarded_display);
        setDisplayClick();

        // The publisher will initiate once the listener wrapper and will use it when load the AdMob Rewarded ad.
        ahWrapperListener = AppHarbr
                .addRewardedAd(AdSdk.ADMOB,
                        ahAdMobRewardedAd,
                        mRewardedLoadCallback,
                        ahListener);

        // The publisher load AdMob Rewarded ad
        requestAd();
    }

    private void requestAd() {
        btnDisplay.setEnabled(false);
        RewardedAd.load(getApplicationContext(),
                getApplicationContext().getResources().getString(R.string.admob_rewarded_ad_unit_id),
                new AdRequest.Builder().build(),
                ahWrapperListener); //AppHarbr wrapper listener
    }

    private void setDisplayClick() {
        btnDisplay.setOnClickListener((view) -> {
            // The publisher display the AdMob Rewarded
            if (isDestroyed()) {
                return;
            }
            if (ahAdMobRewardedAd.getAdMobRewardedAd() != null) {
                final AdStateResult interstitialState = AppHarbr.getRewardedState(ahAdMobRewardedAd);
                if (interstitialState != AdStateResult.BLOCKED) {
                    Log.d("LOG", "**************************** AppHarbr Permit to Display AdMob Rewarded ****************************");
                    ahAdMobRewardedAd.getAdMobRewardedAd().show(AdMobRewardedActivity.this,
                            rewardItem -> Log.d("TAG",  "onUserEarnedReward: " + rewardItem));
                }
                else {
                    Log.d("LOG", "**************************** AppHarbr Blocked AdMob Rewarded ****************************");
                    // You may call to reload Rewarded
                }
            } else {
                Log.d("TAG", "The AdMob Rewarded wasn't loaded yet.");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppHarbr.removeRewardedAd(ahAdMobRewardedAd);
    }

    private final AHListener ahListener = (view, unitId, adFormat, reasons)
            -> Log.d("LOG", "AppHarbr - onAdBlocked for: " + unitId + ", reason: " + Arrays.toString(reasons));

    private final RewardedAdLoadCallback mRewardedLoadCallback = new RewardedAdLoadCallback() {

        @Override
        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
            super.onAdLoaded(rewardedAd);
            Log.e("TAG", "AdMob - RewardedAdLoadCallback - onAdLoaded");
            btnDisplay.setEnabled(true);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            Log.e("TAG", "AdMob - RewardedAdLoadCallback - onAdFailedToLoad: " + loadAdError.getMessage());
        }
    };
}