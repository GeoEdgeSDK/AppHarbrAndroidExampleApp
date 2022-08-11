package com.appharbr.example.app.max;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxInterstitialBinding;

public class MaxInterstitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ActivityMaxInterstitialBinding binding = ActivityMaxInterstitialBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());
    }
}