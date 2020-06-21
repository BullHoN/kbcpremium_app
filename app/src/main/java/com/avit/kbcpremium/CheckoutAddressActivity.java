package com.avit.kbcpremium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CheckoutAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_address);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);
        String address = sharedPreferences.getString(SharedPrefNames.ADDRESS,"");
        String name = sharedPreferences.getString(SharedPrefNames.USER_NAME,"");
        final String nearByAddress = sharedPreferences.getString(SharedPrefNames.NEAR_ADDRESS,"");

        final EditText nameView = findViewById(R.id.name);
        nameView.setText(name);

        final EditText addressView = findViewById(R.id.rawaddress);
        addressView.setText(address);

        final EditText nearByAddressView = findViewById(R.id.nearbyaddress);
        nearByAddressView.setText(nearByAddress);

        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName = nameView.getText().toString().trim();
                String mAddress = addressView.getText().toString().trim();
                String mNearBy = nearByAddressView.getText().toString().trim();

                if(validateSpecialChars(mName) || validateSpecialChars(mAddress) || validateSpecialChars(mNearBy)){
                    Toast.makeText(getApplicationContext(),"Enter Valid Information",Toast.LENGTH_SHORT).show();
                    return;
                }

                saveToSharedPrefs(mName,mAddress,mNearBy);

            }
        });

    }

    private void saveToSharedPrefs(String name,String address,String nearbyAddress){
        String dbName = SharedPrefNames.DB_NAME;
        SharedPreferences sharedPreferences = getSharedPreferences(dbName,Context.MODE_PRIVATE);

        String userName = SharedPrefNames.USER_NAME;
        String addressname = SharedPrefNames.ADDRESS;
        String nearByName = SharedPrefNames.NEAR_ADDRESS;

        SharedPreferences.Editor  editor = sharedPreferences.edit();
        editor.putString(userName,name);
        editor.putString(addressname,address);
        editor.putString(nearByName,nearbyAddress);

        editor.apply();

        finish();
    }

    private boolean validateSpecialChars(String item){
        return item.contains("<") || item.contains(">") || item.contains("//") || item.contains("'") || item.length() < 3;
    }

}
