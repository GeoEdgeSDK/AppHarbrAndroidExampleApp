package com.appharbr.example.app.max;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.appharbr.example.app.R;
import com.appharbr.example.app.databinding.ActivityMaxBannerBinding;

public class MaxBannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActivityMaxBannerBinding binding = ActivityMaxBannerBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());


    }
}