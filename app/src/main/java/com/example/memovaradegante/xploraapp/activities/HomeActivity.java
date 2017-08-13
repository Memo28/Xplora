package com.example.memovaradegante.xploraapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.memovaradegante.xploraapp.R;
import com.example.memovaradegante.xploraapp.fragments.ChatFragment;
import com.example.memovaradegante.xploraapp.fragments.ProfileFragment;
import com.example.memovaradegante.xploraapp.fragments.SavedFragment;
import com.example.memovaradegante.xploraapp.fragments.StartFragment;


public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()){
                case R.id.tab_home:
                    fragmentTransaction.replace(R.id.content, new StartFragment()).commit();
                    return true;
                case R.id.tab_chat:
                    fragmentTransaction.replace(R.id.content, new ChatFragment()).commit();
                    return true;
                case R.id.tab_fab:
                    fragmentTransaction.replace(R.id.content, new SavedFragment()).commit();
                    return true;
                case R.id.tab_profile:
                    fragmentTransaction.replace(R.id.content, new ProfileFragment()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, new StartFragment()).commit();
    }

}
