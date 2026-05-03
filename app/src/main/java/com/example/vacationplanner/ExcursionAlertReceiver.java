package com.example.vacationplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ExcursionAlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("excursionTitle");
        Toast.makeText(context, "Excursion '" + title + "' is happening today!", Toast.LENGTH_LONG).show();
    }
}
