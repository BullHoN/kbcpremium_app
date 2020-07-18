package com.avit.kbcpremium;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avit.kbcpremium.auth.LoginPostData;
import com.avit.kbcpremium.auth.LoginResponseData;
import com.avit.kbcpremium.auth.Userdata;
import com.avit.kbcpremium.dialogs.LoginBottomSheetDialog;
import com.avit.kbcpremium.dialogs.SignUpBottomSheetDialog;
import com.avit.kbcpremium.ui.OtpActivity;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthActivity extends AppCompatActivity {

    private EditText phoneNoView;
    private String TAG = "Auth";
    private int EXIT_CODE = 13;
    private ProgressBar progressBar;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        progressBar = findViewById(R.id.progress);
        verifyButton = findViewById(R.id.verify);

        phoneNoView = findViewById(R.id.phoneNo);
        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNo = phoneNoView.getText().toString();
                if(phoneNo.length() != 10 || !isPhoneNo(phoneNo)){
                    Toast.makeText(getApplicationContext(),"Not A Valid Number",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                verifyButton.setClickable(false);
                // some way to verify the number
                sendLoginData(phoneNo);
            }
        });

    }

    private Boolean isPhoneNo(String no){
        for(int i=0;i<no.length();i++){
            if(!isNumber(no.charAt(i))){
                return false;
            }
        }
        return true;
    }

    private Boolean isNumber(char ch){
        if(ch == '0' || ch == '1' || ch == '2' || ch == '3'
            || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9'){
            return true;
        }
        return false;
    }

    private void sendLoginData(final String phoneNo){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        LoginPostData data = new LoginPostData(phoneNo);
        Call<LoginResponseData> call = networkApi.signIn(data);

        call.enqueue(new Callback<LoginResponseData>() {
            @Override
            public void onResponse(Call<LoginResponseData> call, Response<LoginResponseData> response) {
                LoginResponseData loginResponseData = response.body();
                Log.i(TAG,loginResponseData.toString());
                if(!loginResponseData.getAccountExists()){
                    // start otp activity
                    Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                    intent.putExtra("phoneNo",phoneNo);
                    intent.putExtra("otp",loginResponseData.getOtp());
                    intent.putExtra("type","0");

                    startActivityForResult(intent,EXIT_CODE);

                }else {
                    Userdata userdata = loginResponseData.getAccountData();

                    Gson gson = new Gson();
                    String orderItems =  gson.toJson(userdata.getOrderItems());

                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("userData",userdata.getTheArray());
                    bundle.putString("orderItems",orderItems);

                    Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                    intent.putExtra("bundle",bundle);
                    intent.putExtra("type","1");
                    intent.putExtra("phoneNo",phoneNo);
                    intent.putExtra("otp",loginResponseData.getOtp());

                    startActivityForResult(intent,EXIT_CODE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Some Error occured",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveToSharedPref(Userdata userdata,String phoneNo,String otp){
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

        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXIT_CODE && resultCode == RESULT_OK){
            finish();
        }
    }
}
