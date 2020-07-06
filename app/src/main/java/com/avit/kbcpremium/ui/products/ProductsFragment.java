package com.avit.kbcpremium.ui.products;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.icu.text.UnicodeSetSpanner;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.avit.kbcpremium.NetworkApi;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.RetrofitClient;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.dialogs.CartBottomSheetDialog;
import com.avit.kbcpremium.ui.cart.CartItem;
import com.avit.kbcpremium.ui.cart.CartViewModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductsFragment extends Fragment implements View.OnTouchListener, ViewTreeObserver.OnScrollChangedListener{

    private String TAG = "Products";
    private CartViewModel cartViewModel;
    private LinearLayout subCategoriesView;
    private ViewGroup viewGroup;
    private int start = 0,offset;
    private ScrollView scrollView;
    private String brandName;
    private ProgressBar firstProgress,secondProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_products,container,false);
        cartViewModel =
                ViewModelProviders.of(this).get(CartViewModel.class);


        viewGroup = container;

        firstProgress = root.findViewById(R.id.firstProgress);
        secondProgress = root.findViewById(R.id.secondProgress);

        scrollView = root.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(this);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        Bundle bundle = getArguments();
        brandName =  bundle.getString("brand_name","");

        TextView nwLine = root.findViewById(R.id.nwLine);
        nwLine.setText("All Available \nProducts of " + brandName);

        switch (brandName){
            case "Skeyndor":
                offset = 3;
                getBrandItems(brandName,start,offset);
                break;
            case "Lotus Professional":
                offset = 15;
                getBrandWithOnlyItems(brandName,start,offset);
                break;
            case "Loreal Professional":
                offset = 12;
                getBrandWithOnlyItems(brandName,start,offset);
                break;
        }
        subCategoriesView = root.findViewById(R.id.sub_categories);


        return root;
    }

    private void showSubCategory(LinearLayout linearLayout, ArrayList<SubProducts> subProducts, ViewGroup container){

        for(int i=0;i<subProducts.size();i++){
            View subCategoryView = getLayoutInflater().inflate(R.layout.sub_category_item,container,false);
            SubProducts curr = subProducts.get(i);

            TextView subCategoryTitle = subCategoryView.findViewById(R.id.title);
            subCategoryTitle.setText(curr.getSubCategory());

            LinearLayout products = subCategoryView.findViewById(R.id.products);
            showSubCategoryItems(products,curr.getItems(),container);

            linearLayout.addView(subCategoryView);
        }

    }

    private void showSubCategoryItems(LinearLayout subCategory,ArrayList<ProductItem> productItems,ViewGroup container){

        for(int i=0;i<productItems.size();i++){
            View productView = getLayoutInflater().inflate(R.layout.sub_product_item,container,false);
            final ProductItem curr = productItems.get(i);

//            Log.i(TAG,curr.getPrice() + curr.getName() + " " + curr.getImageUrl());
            ImageView productImage = productView.findViewById(R.id.productImage);
            Glide.with(getContext()).load(curr.getImageUrl()).into(productImage);

            TextView productNameView = productView.findViewById(R.id.product_name);
            productNameView.setText(curr.getName());

            TextView discountView = productView.findViewById(R.id.discount);
            if(curr.getDiscount() !=0){
                discountView.setText(curr.getDiscount() + "% off");
                discountView.setVisibility(View.VISIBLE);
            }

            TextView productPriceView = productView.findViewById(R.id.product_price);
            TextView productPrice2View = productView.findViewById(R.id.product_price2);

            productPriceView.setText("Price: ₹" + curr.getPrice());
            float price = curr.getPrice();
            if(curr.getDiscount() !=0) {
                price = curr.getPrice() - curr.getPrice()*(float)(curr.getDiscount()/100.0);
                productPriceView.setPaintFlags(productPriceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                productPrice2View.setText("Price: ₹" + Math.round(price));
                productPrice2View.setVisibility(View.VISIBLE);
            }
            final int finalPrice = Math.round(price);

            Button addtocartButton = productView.findViewById(R.id.addtocart);
            addtocartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    incrementTheCartNo();
                    addToCart(new CartItem(curr.getName(),finalPrice,curr.getImageUrl()));

                    // open cart dialog
                    Bundle bundle = new Bundle();
                    bundle.putString("item_name",curr.getName());

                    CartBottomSheetDialog cartBottomSheetDialog = new CartBottomSheetDialog();
                    cartBottomSheetDialog.setArguments(bundle);
                    cartBottomSheetDialog.show(getFragmentManager(),"addToCart");

                }
            });

            subCategory.addView(productView);
        }

    }

    private void addToCart(CartItem cartItem){
        cartViewModel.insert(cartItem);
    }

    private void incrementTheCartNo(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);
        int currVal = sharedPreferences.getInt(SharedPrefNames.CART_NOITEMS,0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharedPrefNames.CART_NOITEMS,currVal+1);
        editor.apply();

        TextView cartNum = getActivity().findViewById(R.id.cart_num);
        cartNum.setText(String.valueOf(currVal+1));
    }

    private void getBrandItems(String brandName,int start,int offset){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        Call<ArrayList<SubProducts>> call = networkApi.getBrandItems(brandName,offset,start);
        call.enqueue(new Callback<ArrayList<SubProducts>>() {
            @Override
            public void onResponse(Call<ArrayList<SubProducts>> call, Response<ArrayList<SubProducts>> response) {
                ArrayList<SubProducts> subProducts = response.body();
                if(subProducts == null){
                    Log.i(TAG,"Null res");
                }else {
                    if(subProducts.size() > 0) {
                        showSubCategory(subCategoriesView, subProducts, viewGroup);
                    }else {
                        secondProgress.setVisibility(View.GONE);
                    }
                    firstProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SubProducts>> call, Throwable t) {
                Toast.makeText(getContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showOnlyItems(ArrayList<ProductItem> productItems){
        View categoryView = getLayoutInflater().inflate(R.layout.sub_category_item,viewGroup,false);
        LinearLayout products = categoryView.findViewById(R.id.products);
        showSubCategoryItems(products,productItems,viewGroup);

        subCategoriesView.addView(categoryView);

    }

    private void getBrandWithOnlyItems(String brandName,int start,int offset){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        Call<ArrayList<ProductItem>> call = networkApi.getBrandWithOnlyItems(brandName,offset,start);

        call.enqueue(new Callback<ArrayList<ProductItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductItem>> call, Response<ArrayList<ProductItem>> response) {
                ArrayList<ProductItem> productItems = response.body();
                if(productItems.size() > 0) {
                    showOnlyItems(productItems);
                }else {
                    secondProgress.setVisibility(View.GONE);
                }
                firstProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<ProductItem>> call, Throwable t) {
                Toast.makeText(getContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onScrollChanged() {
        View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int bottomDetector = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
        if(bottomDetector == 0 ){
            start = offset;
            offset = start + offset;
            switch (brandName){
                case "Skeyndor":
                    getBrandItems(brandName,start,offset);
                    break;
                case "Lotus Professional":
                    getBrandWithOnlyItems(brandName,start,offset);
                    break;
                case "Loreal Professional":
                    getBrandWithOnlyItems(brandName,start,offset);
                    break;
            }
        }
    }
}
