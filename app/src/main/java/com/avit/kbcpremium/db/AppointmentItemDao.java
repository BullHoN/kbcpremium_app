package com.avit.kbcpremium.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.avit.kbcpremium.ui.appointment.AppointmentItem;

import java.util.List;

@Dao
public interface AppointmentItemDao {

    @Insert
    void insert(AppointmentItem appointmentItem);

    @Delete
    void delete(AppointmentItem appointmentItem);

    @Update
    void update(AppointmentItem appointmentItem);

    @Query("SELECT * FROM appointment_table")
    LiveData<List<AppointmentItem>> getAllItems();

    @Query("UPDATE appointment_table SET status = :status WHERE bookingId = :orderId")
    int UpdateOrder(String orderId , int status);


}
