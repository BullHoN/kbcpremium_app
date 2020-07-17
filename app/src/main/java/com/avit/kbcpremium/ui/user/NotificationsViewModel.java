package com.avit.kbcpremium.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.kbcpremium.db.AppointmentItemRepository;
import com.avit.kbcpremium.db.CartItemRepository;
import com.avit.kbcpremium.db.OrderItemRepository;

public class NotificationsViewModel extends AndroidViewModel {

    CartItemRepository cartItemRepository;
    OrderItemRepository orderItemRepository;
    AppointmentItemRepository appointmentItemRepository;



    public NotificationsViewModel(@NonNull Application application) {
        super(application);

        cartItemRepository = new CartItemRepository(application);
        orderItemRepository = new OrderItemRepository(application);
        appointmentItemRepository = new AppointmentItemRepository(application);

    }

    public void clearAll(){
        cartItemRepository.deleteAll();
        orderItemRepository.deleteAll();
        appointmentItemRepository.deleteAll();
    }

}