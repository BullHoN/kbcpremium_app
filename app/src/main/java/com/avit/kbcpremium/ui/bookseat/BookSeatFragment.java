package com.avit.kbcpremium.ui.bookseat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.avit.kbcpremium.R;
import com.avit.kbcpremium.ui.booking.SelectedItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookSeatFragment  extends Fragment {

    private ArrayList<SelectedItem> selectedItems;
    private String TAG = "BookSeat";
    private ArrayList<Integer> timeIds;
    private String selectedDate,selectedTime,thh,tzone,tydate;
    private View prevView,fView;
    private LinearLayout datesView;
    private ViewGroup viewGroup;
    private TextView dateView;
    private Button prevButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookseat,container,false);
        fView = root;
        viewGroup = container;

        timeIds = new ArrayList<>();
        populateTimeIdsList();

        datesView = root.findViewById(R.id.dates);
        selectedItems = getArguments().getParcelableArrayList("bookingItems");

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

        Log.i(TAG,currentDateandTime);

        return root;
    }

    private void setUpTime(){
        View root = fView;
        prevButton = null;
        for (int i=0;i<timeIds.size();i++){
            final Button button = root.findViewById(timeIds.get(i));
            int temp = timeIds.get(i);
            String temp2 = button.getText().toString().split(":")[0];
            if((temp == R.id.t9 || temp == R.id.t10 || temp == R.id.t11 || temp == R.id.t12) && selectedDate.contains(tydate)){
                if(tzone.contains("PM")){
                    button.setClickable(false);
                    button.setBackgroundColor(getResources().getColor(R.color.text_color));
                }else {
                    if (Integer.parseInt(temp2) <= Integer.parseInt(thh)) {
                        button.setClickable(false);
                        button.setBackgroundColor(getResources().getColor(R.color.text_color));
                    } else {
                        button.setClickable(true);
                        button.setBackgroundColor(getResources().getColor(R.color.buttonActive));
                    }
                }
            }else if(tzone.contains("PM") && !thh.contains("12")){
                if(Integer.parseInt(temp2) <= Integer.parseInt(thh) && selectedDate.contains(tydate)){
                    button.setClickable(false);
                    button.setBackgroundColor(getResources().getColor(R.color.text_color));
                }else {
                    button.setClickable(true);
                    button.setBackgroundColor(getResources().getColor(R.color.buttonActive));
                }
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
                return "Th";
            case "th":
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
