package com.avit.kbcpremium.ui.appointment;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "appointment_table")
public class AppointmentItem {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "items")
    private String items;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "bookingId")
    private String bookingId;

    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "total")
    private int total;

    public AppointmentItem(String items, String date, String time
            , String bookingId, int status,int total) {
        this.items = items;
        this.date = date;
        this.time = time;
        this.bookingId = bookingId;
        this.status = status;
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
