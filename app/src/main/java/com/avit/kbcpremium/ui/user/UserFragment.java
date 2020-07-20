package com.avit.kbcpremium.ui.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.avit.kbcpremium.AuthActivity;
import com.avit.kbcpremium.MainActivity;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.ui.additionals.AboutmeFragment;
import com.avit.kbcpremium.ui.additionals.RefundFragment;
import com.avit.kbcpremium.ui.additionals.privacyFragment;
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

        root.findViewById(R.id.terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment refundFragment = new RefundFragment();

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment,refundFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        root.findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment privacy = new privacyFragment();

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment,privacy)
                        .addToBackStack(null)
                        .commit();

            }
        });

        root.findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aboutFragment = new AboutmeFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment,aboutFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

            }
        });

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

        root.findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        root.findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsup();
            }
        });

        return root;
    }

    private void call(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "9935179356"));
        getContext().startActivity(intent);
    }

    private void openWhatsup(){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://api.whatsapp.com/send?phone=+919935179356"));
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

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