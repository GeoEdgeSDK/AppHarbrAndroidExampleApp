package com.appharbr.example.app.max;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appharbr.example.app.databinding.ActivityMaxRecyclerViewBinding;

public class MaxRecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ActivityMaxRecyclerViewBinding binding = ActivityMaxRecyclerViewBinding.inflate(getLayoutInflater());
	setContentView(binding.getRoot());
    }
}