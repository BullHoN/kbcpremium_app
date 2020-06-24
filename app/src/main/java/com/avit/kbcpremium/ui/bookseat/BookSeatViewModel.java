package com.avit.kbcpremium.ui.bookseat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.avit.kbcpremium.db.AppointmentItemRepository;
import com.avit.kbcpremium.ui.appointment.AppointmentItem;

public class BookSeatViewModel extends AndroidViewModel {

    private AppointmentItemRepository repository;

    public BookSeatViewModel(@NonNull Application application) {
        super(application);
        repository = new AppointmentItemRepository(application);
    }

    public void insert(AppointmentItem appointmentItem){
        repository.insert(appointmentItem);
    }

}
