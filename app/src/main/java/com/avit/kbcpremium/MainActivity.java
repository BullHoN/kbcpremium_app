package com.avit.kbcpremium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // some logic for checking weather user is logged in or not
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);

        final Intent nextActivity;
        if(sharedPreferences.contains(SharedPrefNames.ALLOW_ACCESS)){
            nextActivity = new Intent(this, HomeActivity.class);
        }else {
            nextActivity = new Intent(this,AuthActivity.class);
        }

        new CountDownTimer(1500,750){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(nextActivity);
                finish();
            }
        }.start();

    }
}
