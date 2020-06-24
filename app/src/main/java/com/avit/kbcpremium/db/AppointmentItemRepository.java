package com.avit.kbcpremium.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.avit.kbcpremium.notification.NotificationReceiveData;
import com.avit.kbcpremium.ui.appointment.AppointmentItem;

import java.util.List;

public class AppointmentItemRepository {

    private AppointmentItemDao appointmentItemDao;
    private LiveData<List<AppointmentItem>> allItems;

    public AppointmentItemRepository(Application application){
        CartItemDatabase database = CartItemDatabase.getInstance(application);
        appointmentItemDao = database.appointmentItemDao();

        allItems = appointmentItemDao.getAllItems();
    }

    public LiveData<List<AppointmentItem>> getAllItems() {
        return allItems;
    }

    public void insert(AppointmentItem appointmentItem){
        new InsertAsyncTask(appointmentItemDao).execute(appointmentItem);
    }

    public void update(AppointmentItem appointmentItem){
        new UpdateAsyncTask(appointmentItemDao).execute(appointmentItem);
    }

    public void updateData(NotificationReceiveData data){
        new UpdateOrderAsyncTask(appointmentItemDao).execute(data);
    }

    public void delete(AppointmentItem appointmentItem){
        new DeleteOrderAsyncTask(appointmentItemDao).execute(appointmentItem);
    }

    private static class InsertAsyncTask extends AsyncTask<AppointmentItem,Void,Void>{

        private AppointmentItemDao dao;

        public InsertAsyncTask(AppointmentItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(AppointmentItem... appointmentItems) {
            dao.insert(appointmentItems[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<AppointmentItem,Void,Void>{

        private AppointmentItemDao dao;

        public UpdateAsyncTask(AppointmentItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(AppointmentItem... appointmentItems) {
            dao.update(appointmentItems[0]);
            return null;
        }
    }

    private static class UpdateOrderAsyncTask extends AsyncTask<NotificationReceiveData,Void,Void>{

        private AppointmentItemDao dao;

        public UpdateOrderAsyncTask(AppointmentItemDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NotificationReceiveData... notificationReceiveData) {
            dao.UpdateOrder(notificationReceiveData[0].getOrderId(),notificationReceiveData[0].getStatus());
            return null;
        }
    }

    private static class DeleteOrderAsyncTask extends AsyncTask<AppointmentItem,Void,Void>{

        private AppointmentItemDao dao;

        public DeleteOrderAsyncTask(AppointmentItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(AppointmentItem... appointmentItems) {
            dao.delete(appointmentItems[0]);
            return null;
        }
    }

}
