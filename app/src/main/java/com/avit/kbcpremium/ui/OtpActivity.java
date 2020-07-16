package com.avit.kbcpremium.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avit.kbcpremium.AddressActivity;
import com.avit.kbcpremium.HomeActivity;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.auth.Userdata;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.ArrayList;

public class OtpActivity extends AppCompatActivity {

    private OtpView otpView;
    private Button validateButton;
    private String userOtp = "";
    private static final String TAG = "OtpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        final String phoneNo = getIntent().getStringExtra("phoneNo");
        final String finalOtp = getIntent().getStringExtra("otp");
        final String type = getIntent().getStringExtra("type");

        TextView infoView = findViewById(R.id.info);
        infoView.setText("Please type the verification code send to \n " + phoneNo);

        otpView = findViewById(R.id.otpView);
        validateButton = findViewById(R.id.validate);

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                userOtp = otp;
            }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + userOtp + " " + finalOtp);
                if(userOtp.equals(finalOtp)){
                    Toast.makeText(getApplicationContext(),"OTP Verified",Toast.LENGTH_SHORT)
                            .show();

                    if(type.equals("0")){
                        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                        intent.putExtra("phoneNo", phoneNo);
                        startActivity(intent);
                    }else {
                        saveToSharedPref(phoneNo);
                    }

                    setResult(RESULT_OK,null);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    private void saveToSharedPref(String phoneNo){

        ArrayList<String> items = getIntent().getBundleExtra("bundle").getStringArrayList("userData");
        Userdata userdata =  Userdata.getFromArray(items);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);

        String userName = SharedPrefNames.USER_NAME;
        String userPhone = SharedPrefNames.PH_NUMBER;
        String addressname = SharedPrefNames.ADDRESS;
        String userAuth = SharedPrefNames.ALLOW_ACCESS;
        String nearByName = SharedPrefNames.NEAR_ADDRESS;

        SharedPreferences.Editor  editor = sharedPreferences.edit();
        editor.putBoolean(userAuth,true);
        editor.putString(userName,userdata.getName());
        editor.putString(userPhone,phoneNo);
        editor.putString(addressname,userdata.getAddress());
        editor.putString(nearByName,userdata.getNearByAddress());

        editor.apply();

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
