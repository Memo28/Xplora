package com.example.memovaradegante.xploraapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.memovaradegante.xploraapp.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }
}
