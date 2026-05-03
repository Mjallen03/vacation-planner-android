package com.example.vacationplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class VacationAlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("vacationTitle");
        String type  = intent.getStringExtra("type");   // "starting" or "ending"
        Toast.makeText(context,
                "Vacation \"" + title + "\" is " + type + " today!",
                Toast.LENGTH_LONG).show();
    }
}
