package com.avit.kbcpremium.ui.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avit.kbcpremium.db.CartItemRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private CartItemRepository repository;
    private LiveData<List<CartItem>> allItems;

    public CartViewModel(@NonNull Application application) {
        super(application);
        repository = new CartItemRepository(application);
        allItems = repository.getAllItems();
    }

    public void insert(CartItem cartItem){
        repository.insert(cartItem);
    }

    public void delete(CartItem cartItem){
        repository.delete(cartItem);
    }

    public void update(CartItem cartItem){
        repository.update(cartItem);
    }

    public LiveData<List<CartItem>> getAllItems(){
        return allItems;
    }

}
