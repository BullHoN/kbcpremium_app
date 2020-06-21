package com.avit.kbcpremium.ui.orders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avit.kbcpremium.db.OrderItemRepository;

import java.util.List;

public class OrdersFragmentViewModel extends AndroidViewModel {

    private OrderItemRepository repository;
    private LiveData<List<OrderItem>> allItems;


    public OrdersFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderItemRepository(application);
        allItems = repository.getAllItems();
    }

    public void insert(OrderItem ordersItem){
        repository.insert(ordersItem);
    }

    public void update(OrderItem ordersItem){
        repository.update(ordersItem);
    }

    public LiveData<List<OrderItem>> getAllItems() {
        return allItems;
    }

}
