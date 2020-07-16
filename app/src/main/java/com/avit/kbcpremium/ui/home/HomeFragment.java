package com.avit.kbcpremium.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.avit.kbcpremium.R;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.dialogs.CartBottomSheetDialog;
import com.avit.kbcpremium.ui.appointment.AppointmentFragment;
import com.avit.kbcpremium.ui.booking.BookingDetailsFragment;
import com.avit.kbcpremium.ui.bookings.BookingsFragment;
import com.avit.kbcpremium.ui.cart.CartFragment;
import com.avit.kbcpremium.ui.cart.CartItem;
import com.avit.kbcpremium.ui.products.BrandItem;
import com.avit.kbcpremium.ui.products.ProductItem;
import com.avit.kbcpremium.ui.products.ProductsFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private LinearLayout booking1,booking2,categoriesView;
    private ViewGroup viewGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewGroup = container;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);
        String user_name = sharedPreferences.getString(SharedPrefNames.USER_NAME,"");
        TextView nameView =  root.findViewById(R.id.username);
        nameView.setText(user_name);

        final Fragment bookingsFragment = new BookingsFragment();
        root.findViewById(R.id.viewall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment,bookingsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        final Fragment cartFragment = new CartFragment();
        getActivity().findViewById(R.id.openCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .replace(R.id.nav_host_fragment
                                ,cartFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        booking1 = root.findViewById(R.id.booking_row1);
        booking2 = root.findViewById(R.id.booking_row2);

        ArrayList<BookingItem> booking1Items = homeViewModel.getBooking1();
        ArrayList<BookingItem> booking2Items = homeViewModel.getBooking2();
        addBookingItems(booking1,booking1Items,container);
        addBookingItems(booking2,booking2Items,container);

        // assign all the products
        categoriesView = root.findViewById(R.id.categories);
        setBrandCategories();

        return root;
    }

    private void addBookingItems(LinearLayout linearLayout, ArrayList<BookingItem> bookingItems,ViewGroup container){
        for(int i=0;i<bookingItems.size();i++){
            View itemView = getLayoutInflater().inflate(R.layout.booking_item,container,false);
            ImageView imageView =  itemView.findViewById(R.id.booking_icon);
            imageView.setImageResource(bookingItems.get(i).getResourceId());

            TextView textView =  itemView.findViewById(R.id.booking_heading);
            textView.setText(bookingItems.get(i).getTitle());

            final Bundle bundle = new Bundle();
            bundle.putString("name",bookingItems.get(i).getTitle());
            bundle.putStringArray("items",bookingItems.get(i).getItems());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment bookingDetailsFragment = new BookingDetailsFragment();
                    bookingDetailsFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment
                                    ,bookingDetailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            linearLayout.addView(itemView);
        }
    }

    private void addToCart(CartItem cartItem){
        homeViewModel.insertCartItem(cartItem);
    }

    private void showAlertBox(final CartItem cartItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Add This Item To Cart ?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addToCart(cartItem);

                        Bundle bundle = new Bundle();
                        bundle.putString("item_name",cartItem.getItemName());

                        CartBottomSheetDialog cartBottomSheetDialog = new CartBottomSheetDialog();
                        cartBottomSheetDialog.setArguments(bundle);
                        cartBottomSheetDialog.show(getFragmentManager(),"addToCart");
                    }
                });

        builder.show();

    }

    private void setBrandCategories(){
        ArrayList<BrandItem> brandItems = homeViewModel.getProductItems();

        for(int i=0;i<brandItems.size();i++){
            View brandView = getLayoutInflater().inflate(R.layout.categories_item,viewGroup,false);
            TextView brandName = brandView.findViewById(R.id.brandName);
            brandName.setText(brandItems.get(i).getBrandName());

            LinearLayout product_items = brandView.findViewById(R.id.brand_items);
            ArrayList<ProductItem> productItems = brandItems.get(i).getItems();

            for(int j=0;j<productItems.size();j++){
                final ProductItem curr = productItems.get(j);
                View productView = getLayoutInflater().inflate(R.layout.product_item,viewGroup,false);

                productView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertBox(new CartItem(curr.getName(),curr.getPrice(),curr.getImageUrl()));
                    }
                });

                ImageView imageView = productView.findViewById(R.id.image);
                Glide.with(getContext()).load(curr.getImageUrl()).into(imageView);

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
                if(curr.getDiscount() !=0) {
                    float price = curr.getPrice() - curr.getPrice()*(float)(curr.getDiscount()/100.0);

                    productPriceView.setPaintFlags(productPriceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    productPrice2View.setText("Price: ₹" + Math.round(price));
                    productPrice2View.setVisibility(View.VISIBLE);
                }

                product_items.addView(productView);
            }

            TextView openProduct = brandView.findViewById(R.id.openProduct);
            final Fragment productsFragment = new ProductsFragment();

            Bundle bundle = new Bundle();
            bundle.putString("brand_name",brandItems.get(i).getBrandName());
            productsFragment.setArguments(bundle);

            openProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                            .replace(R.id.nav_host_fragment
                                    ,productsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            categoriesView.addView(brandView);
        }
    }

}
