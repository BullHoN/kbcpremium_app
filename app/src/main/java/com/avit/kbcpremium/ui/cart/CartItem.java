package com.avit.kbcpremium.ui.cart;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "itemName")
    private String mItemName;

    @ColumnInfo(name = "price")
    private int mPrice;

    @ColumnInfo(name = "quantity")
    private int mQuantity=1;

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    public CartItem(String mItemName, int mPrice,String imageUrl) {
        this.mItemName = mItemName;
        this.mPrice = mPrice;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void quantity(int quantity){
        this.mQuantity = quantity;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }


    public int getTotal(){
       return mQuantity*mPrice;
    }

    public void incrementedQuantity(){
        ++mQuantity;
    }

    public void decrementedQuantity(){
        mQuantity = mQuantity > 1 ? mQuantity-1 : mQuantity;
    }


    public int getQuantity() {
        return mQuantity;
    }

    public String getItemName() {
        return mItemName;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "mItemName='" + mItemName + '\'' +
                ", mPrice=" + mPrice +
                ", mQuantity=" + mQuantity +
                '}';
    }
}
