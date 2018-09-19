package com.example.androidlabs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private String READ_PHONE_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        READ_PHONE_STATE = getResources().getString(R.string.allowReadPhoneState);

        TextView versionTextView = findViewById(R.id.versionTextView);
        String versionName = BuildConfig.VERSION_NAME ;
        versionTextView.setText(versionName);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
            );
        } else {

            TelephonyManager tm = (TelephonyManager)
                    getSystemService(this.TELEPHONY_SERVICE);

            String IMEI;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IMEI = tm.getImei();
            } else {
                IMEI = tm.getDeviceId();
            }

            setTextToIMEITextView(IMEI);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            // Processing permission results
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                String IMEI = READ_PHONE_STATE;

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        IMEI = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    } catch (SecurityException e) {

                    }
                }
                setTextToIMEITextView(IMEI);
            }
        }
    }


    private void setTextToIMEITextView(String text) throws SecurityException {
        TextView IMEITextView = findViewById(R.id.imeiTextView);
        IMEITextView.setText(text);
    }
}
