package com.avit.kbcpremium.ui.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.avit.kbcpremium.AuthActivity;
import com.avit.kbcpremium.MainActivity;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.ui.orders.OrdersFragment;

public class UserFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        sharedPreferences = sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);

        root.findViewById(R.id.my_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ordersFragment = new OrdersFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment
                                , ordersFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });

        root.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertBox();
            }
        });

        return root;
    }

    private void showAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Do You want to logout ?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearAllData();
                    }
                });

        builder.show();
    }

    private void clearAllData(){
        // shared pref,order repo,cart repo, appoint repo
        notificationsViewModel.clearAll();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);

        getActivity().finish();
    }

}