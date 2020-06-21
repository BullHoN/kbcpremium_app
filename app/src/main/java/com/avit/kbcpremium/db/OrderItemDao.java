package com.avit.kbcpremium.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Update;

import com.avit.kbcpremium.ui.orders.OrderItem;

import java.util.List;


@Dao
public interface OrderItemDao {

    @Insert
    void insert(OrderItem ordersItem);

    @Update
    void update(OrderItem ordersItem);

    @Delete
    void delete(OrderItem orderItem);

    @Query("SELECT * FROM order_table ORDER BY _id DESC")
    LiveData<List<OrderItem>> getAllItems();

    @Query("UPDATE order_table SET status = :status WHERE order_id = :orderId")
    int UpdateOrder(String orderId , int status);

}
