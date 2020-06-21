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
    private ArrayList<Integer> daysIds,timeIds;
    private String selectedDate,selectedTime;
    private View prevView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookseat,container,false);

        daysIds = new ArrayList<>();
        timeIds = new ArrayList<>();
        populateIdsList();
        populateTimeIdsList();

        selectedItems = getArguments().getParcelableArrayList("bookingItems");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E,dd MMM yyyy,hh:mm:ss,a");
        String currentDateandTime = simpleDateFormat.format(new Date());

        final String date = currentDateandTime.split(",")[1];
        final TextView dateView = root.findViewById(R.id.date);
        dateView.setText(date);

        int tdate = Integer.parseInt(date.split(" ")[0]);
        String tday = currentDateandTime.split(",")[0];
        prevView = null;

        for(int i=0;i<daysIds.size();i++){
            final View daysView = root.findViewById(daysIds.get(i));

            final TextView daysDateView = daysView.findViewById(R.id.appdate);
            daysDateView.setText(String.valueOf(tdate));
            tdate++;

            if(i == 0){
                prevView = daysDateView;
            }

            TextView daysDayView = daysView.findViewById(R.id.day);
            daysDayView.setText(tday);

            tday = nextDay(tday.toLowerCase());

            daysView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prevView.setBackgroundColor(getResources().getColor(R.color.trans));
                    daysDateView.setBackgroundColor(getResources().getColor(R.color.white));
                    prevView = daysDateView;

                    selectedDate = daysDateView.getText().toString() + " " + date.split(" ")[1]
                            + " " + date.split(" ")[2];
                    dateView.setText(selectedDate);
                }
            });

        }

        String thh = currentDateandTime.split(",")[2].split(":")[0];
        String tzone = currentDateandTime.split(",")[3];

        for (int i=0;i<timeIds.size();i++){
            Button button = root.findViewById(timeIds.get(i));
            int temp = timeIds.get(i);
            if(temp == R.id.t9 || temp == R.id.t10 || temp == R.id.t11 || temp == R.id.t12){
                String temp2 = button.getText().toString().split(":")[0];
                if(tzone.contains("PM") && Integer.parseInt(temp2) <= Integer.parseInt(thh) && selectedDate != date){
                    button.setClickable(false);
                    button.setBackgroundColor(getResources().getColor(R.color.text_color));
                }
            }


        }

        Log.i(TAG,currentDateandTime);

        return root;
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

    private void populateIdsList(){
        daysIds.add(R.id.first);
        daysIds.add(R.id.second);
        daysIds.add(R.id.third);
        daysIds.add(R.id.fourth);
        daysIds.add(R.id.fifth);
        daysIds.add(R.id.sixth);
        daysIds.add(R.id.seventh);
    }

}
