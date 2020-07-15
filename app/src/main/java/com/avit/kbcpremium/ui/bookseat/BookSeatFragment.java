package com.avit.kbcpremium.ui.bookseat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.avit.kbcpremium.NetworkApi;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.RetrofitClient;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.ui.appointment.AppointmentFragment;
import com.avit.kbcpremium.ui.appointment.AppointmentItem;
import com.avit.kbcpremium.ui.booking.SelectedItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookSeatFragment  extends Fragment {

    private ArrayList<SelectedItem> selectedItems;
    private String TAG = "BookSeat";
    private ArrayList<Integer> timeIds;
    private String selectedDate,selectedTime,thh,tzone,tydate,bookingCat,bookingId;
    private View prevView,fView;
    private LinearLayout datesView;
    private ViewGroup viewGroup;
    private TextView dateView,totalView;
    private Button prevButton;
    private LinearLayout billItemsView;
    private SharedPreferences sharedPreferences;
    private int total;
    private BookSeatViewModel viewModel;
    private LinearLayout timingView,marrigeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookseat,container,false);
        fView = root;
        viewGroup = container;
        viewModel = ViewModelProviders.of(this).get(BookSeatViewModel.class);

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.DB_NAME, Context.MODE_PRIVATE);
        timeIds = new ArrayList<>();
        populateTimeIdsList();

        datesView = root.findViewById(R.id.dates);
        selectedItems = getArguments().getParcelableArrayList("bookingItems");
        bookingCat = getArguments().getString("booking_cat");
        billItemsView = root.findViewById(R.id.bill_items);
        totalView = root.findViewById(R.id.total);
        timingView = root.findViewById(R.id.timing);
        marrigeView = root.findViewById(R.id.marrige);

        if(bookingCat.contains("Bridal")){
            timingView.setVisibility(View.GONE);
            selectedTime = "None";
        }else {
            marrigeView.setVisibility(View.GONE);
        }

        setUpBillItems();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E,dd MMM yyyy,hh:mm:ss,a,MM");
        String currentDateandTime = simpleDateFormat.format(new Date());

        String arr[] = currentDateandTime.split(",");
        final String date = arr[1];
        tydate = date;
        dateView = root.findViewById(R.id.date);
        dateView.setText(date);

        int tdate = Integer.parseInt(date.split(" ")[0]);
        String tday = arr[0];
        String tmonth = arr[4];
        String tyear = arr[1].split(" ")[2];
        insertDates(tday,tdate,Integer.parseInt(tmonth),Integer.parseInt(tyear));

        // set time
        thh = arr[2].split(":")[0];
        tzone = arr[3];
        setUpTime();

        root.findViewById(R.id.booking_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTime == null){
                    Toast.makeText(getContext(),"Please Select Booking Time",Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                showAlertBox();
            }
        });

        return root;
    }

    private void showAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Are You Sure You Want To Book Seat ?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendNotification();
                    }
                });

        builder.show();

    }

    private void sendNotification(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        String name = sharedPreferences.getString(SharedPrefNames.USER_NAME,"");
        String address = sharedPreferences.getString(SharedPrefNames.ADDRESS,"");
        String nearBy = sharedPreferences.getString(SharedPrefNames.NEAR_ADDRESS,"");
        String phoneNo = sharedPreferences.getString(SharedPrefNames.PH_NUMBER,"");
        String fcm_id = sharedPreferences.getString(SharedPrefNames.SOCKET_ID,"");
        bookingId = generateOrderId(18);

        BookingNotificationPostData data = new BookingNotificationPostData(name,phoneNo,fcm_id
                ,total,selectedItems,address,nearBy,bookingId,bookingCat,selectedDate,selectedTime);

        Call<BookingNotificationResponseData> call = networkApi.sendBookingNotifcation(data);

        call.enqueue(new Callback<BookingNotificationResponseData>() {
            @Override
            public void onResponse(Call<BookingNotificationResponseData> call, Response<BookingNotificationResponseData> response) {
                BookingNotificationResponseData responseData = response.body();

                if(responseData.isStatus()){
                    Toast.makeText(getContext(),"Booking Has Been Registered",Toast.LENGTH_SHORT)
                            .show();
                    saveToDatabse();
                }else {
                    Toast.makeText(getContext(),"Sorry But Seat is Unavaible so change Time",Toast.LENGTH_LONG)
                            .show();
                }
                
            }

            @Override
            public void onFailure(Call<BookingNotificationResponseData> call, Throwable t) {

            }
        });

    }

    private void saveToDatabse(){
        StringBuilder stringBuilder = new StringBuilder();
        for(SelectedItem selectedItem : selectedItems){
            stringBuilder.append(selectedItem.getName() + "=");
        }

        AppointmentItem appointmentItem = new AppointmentItem(stringBuilder.toString(),selectedDate
                ,selectedTime,bookingId,0,total);

        viewModel.insert(appointmentItem);

        Fragment appointFragment = new AppointmentFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment
                        ,appointFragment)
                .addToBackStack(null)
                .commit();

    }

    private String generateOrderId(int n){
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private void setUpBillItems(){
        total = 0;
        for(SelectedItem curr : selectedItems) {
            total += Integer.parseInt(curr.getPrice());
            View view = getLayoutInflater().inflate(R.layout.appoint_item, viewGroup, false);

            TextView nameView = view.findViewById(R.id.name);
            nameView.setText(curr.getName());

            TextView priceView = view.findViewById(R.id.price);
            priceView.setText("₹" + curr.getPrice());

            billItemsView.addView(view);
        }

        totalView.setText("₹" + total);

    }

    private void setUpTime(){
        View root = fView;
        prevButton = null;
        for (int i=0;i<timeIds.size();i++){
            final Button button = root.findViewById(timeIds.get(i));
            int temp = timeIds.get(i);
            String temp2 = button.getText().toString().split(":")[0];

            if(tydate.contains(selectedDate)) {
                if ((temp == R.id.t9 || temp == R.id.t10 || temp == R.id.t11 || temp == R.id.t12)) {
                    if (tzone.contains("PM")) {
                        button.setClickable(false);
                        button.setBackgroundColor(getResources().getColor(R.color.text_color));
                    } else {
                        if (Integer.parseInt(temp2) <= Integer.parseInt(thh)) {
                            button.setClickable(false);
                            button.setBackgroundColor(getResources().getColor(R.color.text_color));
                        } else {
                            button.setClickable(true);
                            button.setBackgroundColor(getResources().getColor(R.color.buttonActive));
                        }
                    }
                } else if (tzone.contains("PM") && !thh.contains("12")) {
                    if (Integer.parseInt(temp2) <= Integer.parseInt(thh) && selectedDate.contains(tydate)) {
                        button.setClickable(false);
                        button.setBackgroundColor(getResources().getColor(R.color.text_color));
                    } else {
                        button.setClickable(true);
                        button.setBackgroundColor(getResources().getColor(R.color.buttonActive));
                    }
                }
            }else {
                button.setClickable(true);
                button.setBackgroundColor(getResources().getColor(R.color.buttonActive));
            }


            if(button.isClickable()){
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        button.setBackgroundColor(getResources().getColor(R.color.categoryHeading));
                        if(prevButton != null){
                            prevButton.setBackgroundColor(getResources().getColor(R.color.buttonActive));
                        }


                        int temp = Integer.parseInt(button.getText().toString().split(":")[0]);

                        if(temp == 9 || temp == 10 || temp == 11) {
                            selectedTime = button.getText().toString() + " " + "AM";
                        }else {
                            selectedTime = button.getText().toString() + " " + "PM";
                        }

                        prevButton = button;
                    }
                });
            }
        }
    }



    private void insertDates(String tday,int tdate,int tmonth,int tyear){

        for(int i=0;i<15;i++){
            View daysView = getLayoutInflater().inflate(R.layout.date_item,viewGroup,false);


            final TextView daysDateView = daysView.findViewById(R.id.date);
            if(i == 0){
                prevView = daysDateView;
                daysDateView.setBackgroundColor(getResources().getColor(R.color.white));
                selectedDate = tdate + " " + getMonth(tmonth) + " " + tyear;
            }
            daysDateView.setText(String.valueOf(tdate));
            if(tdate == getMaxDaysOfMonth(tmonth)){
                tdate = 1;
                if(tmonth == 12){
                    tmonth = 1;
                    tyear++;
                }else {
                    tmonth++;
                }
            }else {
                tdate++;
            }

            TextView daysDayView = daysView.findViewById(R.id.day);
            daysDayView.setText(tday);
            tday = nextDay(tday.toLowerCase());

            final int temp1 = tmonth,temp2 = tyear;
            daysView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prevView.setBackgroundColor(getResources().getColor(R.color.trans));
                    daysDateView.setBackgroundColor(getResources().getColor(R.color.white));
                    prevView = daysDateView;

                    selectedDate = daysDateView.getText().toString() + " " + getMonth(temp1)
                            + " " + temp2;
                    dateView.setText(selectedDate);
                    setUpTime();
                }
            });

            datesView.addView(daysView);
        }
    }

    private int getMaxDaysOfMonth(int month){
        if(month == 2){
            return 28;
        }else {
            if(month%2 == 0){
                return 30;
            }else {
                return 31;
            }
        }
    }

    private String getMonth(int month){
        String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        return months[month-1];
    }

    private String nextDay(String currentDay){
        switch (currentDay){
            case "mon":
                return "Tue";
            case "tue":
                return "Wed";
            case "wed":
                return "Thu";
            case "thu":
                return "Fri";
            case "fri":
                return "Sat";
            case "sat":
                return "Sun";
            case "sun":
                return "Mon";
        }

        return  currentDay;
    }

    private void populateTimeIdsList(){
        timeIds.add(R.id.t1);
        timeIds.add(R.id.t2);
        timeIds.add(R.id.t3);
        timeIds.add(R.id.t4);
        timeIds.add(R.id.t5);
        timeIds.add(R.id.t6);
        timeIds.add(R.id.t7);
        timeIds.add(R.id.t9);
        timeIds.add(R.id.t10);
        timeIds.add(R.id.t11);
        timeIds.add(R.id.t12);
    }

}
