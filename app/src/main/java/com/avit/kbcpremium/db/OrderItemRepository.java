package com.avit.kbcpremium.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.avit.kbcpremium.notification.NotificationReceiveData;
import com.avit.kbcpremium.ui.orders.OrderItem;

import java.util.List;

public class OrderItemRepository {

    private OrderItemDao orderItemDao;
    private LiveData<List<OrderItem>> allItems;

    public OrderItemRepository(Application application){
        CartItemDatabase database = CartItemDatabase.getInstance(application);
        orderItemDao = database.orderItemDao();

        allItems = orderItemDao.getAllItems();
    }

    public LiveData<List<OrderItem>> getAllItems() {
        return allItems;
    }

    public void insert(OrderItem ordersItem){
        new InsertAsyncTask(orderItemDao).execute(ordersItem);
    }

    public void update(OrderItem ordersItem){
        new UpdateAsyncTask(orderItemDao).execute(ordersItem);
    }

    public void updateData(NotificationReceiveData data){
        new UpdateOrderAsyncTask(orderItemDao).execute(data);
    }

    public void delete(OrderItem orderItem){
        new DeleteOrderAsyncTask(orderItemDao).execute(orderItem);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(orderItemDao).execute();
    }

    private static class DeleteOrderAsyncTask extends AsyncTask<OrderItem,Void,Void>{

        private OrderItemDao dao;

        public DeleteOrderAsyncTask(OrderItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(OrderItem... ordersItems) {
            dao.delete(ordersItems[0]);
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<OrderItem,Void,Void>{

        private OrderItemDao dao;

        public InsertAsyncTask(OrderItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(OrderItem... ordersItems) {
            dao.insert(ordersItems[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<OrderItem,Void,Void> {

        private OrderItemDao dao;

        public UpdateAsyncTask(OrderItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(OrderItem... ordersItems) {
            dao.update(ordersItems[0]);
            return null;
        }
    }

    private static class UpdateOrderAsyncTask extends AsyncTask<NotificationReceiveData,Void,Void>{

        private OrderItemDao dao;

        public UpdateOrderAsyncTask(OrderItemDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NotificationReceiveData... notificationReceiveData) {
            dao.UpdateOrder(notificationReceiveData[0].getOrderId()
                    ,notificationReceiveData[0].getStatus(),notificationReceiveData[0].getMessage());
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{

        private OrderItemDao dao;

        public DeleteAllAsyncTask(OrderItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

}
