package com.appharbr.example.app.max;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxRewardedBinding;

public class MaxRewardedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ActivityMaxRewardedBinding binding = ActivityMaxRewardedBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());

    }
}