package com.avit.kbcpremium.ui.appointment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.avit.kbcpremium.R;

import java.util.List;

public class AppointmentFragment extends Fragment {

    private static final String TAG = "AppointmentFragment";
    private AppointmentFragmentViewModel viewModel;
    private List<AppointmentItem> mAppointmentItems;
    private LinearLayout itemsView;
    private ViewGroup viewGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_appointment, container, false);
        viewModel = ViewModelProviders.of(this).get(AppointmentFragmentViewModel.class);
        itemsView = root.findViewById(R.id.items);
        viewGroup = container;

        viewModel.getAllItems().observe(this, new Observer<List<AppointmentItem>>() {
            @Override
            public void onChanged(List<AppointmentItem> appointmentItems) {
                Log.i(TAG, appointmentItems.size() + "");
                mAppointmentItems = appointmentItems;
                populateUI();
            }
        });

        return root;
    }

    private void populateUI(){
        for(int i=0;i<mAppointmentItems.size();i++){
            AppointmentItem curr = mAppointmentItems.get(i);
            View view = getLayoutInflater().inflate(R.layout.appointmentcart_item,viewGroup,false);

            String items[] = curr.getItems().split("=");

            TextView priceView = view.findViewById(R.id.total);
            priceView.setText("₹" + curr.getTotal());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Activities");

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            final AlertDialog dialog = builder.create();
            view.findViewById(R.id.viewmore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });

            TextView dateView = view.findViewById(R.id.date);
            dateView.setText(curr.getDate());

            TextView timeView = view.findViewById(R.id.time);
            timeView.setText(curr.getTime());

            itemsView.addView(view);
        }
    }


}