package com.example.androidlabs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class AboutFragment extends Fragment {

    private View currentView;
    private MainActivity homeActivity;

    private static final int PERMISSION_READ_PHONE_STATE = 0;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        currentView = view;
        homeActivity = (MainActivity) getActivity();
        String api_version = BuildConfig.VERSION_NAME;
        TextView textView = view.findViewById(R.id.versionTextView);
        textView.setText(api_version);
        showHomePreview();
        return view;
    }

    private void showHomePreview() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            setUpIMEITextView();
        } else {
            requestReadPhoneStatePermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {

        if (requestCode == PERMISSION_READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpIMEITextView();
            } else {
                Context context = Objects.requireNonNull(homeActivity).getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(
                        context,
                        "Denied!",
                        duration
                );
                toast.show();
            }
        }
    }

    private void requestReadPhoneStatePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(homeActivity),
                Manifest.permission.READ_PHONE_STATE)) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(homeActivity);
            dialogBuilder.setMessage("Get permission")
            ;
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            requestPermissions(
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    PERMISSION_READ_PHONE_STATE
                            );
                        }
                    }
            );

            dialogBuilder.show();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_READ_PHONE_STATE
            );
        }
    }

    private void setUpIMEITextView() {
        TelephonyManager telephonyManager = (TelephonyManager) Objects.requireNonNull(homeActivity).getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"MissingPermission", "HardwareIds"}) String IMEI = telephonyManager.getDeviceId();
        TextView imeiTextView = currentView.findViewById(R.id.imeiTextView);
        imeiTextView.setText(IMEI);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}