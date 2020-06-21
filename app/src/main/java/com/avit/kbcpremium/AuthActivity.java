package com.avit.kbcpremium;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avit.kbcpremium.auth.LoginPostData;
import com.avit.kbcpremium.auth.LoginResponseData;
import com.avit.kbcpremium.auth.Userdata;
import com.avit.kbcpremium.dialogs.LoginBottomSheetDialog;
import com.avit.kbcpremium.dialogs.SignUpBottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthActivity extends AppCompatActivity {

    private EditText phoneNoView;
    private String TAG = "Auth";
    private int EXIT_CODE = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        phoneNoView = findViewById(R.id.phoneNo);
        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = phoneNoView.getText().toString();
                if(phoneNo.length() != 10){
                    Toast.makeText(getApplicationContext(),"Not A Valid Number",Toast.LENGTH_SHORT).show();
                    return;
                }

                // some way to verify the number
                sendLoginData(phoneNo);
            }
        });

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
                    // start register activity
                    Intent intent = new Intent(getApplicationContext(),AddressActivity.class);
                    intent.putExtra("phoneNo",phoneNo);
                    startActivityForResult(intent,EXIT_CODE);
                }else {
                    Userdata userdata = loginResponseData.getAccountData();
                    saveToSharedPref(userdata,phoneNo);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Some Error occured",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveToSharedPref(Userdata userdata,String phoneNo){
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
