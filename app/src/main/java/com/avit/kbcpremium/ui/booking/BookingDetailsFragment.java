package com.avit.kbcpremium.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.avit.kbcpremium.NetworkApi;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.RetrofitClient;
import com.avit.kbcpremium.ui.bookseat.BookSeatFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingDetailsFragment extends Fragment {

    private String TAG ="BookingItem";
    private LinearLayout itemsView;
    private ViewGroup viewGroup;
    private ProgressBar progressBar;
    private LinearLayout bookingBoxView;
    private ArrayList<SelectedItem> selectedItems;
    private TextView bookingPriceView;
    private TextView bookingCountView;
    private int totalPrice = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookingdetails,container,false);
        viewGroup = container;

        root.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        itemsView = root.findViewById(R.id.items);
        progressBar = root.findViewById(R.id.progress);
        bookingBoxView = root.findViewById(R.id.bookingBox);
        bookingCountView = root.findViewById(R.id.bookingServices);
        bookingPriceView = root.findViewById(R.id.bookingAmount);

        TextView titleView = root.findViewById(R.id.title);
        final String name = getArguments().getString("name");
        titleView.setText(name);

        root.findViewById(R.id.bookbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("booking_cat",name);
                bundle.putParcelableArrayList("bookingItems",selectedItems);

                Fragment bookSeatFragment = new BookSeatFragment();
                bookSeatFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment
                                ,bookSeatFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        selectedItems = new ArrayList<>();

        String items[] = getArguments().getStringArray("items");
        for(int i=0;i<items.length;i++) {
            getBookingItem(items[i]);
        }

        return root;
    }

    private void addItemsToLayout(final BookingItems bookingItems){
        ArrayList<BookingItem> bookingItems1 = bookingItems.getItems();
        for(int i=0;i<bookingItems1.size();i++){
            final BookingItem curr = bookingItems1.get(i);
            final int[] selected = {0};
            View itemView = getLayoutInflater().inflate(R.layout.bookingselect_item,viewGroup,false);

            CheckBox checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setText(curr.getName());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SelectedItem selectedItem = new SelectedItem(curr.getName(),bookingItems.getOptions().get(selected[0]));
                    String temp = curr.getPrices().get(selected[0]);
                    selectedItem.setPrice(temp);

                    if(constainsSelected(selectedItem)){
                        deleteSelected(selectedItem);
                        totalPrice -= Integer.parseInt(temp);
                    }else {
                        selectedItems.add(selectedItem);
                        totalPrice += Integer.parseInt(temp);
                    }

                    if(selectedItems.size() > 0){
                        updateTheBookingView();
                        bookingBoxView.setVisibility(View.VISIBLE);
                    }else {
                        bookingBoxView.setVisibility(View.GONE);
                    }
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item,bookingItems.getOptions());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            final Spinner spinner = itemView.findViewById(R.id.spinner);
            spinner.setAdapter(adapter);

            final TextView priceView = itemView.findViewById(R.id.price);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    priceView.setText("₹" + curr.getPrices().get(position));

                    SelectedItem selectedItem = new SelectedItem(curr.getName(),bookingItems.getOptions().get(selected[0]));
                    String temp = curr.getPrices().get(selected[0]);
                    if(constainsSelected(selectedItem)) {
                        totalPrice -= Integer.parseInt(temp);
                    }

                    selected[0] = position;

                    if(constainsSelected(selectedItem)) {
                        selectedItem.setOption(bookingItems.getOptions().get(selected[0]));
                        temp = curr.getPrices().get(selected[0]);
                        totalPrice += Integer.parseInt(temp);
                        selectedItem.setPrice(temp);

                        updateSelected(selectedItem);
                    }

                    if(bookingItems.getOptions().size() == 1){
                        spinner.setVisibility(View.INVISIBLE);
                    }

                    updateTheBookingView();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            itemsView.addView(itemView);
        }
    }

    private void updateSelected(SelectedItem selectedItem){
        for(SelectedItem curr : selectedItems){
            if(curr.getName() == selectedItem.getName()){
                curr.setOption(selectedItem.getOption());
                curr.setPrice(selectedItem.getPrice());
            }
        }
    }

    private void updateTheBookingView(){
        bookingPriceView.setText("₹" + totalPrice);
        bookingCountView.setText(selectedItems.size() + " Services");
    }

    private void deleteSelected(SelectedItem selectedItem){
        for (SelectedItem curr : selectedItems){
            if(curr.getName() == selectedItem.getName()){
                selectedItems.remove(curr);
                return;
            }
        }
    }

    private boolean constainsSelected(SelectedItem selectedItem){
        for(SelectedItem curr : selectedItems){
            if(curr.getName() == selectedItem.getName()){
                return true;
            }
        }
        return false;
    }

    private void getBookingItem(String bookingCategory){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        Call<BookingItems> call = networkApi.getBookingItemsOf(bookingCategory);

        call.enqueue(new Callback<BookingItems>() {
            @Override
            public void onResponse(Call<BookingItems> call, Response<BookingItems> response) {
                BookingItems bookingItems = response.body();
                progressBar.setVisibility(View.GONE);
                addItemsToLayout(bookingItems);
            }

            @Override
            public void onFailure(Call<BookingItems> call, Throwable t) {
                Toast.makeText(getContext(),"Check Your Internet",Toast.LENGTH_SHORT)
                        .show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}
