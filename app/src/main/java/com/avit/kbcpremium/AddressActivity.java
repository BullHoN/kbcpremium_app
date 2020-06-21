package com.avit.kbcpremium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.avit.kbcpremium.auth.LoginResponseData;
import com.avit.kbcpremium.auth.RegisterPostData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddressActivity extends AppCompatActivity {

    private String phoneNo;
    private String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        phoneNo = getIntent().getStringExtra("phoneNo");
        final EditText nameView = findViewById(R.id.name);
        final EditText addressView = findViewById(R.id.rawaddress);
        final EditText nearByAddressView = findViewById(R.id.nearbyaddress);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString().trim();
                String address = addressView.getText().toString().trim();
                String nearByAddress = nearByAddressView.getText().toString().trim();

                if(validateSpecialChars(name) || validateSpecialChars(address) || validateSpecialChars(nearByAddress)){
                    Toast.makeText(getApplicationContext(),"Enter Valid Information",Toast.LENGTH_SHORT).show();
                    return;
                }

                signUp(name,address,nearByAddress,phoneNo);

            }
        });

    }

    private void signUp(final String name, final String address, final String nearbyAddress, final String phoneNo){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        RegisterPostData data = new RegisterPostData(name,phoneNo,address,nearbyAddress);
        Call<ResponseBody> call = networkApi.signUp(data);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                Log.i(TAG,"Registered Success");
                saveToSharedPrefs(name,phoneNo,address,nearbyAddress);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Some error Occured",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveToSharedPrefs(String name,String address,String nearbyAddress,String phoneNo){
        String dbName = SharedPrefNames.DB_NAME;
        SharedPreferences sharedPreferences = getSharedPreferences(dbName,Context.MODE_PRIVATE);

        String userName = SharedPrefNames.USER_NAME;
        String userPhone = SharedPrefNames.PH_NUMBER;
        String addressname = SharedPrefNames.ADDRESS;
        String userAuth = SharedPrefNames.ALLOW_ACCESS;
        String nearByName = SharedPrefNames.NEAR_ADDRESS;

        SharedPreferences.Editor  editor = sharedPreferences.edit();
        editor.putBoolean(userAuth,true);
        editor.putString(userName,name);
        editor.putString(userPhone,phoneNo);
        editor.putString(addressname,address);
        editor.putString(nearByName,nearbyAddress);

        editor.apply();

        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        setResult(RESULT_OK,null);
        finish();
    }



    private boolean validateSpecialChars(String item){
        return item.contains("<") || item.contains(">") || item.contains("//") || item.contains("'") || item.length() < 3;
    }

}
