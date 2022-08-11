package com.appharbr.example.app.max;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxNativeAdBinding;

public class MaxNativeAdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ActivityMaxNativeAdBinding binding = ActivityMaxNativeAdBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());
    }
}