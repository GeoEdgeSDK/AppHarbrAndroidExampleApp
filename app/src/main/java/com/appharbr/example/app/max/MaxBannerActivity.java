package com.appharbr.example.app.max;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxBannerBinding;

public class MaxBannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ActivityMaxBannerBinding binding = ActivityMaxBannerBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());


    }
}