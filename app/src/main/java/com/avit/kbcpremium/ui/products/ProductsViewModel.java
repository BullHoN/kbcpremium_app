package com.avit.kbcpremium.ui.products;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.avit.kbcpremium.NetworkApi;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductsViewModel extends ViewModel {

    private ArrayList<SubProducts> subProducts1;
    private HashMap<String,String> filterData;
    private String test;

    public ProductsViewModel(){
        ArrayList<ProductItem> productItems = new ArrayList<>();
        productItems.add(new ProductItem(20,"Vaineet Expert Absolute Repair Lipidium Shampoo 300ml",0, "aksnfkansf"));
        productItems.add(new ProductItem(30,"Vaineet Expert Absolute Repair Lipidium Shampoo 300ml",10,"askfkasfn"));
        productItems.add(new ProductItem(50,"Vaineet Expert Absolute Repair Lipidium Shampoo 300ml",20,"ljdfjd"));
        productItems.add(new ProductItem(10,"Vaineet Expert Absolute Repair Lipidium Shampoo 300ml",30,"kasnfkansf"));

        subProducts1 = new ArrayList<>();
        subProducts1.add(new SubProducts("Shampoes",productItems));
        subProducts1.add(new SubProducts("Zeher1",productItems));

        filterData = new HashMap<>();
        filterData.put("shampoo","shampoo");
        filterData.put("mascare","zeher1");

    }

    public void setTest(String test) {
        Log.i("Products","Set is called");
        this.test = test;
    }

    public String getTest() {
        return test;
    }

    public HashMap<String, String> getFilterData() {
        return filterData;
    }

    public ArrayList<SubProducts> getSubProducts1() {
        return subProducts1;
    }
}
