package com.avit.kbcpremium.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.kbcpremium.R;
import com.avit.kbcpremium.db.CartItemRepository;
import com.avit.kbcpremium.ui.cart.CartItem;
import com.avit.kbcpremium.ui.products.BrandItem;
import com.avit.kbcpremium.ui.products.ProductItem;

import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {

    private ArrayList<BookingItem> booking1,booking2;
    private ArrayList<BrandItem> brandItems;
    private CartItemRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        repository = new CartItemRepository(application);

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
        booking1.add(new BookingItem("Mehandi",R.drawable.ic_henna,Mehandi));

        booking2 = new ArrayList<>();
        booking2.add(new BookingItem("Hair\nRebounding", R.drawable.ic_hair_cut,hairRebounding));
        booking2.add(new BookingItem("KBC\nSpecial",R.drawable.ic_magic_wand,kbcSpecial));
        booking2.add(new BookingItem("Make-Up\n(Bridal,Party)",R.drawable.ic_wedding,bridalMakeUp));

        ArrayList<ProductItem> productItems = new ArrayList<>();
        productItems.add(new ProductItem(375,"Deep Pore Cleansing Face Wash(80 g)",0,"http:/"+ "/" +"18.188.149.40:5000/images/Lotus Professional/Deep Pore Cleansing Face Wash(80 g).png"));
        productItems.add(new ProductItem(865,"Brazilian Anti Ageing Skin Firming Creme(50 g)",0,"http:/" + "/" +"18.188.149.40:5000/images/Lotus Professional/Brazilian Anti Ageing Skin Firming Creme(50 g).png"));
        productItems.add(new ProductItem(455,"Bulgarian Rose Glow Skin Brightening Face Wash(80 g)",0,"http:/" + "/" + "18.188.149.40:5000/images/Lotus Professional/Brazilian Sprinkle Of Youth Face Wash(80 g).png"));
        productItems.add(new ProductItem(445,"Brazilian Sprinkle Of Youth Face Wash(80 g)",0,"http:/" + "/" + "18.188.149.40:5000/images/Lotus Professional/Brazilian Sprinkle Of Youth Face Wash(80 g).png"));

        ArrayList<ProductItem> skendorItems = new ArrayList<>();
        skendorItems.add(new ProductItem(1750,"Delicate Cleansing Milk(250 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Skeyndor/Delicate Cleansing Milk(250 ml).png"));
        skendorItems.add(new ProductItem(2100,"Re-Balancing Geantle Creame FI(50 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Skeyndor/Re-Balancing Geantle Creame FI(50 ml).png"));
        skendorItems.add(new ProductItem(1700,"Thermal Cleansing Gel(250 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Skeyndor/Thermal Cleansing Gel(250 ml).png"));
        skendorItems.add(new ProductItem(950,"Thermal Concentrate Water(100 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Skeyndor/Thermal Concentrate Water(100 ml).png"));

        ArrayList<ProductItem> lorelItems = new ArrayList<>();
        lorelItems.add(new ProductItem(380,"For Dry Hair  Hair Spa Deep Nourishing Conditioner(200 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Loreal Professional/For Dry Hair  Hair Spa Deep Nourishing Conditioner      (200 ml).png"));
        lorelItems.add(new ProductItem(635,"For Flaky Scalp  Serie Expert Instant Clear Shampoo (300 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Loreal Professional/For Flaky Scalp  Serie Expert Instant Clear Shampoo (300 ml).png"));
        lorelItems.add(new ProductItem(380,"For Frizzy Hair Hair Spa Smooth Revival Shampoo(250 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Loreal Professional/For Frizzy Hair -Hair Spa Smooth Revival Shampoo (250 ml).png"));
        lorelItems.add(new ProductItem(380,"For Colored Hair  Hair Spa Color Pure Conditioner (200 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Loreal Professional/For Colored Hair  Hair Spa Color Pure Conditioner (200 ml).png"));

        ArrayList<ProductItem> matrixItems = new ArrayList<>();
        matrixItems.add(new ProductItem(170,"Smoothing Conditioner (98 g)",0,"http:/" + "/" + "18.188.149.40:5000/images/Matrix/Smoothing Conditioner (98 g).png"));
        matrixItems.add(new ProductItem(205,"Smoothing Shampoo (200 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Matrix/Smoothing Shampoo (200 ml).png"));
        matrixItems.add(new ProductItem(315,"Hydrating Conditioner (196 g)",0,"http:/" + "/" + "18.188.149.40:5000/images/Matrix/Hydrating Conditioner (196 g).png"));
        matrixItems.add(new ProductItem(205,"Color Protecting Shampoo (200 ml)",0,"http:/" + "/" + "18.188.149.40:5000/images/Matrix/Color Protecting Shampoo (200 ml).png"));

        ArrayList<ProductItem> cherlysItems = new ArrayList<>();
        cherlysItems.add(new ProductItem(265,"ClariWash Face Wash For Oily Skin (50 g)",0,"http:/" + "/" + "18.188.149.40:5000/images/Cheryls/ClariWash Face Wash For Oily Skin (50 g).png"));
        cherlysItems.add(new ProductItem(265,"Derma Cleanse For Acne Prone Skin (80 g)",0,"http:/" + "/" + "18.188.149.40:5000/images/Cheryls/Derma Cleanse For Acne Prone Skin (80 g).png"));
        cherlysItems.add(new ProductItem(645,"Dermalite Fairness Lotion Instant Brightening (50 g)",0,"http:/" + "/" + "18.188.149.40:5000/images/Cheryls/Dermalite Fairness Lotion Instant Brightening (50 g).png"));
        cherlysItems.add(new ProductItem(300,"HeelPeel Cracked Heel Eliminator For cracked Heels (40 g)",0,"http:/" + "/" + "18.188.149.40:5000/images/Cheryls/HeelPeel Cracked Heel Eliminator For cracked Heels (40 g).png"));

        brandItems = new ArrayList<>();
        brandItems.add(new BrandItem("Lotus Professional",productItems));
        brandItems.add(new BrandItem("Skeyndor",skendorItems));
        brandItems.add(new BrandItem("Loreal Professional",lorelItems));
        brandItems.add(new BrandItem("Matrix",matrixItems));
        brandItems.add(new BrandItem("Cherlys",cherlysItems));

    }

    public void insertCartItem(CartItem cartItem){
        repository.insert(cartItem);
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