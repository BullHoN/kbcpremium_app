package com.avit.kbcpremium.ui.orders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avit.kbcpremium.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderFragmentCustomAdapter extends ArrayAdapter<OrderItem> {

    List<OrderItem> items;

    public OrderFragmentCustomAdapter(@NonNull Context context, int resource, @NonNull List<OrderItem> objects) {
        super(context, resource, objects);
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ordersListView = convertView;
        if (ordersListView == null){
            ordersListView = LayoutInflater.from(getContext())
                    .inflate(R.layout.order_item,parent,false);
        }

        OrderItem currentItem = items.get(position);

        TextView addressView = ordersListView.findViewById(R.id.address);
        addressView.setText(currentItem.getAddress());

        TextView itemsView = ordersListView.findViewById(R.id.items);
        itemsView.setText(currentItem.getNoItems() + " items");

        TextView detailsView = ordersListView.findViewById(R.id.items_details);
        detailsView.setText(currentItem.getItems());

        TextView totalView = ordersListView.findViewById(R.id.total);
        totalView.setText("₹" + currentItem.getTotal());


        ImageView imageView = ordersListView.findViewById(R.id.image);
        TextView imageTextView = ordersListView.findViewById(R.id.image_text);
        TextView deliveryTimeView = ordersListView.findViewById(R.id.delivery_times);

        deliveryTimeView.setText("Will Be Delivered Soon");

        Log.i("Order",currentItem.getStatus()+"");

        switch (currentItem.getStatus()){
            case 0:
                imageView.setImageResource(R.drawable.ic_pending);
                imageTextView.setText("We Got Your Order");
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_out_dilevery);
                imageTextView.setText("Order Out For Delivery");
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_dilevered);
                Date currentTime = Calendar.getInstance().getTime();
                String time = currentTime.toString().split(" G")[0];
                deliveryTimeView.setText(time);
                imageTextView.setText("Your Order Has Been Delivered");
                break;
            case -1:
                imageView.setImageResource(R.drawable.ic_cancled);
                imageTextView.setText("Order is cancled!!");
                break;
            default:
                imageView.setImageResource(R.drawable.ic_pending);
                imageTextView.setText("Order in process");
        }

        return ordersListView;
    }
}
