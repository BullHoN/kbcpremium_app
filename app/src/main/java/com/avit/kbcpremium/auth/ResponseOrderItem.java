package com.avit.kbcpremium.auth;

import com.avit.kbcpremium.ui.orders.OrderItem;

import java.util.ArrayList;

public class ResponseOrderItem {

    private int status;
    private int total;
    private String date;
    private ArrayList<String> items;
    private String orderId;

    public ResponseOrderItem(int status, int total, String date, ArrayList<String> items, String orderId) {
        this.status = status;
        this.total = total;
        this.date = date;
        this.items = items;
        this.orderId = orderId;
    }

    public static ArrayList<OrderItem> convertToOrderItem(ArrayList<ResponseOrderItem> responseOrderItems,String address){
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for(int i=0;i<responseOrderItems.size();i++){
            ResponseOrderItem curr = responseOrderItems.get(i);

            orderItems.add(new OrderItem(curr.getStatus(),curr.getTotal(),curr.getItems().size(),
                    address,curr.getDate(),getStringFromArray(curr.getItems()),curr.getOrderId()));
        }

        return orderItems;
    }

    private static String getStringFromArray(ArrayList<String> items){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<items.size();i++){
            String tempItem = items.get(i).split("'")[1];
            stringBuilder.append(tempItem + ",");
        }

        return stringBuilder.toString();
    }

    public int getStatus() {
        return status;
    }

    public int getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public String getOrderId() {
        return orderId;
    }
}
