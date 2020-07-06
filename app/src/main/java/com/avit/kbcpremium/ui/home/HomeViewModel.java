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

        String hairTreatment[] = {"Hair Treatment","Spa"};
        String BleachCleanUp[] = {"Bleach","CleanUp"};
        String colorHighlight[] = {"Color","Highlighting"};
        String hairRebounding[] = {"Hair Rebounding","Styling"};
        String bridalMakeUp[] = {"MakeUp"};
        String kbcSpecial[] = {"KBC Special"};
        String Mehandi[] = {"Mehandi"};


        booking1 = new ArrayList<>();
        booking1.add(new BookingItem("Hair \nTreatment", R.drawable.ic_hair_salon,hairTreatment));
        booking1.add(new BookingItem("Bleach & Cleanup",R.drawable.ic_makeup,BleachCleanUp));
        booking1.add(new BookingItem("Color & Highlight",R.drawable.ic_hair,colorHighlight));
        booking1.add(new BookingItem("Mehandi & Touchups",R.drawable.ic_henna_painted_hand,Mehandi));

        booking2 = new ArrayList<>();
        booking2.add(new BookingItem("Hair\nRebounding", R.drawable.ic_hair_cut,hairRebounding));
        booking2.add(new BookingItem("KBC\nSpecial",R.drawable.ic_magic_wand,kbcSpecial));
        booking2.add(new BookingItem("Make-Up\n(Bridal,Party)",R.drawable.ic_blush,bridalMakeUp));

        ArrayList<ProductItem> productItems = new ArrayList<>();
        productItems.add(new ProductItem(375,"Deep Pore Cleansing Face Wash(80 g)",1,"http:/"+ "/" +"18.188.149.40:5000/images/Lotus Professional/Deep Pore Cleansing Face Wash(80 g).png"));
        productItems.add(new ProductItem(865,"Brazilian Anti Ageing Skin Firming Creme(50 g)",2,"http:/" + "/" +"18.188.149.40:5000/images/Lotus Professional/Brazilian Anti Ageing Skin Firming Creme(50 g).png"));
        productItems.add(new ProductItem(455,"Bulgarian Rose Glow Skin Brightening Face Wash(80 g)",3,"http:/" + "/" + "18.188.149.40:5000/images/Lotus Professional/Brazilian Sprinkle Of Youth Face Wash(80 g).png"));
        productItems.add(new ProductItem(445,"Brazilian Sprinkle Of Youth Face Wash(80 g)",4,"http:/" + "/" + "18.188.149.40:5000/images/Lotus Professional/Brazilian Sprinkle Of Youth Face Wash(80 g).png"));

        ArrayList<ProductItem> skendorItems = new ArrayList<>();
        skendorItems.add(new ProductItem(1750,"Delicate Cleansing Milk(250 ml)",4,"http:/" + "/" + "18.188.149.40:5000/images/Skeyndor/Delicate Cleansing Milk(250 ml).png"));
        skendorItems.add(new ProductItem(2100,"Re-Balancing Geantle Creame FI(50 ml)",2,"http:/" + "/" + "18.188.149.40:5000/images/Skeyndor/Re-Balancing Geantle Creame FI(50 ml).png"));
        skendorItems.add(new ProductItem(1700,"Thermal Cleansing Gel(250 ml)",3,"http:/" + "/" + "18.188.149.40:5000/images/Skeyndor/Thermal Cleansing Gel(250 ml).png"));
        skendorItems.add(new ProductItem(950,"Thermal Concentrate Water(100 ml)",1,"http:/" + "/" + "18.188.149.40:5000/images/Skeyndor/Thermal Concentrate Water(100 ml).png"));

        ArrayList<ProductItem> lorelItems = new ArrayList<>();
        lorelItems.add(new ProductItem(380,"For Dry Hair  Hair Spa Deep Nourishing Conditioner(200 ml)",4,"http:/" + "/" + "18.188.149.40:5000/images/Loreal Professional/For Dry Hair  Hair Spa Deep Nourishing Conditioner      (200 ml).png"));
        lorelItems.add(new ProductItem(635,"For Flaky Scalp  Serie Expert Instant Clear Shampoo (300 ml)",4,"http:/" + "/" + "18.188.149.40:5000/images/Loreal Professional/For Flaky Scalp  Serie Expert Instant Clear Shampoo (300 ml).png"));
        lorelItems.add(new ProductItem(380,"For Frizzy Hair Hair Spa Smooth Revival Shampoo(250 ml)",4,"http:/" + "/" + "18.188.149.40:5000/images/Loreal Professional/For Frizzy Hair -Hair Spa Smooth Revival Shampoo (250 ml).png"));
        lorelItems.add(new ProductItem(380,"For Colored Hair  Hair Spa Color Pure Conditioner (200 ml)",4,"http:/" + "/" + "18.188.149.40:5000/images/Loreal Professional/For Colored Hair  Hair Spa Color Pure Conditioner (200 ml).png"));


        brandItems = new ArrayList<>();
        brandItems.add(new BrandItem("Lotus Professional",productItems));
        brandItems.add(new BrandItem("Skeyndor",skendorItems));
        brandItems.add(new BrandItem("Loreal Professional",lorelItems));

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