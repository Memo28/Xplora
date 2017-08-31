package com.example.memovaradegante.xploraapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.memovaradegante.xploraapp.fragments.LogInFragment;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();

    }
}
