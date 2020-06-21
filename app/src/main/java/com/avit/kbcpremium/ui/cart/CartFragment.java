package com.avit.kbcpremium.ui.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.avit.kbcpremium.NetworkApi;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.RetrofitClient;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.dialogs.PaymentAddressSheetDialog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private String TAG = "CART";
    private LinearLayout cartItemsView;
    private ViewGroup viewGroup;
    private List<CartItem> allCartItems;
    private int from = 0;
    private int mSubtotal,mTotal,deliveryCharge = 10;
    private boolean isAvailable = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart,container,false);
        cartViewModel =
                ViewModelProviders.of(this).get(CartViewModel.class);
        viewGroup = container;

        checkAvailability();
        // checkout button
        root.findViewById(R.id.checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAvailable){

                    ArrayList<String> items = new ArrayList<>();
                    for(CartItem cartItem: allCartItems){
                        items.add(cartItem.toString().replace("CartItem",""));
                    }

                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("order_items",items);
                    bundle.putInt("total_amount",mTotal);
                    bundle.putInt("delivery_amount",deliveryCharge);

                    PaymentAddressSheetDialog paymentAddressSheetDialog =
                            new PaymentAddressSheetDialog();
                    paymentAddressSheetDialog.setArguments(bundle);

                    paymentAddressSheetDialog.show(getFragmentManager(),"Chekcout_dialog");

                }else {
                    Toast.makeText(getContext(),"Sorry Saloon is currently closed",Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


        cartItemsView = root.findViewById(R.id.cart_items);
        final TextView cartNoView = root.findViewById(R.id.no_items);
        final TextView subTotalView = root.findViewById(R.id.subtotal_amount);
        final TextView totalView = root.findViewById(R.id.total_amount);
        final LinearLayout priceViews = root.findViewById(R.id.charges);
        final TextView emptyView = root.findViewById(R.id.empty);

        cartViewModel.getAllItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                allCartItems = cartItems;
                showAllCartItems(from);
                from = allCartItems.size();
                updateCartNo(allCartItems.size());

                cartNoView.setText(allCartItems.size() + " items");
                mSubtotal =  calculateSubTotal();
                subTotalView.setText("₹" + mSubtotal);

                mTotal = mSubtotal + deliveryCharge;
                totalView.setText("₹" + mTotal);

                if(cartItems.size() > 0){
                    priceViews.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }else {
                    priceViews.setVisibility(View.GONE);
                }

            }
        });

        return root;
    }

    private void showAllCartItems(int from){
        for(int i=from;i<allCartItems.size();i++){
            final CartItem cartItem = allCartItems.get(i);
            final View cartView = getLayoutInflater().inflate(R.layout.cart_item,viewGroup,false);

            ImageView imageView = cartView.findViewById(R.id.productImage);
            Glide.with(getContext()).load(cartItem.getImageUrl()).into(imageView);

            TextView nameView = cartView.findViewById(R.id.product_name);
            nameView.setText(cartItem.getItemName());

            final TextView priceView = cartView.findViewById(R.id.product_price);
            priceView.setText("Total: ₹" + cartItem.getTotal());

            final TextView quantityView = cartView.findViewById(R.id.quantity);
            quantityView.setText(String.valueOf(cartItem.getQuantity()));

            ImageButton addButton = cartView.findViewById(R.id.add_button);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItem.incrementedQuantity();
                    cartViewModel.update(cartItem);
                    quantityView.setText(String.valueOf(cartItem.getQuantity()));
                    priceView.setText("Total: ₹" + cartItem.getTotal());
                }
            });

            ImageButton subButton = cartView.findViewById(R.id.sub_button);
            subButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cartItem.getQuantity() == 1){
                        cartViewModel.delete(cartItem);
                        cartItemsView.removeView(cartView);
                        return;
                    }
                    cartItem.decrementedQuantity();
                    cartViewModel.update(cartItem);
                    quantityView.setText(String.valueOf(cartItem.getQuantity()));
                    priceView.setText("Total: ₹" + cartItem.getTotal());
                }
            });

            cartItemsView.addView(cartView);
        }
    }

    private void updateCartNo(int val){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharedPrefNames.CART_NOITEMS,val);
        editor.apply();

        TextView cartNum = getActivity().findViewById(R.id.cart_num);
        cartNum.setText(String.valueOf(val));
    }

    private int calculateSubTotal(){
        int subtotal = 0;
        for(int i=0;i<allCartItems.size();i++){
            subtotal += allCartItems.get(i).getTotal();
        }
        return  subtotal;
    }

    private boolean checkAvailability(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        Call<AvailabilityResponse> call = networkApi.checkAvailability();

        call.enqueue(new Callback<AvailabilityResponse>() {
            @Override
            public void onResponse(Call<AvailabilityResponse> call, Response<AvailabilityResponse> response) {
                AvailabilityResponse availabilityResponse = response.body();
                isAvailable = availabilityResponse.isAvailable();
            }

            @Override
            public void onFailure(Call<AvailabilityResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Check Your Network",Toast.LENGTH_SHORT).show();
            }
        });

        return false;
    }

}
