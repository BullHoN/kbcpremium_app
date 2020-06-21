package com.avit.kbcpremium;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.avit.kbcpremium.db.CartItemRepository;
import com.avit.kbcpremium.db.OrderItemRepository;
import com.avit.kbcpremium.ui.orders.OrderItem;

public class HomeActivityViewModel extends AndroidViewModel {

    private OrderItemRepository repository;
    private CartItemRepository cartItemRepository;

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderItemRepository(application);
        cartItemRepository = new CartItemRepository(application);
    }

    public void clearCart(){
        cartItemRepository.deleteAll();
    }

    public void delete(OrderItem orderItem){
        repository.delete(orderItem);
    }

    public void insert(OrderItem ordersItem){
        repository.insert(ordersItem);
    }
}
