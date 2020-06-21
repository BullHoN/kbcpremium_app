package com.avit.kbcpremium.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.kbcpremium.R;
import com.avit.kbcpremium.ui.products.BrandItem;
import com.avit.kbcpremium.ui.products.ProductItem;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private ArrayList<BookingItem> booking1,booking2;
    private ArrayList<BrandItem> brandItems;

    public HomeViewModel() {
        booking1 = new ArrayList<>();
        booking1.add(new BookingItem("Hair Style", R.drawable.ic_hair_cut));
        booking1.add(new BookingItem("Facial",R.drawable.ic_makeup));
        booking1.add(new BookingItem("Coloring",R.drawable.ic_hair));
        booking1.add(new BookingItem("Makeup",R.drawable.ic_blush));

        booking2 = new ArrayList<>();
        booking2.add(new BookingItem("Hair Style", R.drawable.ic_hair_cut));
        booking2.add(new BookingItem("Facial",R.drawable.ic_makeup));
        booking2.add(new BookingItem("Makeup",R.drawable.ic_blush));

        ArrayList<ProductItem> productItems = new ArrayList<>();
        productItems.add(new ProductItem(20,"Vaineet Expert Absolute Repair Lipidium Shampoo 300ml",0,"knk"));
        productItems.add(new ProductItem(30,"Vaineet Expert Absolute Repair Lipidium Shampoo 300ml",10,""));
        productItems.add(new ProductItem(50,"Vaineet Expert Absolute Repair Lipidium Shampoo 300ml",20," "));
        productItems.add(new ProductItem(10,"Vaineet Expert Absolute Repair Lipidium Shampoo 300ml",30,"kjh"));

        brandItems = new ArrayList<>();
        brandItems.add(new BrandItem("Lotus Professional",productItems));
        brandItems.add(new BrandItem("Skeyndor",productItems));

    }

    public ArrayList<BrandItem> getProductItems(){
        return  brandItems;
    }

    public ArrayList<BookingItem> getBooking1(){
        return  booking1;
    }

    public ArrayList<BookingItem> getBooking2(){
        return booking2;
    }
}