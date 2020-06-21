package com.avit.kbcpremium.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.FtsOptions;

import com.avit.kbcpremium.CheckoutAddressActivity;
import com.avit.kbcpremium.HomeActivity;
import com.avit.kbcpremium.HomeActivityViewModel;
import com.avit.kbcpremium.NetworkApi;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.RetrofitClient;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.notification.NotificationPostData;
import com.avit.kbcpremium.notification.NotificationResponseData;
import com.avit.kbcpremium.ui.orders.OrderItem;
import com.avit.kbcpremium.ui.orders.OrdersFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaymentAddressSheetDialog extends BottomSheetDialogFragment {

    private int EXIT_CODE = 13;
    private TextView addressView;
    private SharedPreferences sharedPreferences;
    private String TAG = "CART";
    private final int idLength = 18;
    private ArrayList<String> orderItems;
    private int totalAmount,deliveryCharge;
    private String orderId;
    private HomeActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.bottom_sheet_address_checkout,container,false);

         viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);

         Bundle bundle = getArguments();
         orderItems = bundle.getStringArrayList("order_items");
         totalAmount = bundle.getInt("total_amount");
         deliveryCharge = bundle.getInt("delivery_amount");


        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);
        String address = sharedPreferences.getString(SharedPrefNames.ADDRESS,"");
        String phoneNo = sharedPreferences.getString(SharedPrefNames.PH_NUMBER,"");


        addressView = root.findViewById(R.id.address);
        addressView.setText(address);

        TextView phoneNoView = root.findViewById(R.id.phoneNo);
        phoneNoView.setText(phoneNo);

        root.findViewById(R.id.change_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CheckoutAddressActivity.class);
                startActivityForResult(intent,EXIT_CODE);
            }
        });


        root.findViewById(R.id.cash_on_delivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askFinalPayment();
            }
        });

         return root;
    }

    private String generateOrderId(int n){
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private void askFinalPayment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do You Want To Place The Order ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startPaymentProcess(false);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void startPaymentProcess(boolean upi){
        orderId = generateOrderId(idLength);
        if(orderId.length() != 0 && totalAmount != 0){
            if(upi){

            }else {
                Toast.makeText(getContext(),"Order is successfully made",Toast.LENGTH_SHORT)
                        .show();
                OrderItem orderItem = new OrderItem(0,totalAmount,orderItems.size(),addressView.getText().toString()
                        ,"Order in Processing",makeItemsString(),orderId);
                saveAndClearCart(orderItem);
                SendNotificationAndSaveOrder(false,orderItem);
            }
            dismiss();
        }
    }

    private void SendNotificationAndSaveOrder(boolean isPaid, final OrderItem orderItem){

        String name = sharedPreferences.getString(SharedPrefNames.USER_NAME,"");
        String phoneNo = sharedPreferences.getString(SharedPrefNames.PH_NUMBER,"");
        final String address = sharedPreferences.getString(SharedPrefNames.ADDRESS,"");
        String nearBy = sharedPreferences.getString(SharedPrefNames.NEAR_ADDRESS,"");
        String fcmId = sharedPreferences.getString(SharedPrefNames.SOCKET_ID,"");

        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        NotificationPostData data = new NotificationPostData(name,phoneNo,fcmId,totalAmount
                ,orderItems,address,nearBy,orderId,isPaid,deliveryCharge);

        Call<NotificationResponseData> call = networkApi.sendNotificationToServer(data);

        call.enqueue(new Callback<NotificationResponseData>() {
            @Override
            public void onResponse(Call<NotificationResponseData> call, Response<NotificationResponseData> response) {
                NotificationResponseData responseData = response.body();
                Log.i(TAG,responseData.isStatus()+"");
                if(!responseData.isStatus()){

                }
            }

            @Override
            public void onFailure(Call<NotificationResponseData> call, Throwable t) {
                Toast.makeText(getContext(), "Try Again Later", Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    private void saveAndClearCart(OrderItem orderItem){
        Log.i(TAG,"Order Successs");

        viewModel.insert(orderItem);
        startNewFragment();

    }

    private void startNewFragment() {
        Fragment ordersFragment = new OrdersFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
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
        TextView cartNum = getActivity().findViewById(R.id.cart_num);
        cartNum.setText(String.valueOf(0));
    }

    private String makeItemsString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(String orderItem : orderItems){
            String item = orderItem.split(",")[0].split("=")[1].replace("'","");
            stringBuilder.append(item + ",");
        }
        return stringBuilder.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        String address = sharedPreferences.getString(SharedPrefNames.ADDRESS,"");
        addressView.setText(address);
    }
}
