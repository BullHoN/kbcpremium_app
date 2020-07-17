package com.avit.kbcpremium;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.avit.kbcpremium.notification.NotificationPostData;
import com.avit.kbcpremium.notification.NotificationResponseData;
import com.avit.kbcpremium.ui.orders.OrderItem;
import com.avit.kbcpremium.ui.orders.OrdersFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    final int UPI_PAYMENT = 0;
    HomeActivityViewModel viewModel;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel =
                ViewModelProviders.of(this).get(HomeActivityViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        TextView cartNo = findViewById(R.id.cart_num);

        sharedPreferences = getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);
        getFirebaseToken();

        int noCartItems;
        if(sharedPreferences.contains(SharedPrefNames.CART_NOITEMS)){
            noCartItems = sharedPreferences.getInt(SharedPrefNames.CART_NOITEMS,0);
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SharedPrefNames.CART_NOITEMS,0);
            editor.apply();
            noCartItems = 0;
        }
        cartNo.setText(String.valueOf(noCartItems));

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void getFirebaseToken() {

        final String socketIdName = SharedPrefNames.SOCKET_ID;
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("Token", "getInstanceId failed", task.getException());
                    return;
                }

                String token = task.getResult().getToken();
                editor.putString(socketIdName,token);

                editor.apply();

                Log.i("Token",token);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UPI_PAYMENT){
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response").toLowerCase();
                    // send notification to server
                    Log.i("UPI", "onActivityResult: " + trxt);
                    if(trxt.contains("success")) {
                        SendNotificationAndSaveOrder();
                    }else{
                        // payment was not successful
                        Toast.makeText(getApplicationContext(),"Order Was Canceled",Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    // some error in transaction
                    Toast.makeText(getApplicationContext(),"Some Error Occurred Try Again Later",Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                // when user simply back without payment
                Toast.makeText(getApplicationContext(),"Order Was Canceled",Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void SendNotificationAndSaveOrder(){
        Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT)
                .show();

        String orderItemName = SharedPrefNames.ORDERITEM;

        String orderItemString = sharedPreferences.getString(orderItemName,"");

        Gson gson = new Gson();
        OrderItem orderItem = gson.fromJson(orderItemString,OrderItem.class);

        viewModel.insert(orderItem);
        sendNotification(orderItem);
        startNewFragment();

    }

    private void startNewFragment() {
        Fragment ordersFragment = new OrdersFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment
                        , ordersFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();

        viewModel.clearCart();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharedPrefNames.CART_NOITEMS,0);
        editor.apply();
        updateCart();
    }

    private void updateCart(){
        TextView cartNum = findViewById(R.id.cart_num);
        cartNum.setText(String.valueOf(0));
    }

    private void sendNotification(OrderItem orderItem){
        String name = sharedPreferences.getString(SharedPrefNames.USER_NAME,"");
        String phoneNo = sharedPreferences.getString(SharedPrefNames.PH_NUMBER,"");
        String address = sharedPreferences.getString(SharedPrefNames.ADDRESS,"");
        String nearBy = sharedPreferences.getString(SharedPrefNames.NEAR_ADDRESS,"");
        String fcmId = sharedPreferences.getString(SharedPrefNames.SOCKET_ID,"");
        int deliveryCharge = sharedPreferences.getInt(SharedPrefNames.DELIVERYCHARGE,15);
        String orderItemsString = sharedPreferences.getString(SharedPrefNames.ORDERITEMS,"");

        String orderItems[] = orderItemsString.split("##");
        ArrayList<String> orderItems2 = new ArrayList<>();

        for(int i=0;i<orderItems.length;i++){
            orderItems2.add(orderItems[i]);
        }

        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        NotificationPostData data = new NotificationPostData(name,phoneNo,fcmId,orderItem.getTotal()
                ,orderItems2,address,nearBy,orderItem.getOrderId(),true,deliveryCharge);

        Call<NotificationResponseData> call = networkApi.sendNotificationToServer(data);

        call.enqueue(new Callback<NotificationResponseData>() {
            @Override
            public void onResponse(Call<NotificationResponseData> call, Response<NotificationResponseData> response) {

            }

            @Override
            public void onFailure(Call<NotificationResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Some Error Occured Contact Support",Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

}
