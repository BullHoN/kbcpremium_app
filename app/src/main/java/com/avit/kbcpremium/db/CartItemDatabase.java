package com.avit.kbcpremium.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.FtsOptions;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.avit.kbcpremium.ui.appointment.AppointmentItem;
import com.avit.kbcpremium.ui.cart.CartItem;
import com.avit.kbcpremium.ui.orders.OrderItem;


@Database(entities = {CartItem.class, OrderItem.class, AppointmentItem.class},version = 1)
public abstract class CartItemDatabase extends RoomDatabase {

    private static CartItemDatabase instance;

    public abstract  CartItemDao cartItemDao();
    public abstract OrderItemDao orderItemDao();
    public abstract AppointmentItemDao appointmentItemDao();

    public static synchronized CartItemDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext()
                    ,CartItemDatabase.class,"KBC_Premium_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

//    private static RoomDatabase.Callback callback =
//            new RoomDatabase.Callback(){
//                @Override
//                public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                    super.onCreate(db);
//                    new PopulatedbAsyncTask(instance).execute();
//                }
//            };
//
//    private static class PopulatedbAsyncTask extends AsyncTask<Void,Void,Void> {
//
//        private AppointmentItemDao cartItemDao;
//
//        private PopulatedbAsyncTask(CartItemDatabase db){
//
//            cartItemDao = db.appointmentItemDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            AppointmentItem item = new AppointmentItem(1,"asfasfasf","asfafasf","asasgasg","asgasg",1);
//
//            cartItemDao.insert(item);
//
//            return null;
//        }
//    }

}
