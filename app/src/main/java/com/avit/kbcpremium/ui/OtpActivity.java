package com.avit.kbcpremium.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avit.kbcpremium.AddressActivity;
import com.avit.kbcpremium.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

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
                if(userOtp.equals(finalOtp)){
                    Toast.makeText(getApplicationContext(),"OTP Verified",Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                    intent.putExtra("phoneNo",phoneNo);
                    startActivity(intent);

                    setResult(RESULT_OK,null);
                    finish();
                }
            }
        });

    }
}
