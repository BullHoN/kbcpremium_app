package com.avit.kbcpremium.ui.appointment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avit.kbcpremium.db.AppointmentItemRepository;

import java.util.List;

public class AppointmentFragmentViewModel extends AndroidViewModel {

    private AppointmentItemRepository repository;
    private LiveData<List<AppointmentItem>> allItems;

    public AppointmentFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AppointmentItemRepository(application);
        allItems = repository.getAllItems();
    }

    public LiveData<List<AppointmentItem>> getAllItems() {
        return allItems;
    }
}
