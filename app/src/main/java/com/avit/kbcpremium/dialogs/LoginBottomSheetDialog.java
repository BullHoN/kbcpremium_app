package com.avit.kbcpremium.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avit.kbcpremium.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LoginBottomSheetDialog extends BottomSheetDialogFragment {

    private EditText phoneNoView;
    private String TAG = "Login";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottom_sheet_login,container,false);

        phoneNoView = root.findViewById(R.id.phoneNo);
        root.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = phoneNoView.getText().toString();
                if(phoneNo.length() != 10){
                    Toast.makeText(getContext(),"Invaild No",Toast.LENGTH_SHORT).show();
                    return;
                }

                // some logic to verify the phoneNo

            }
        });

        return root;
    }
}
