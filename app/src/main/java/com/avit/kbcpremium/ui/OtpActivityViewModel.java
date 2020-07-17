package com.avit.kbcpremium.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.avit.kbcpremium.db.OrderItemRepository;
import com.avit.kbcpremium.ui.orders.OrderItem;

import java.util.ArrayList;

public class OtpActivityViewModel extends AndroidViewModel {

    OrderItemRepository orderItemRepository;

    public OtpActivityViewModel(@NonNull Application application) {
        super(application);
        orderItemRepository = new OrderItemRepository(application);
    }

    public void addOrderItems(ArrayList<OrderItem> orderItems){
        for(OrderItem orderItem : orderItems){
            orderItemRepository.insert(orderItem);
        }
    }

}
