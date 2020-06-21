package com.avit.kbcpremium;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

}
