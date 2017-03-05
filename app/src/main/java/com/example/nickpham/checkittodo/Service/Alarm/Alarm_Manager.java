package com.example.nickpham.checkittodo.Service.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nickpham.checkittodo.DataManager.Account_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_AccountInfo;
import com.example.nickpham.checkittodo.GAS.GAS_CoverString_Time;
import com.example.nickpham.checkittodo.MainActivity;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nickpham on 03/01/2017.
 */

public class Alarm_Manager {

    Context context;
    Calendar calendar_w_set;
    int Alarm_Id;


    AlarmManager call_alarm_manager;
    PendingIntent alarmIntent;

    public Alarm_Manager(Context context, Calendar calendar, int Alarm_Id) {

        this.context = context;
        this.calendar_w_set = calendar;
        this.Alarm_Id = Alarm_Id;

        call_alarm_manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }

    public void Start_Alarm() {
        Log.d("DD", "Starting Alarm !");

        Intent intent = new Intent(context, Alarm_Service.class);

        Bundle dm = new Bundle();
        dm.putString("DulieuType", "Normal");
        dm.putInt("DulieuID", Alarm_Id);
        dm.putString("TableName", MainActivity.Account_Bundle[0]);

        intent.putExtra("Data", dm);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Alarm_Id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar_w_set.getTimeInMillis(), pendingIntent);

    }


    public void Start_Alarm_For_Usually_Work() {

        Log.d("DD", "Starting Usually Work Alarm !");

        Intent intent = new Intent(context, Alarm_Service.class);

        Bundle dm = new Bundle();

        dm.putString("DulieuType", "Usually");
        dm.putInt("DulieuID", Alarm_Id);
        dm.putString("TableName", MainActivity.Account_Bundle[0]);

        intent.putExtra("Data", dm);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Alarm_Id, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        calendar_w_set.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        calendar_w_set.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
        calendar_w_set.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));


        String TimeA =
                String.valueOf(calendar_w_set.get(Calendar.HOUR_OF_DAY)) + "h" +
                        String.valueOf(calendar_w_set.get(Calendar.MINUTE)) + "&" +
                        String.valueOf(calendar_w_set.get(Calendar.DAY_OF_MONTH)) + "M" +
                        String.valueOf(calendar_w_set.get(Calendar.MONTH) + 1) + "Y" +
                        String.valueOf(calendar_w_set.get(Calendar.YEAR));

        boolean ValidTime = new GAS_CoverString_Time().Check_Valid_Time(TimeA);

        if (ValidTime == false)
        {
            calendar_w_set.set(Calendar.DAY_OF_MONTH, calendar_w_set.get(Calendar.DAY_OF_MONTH) + 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar_w_set.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);

    }


    public void Delete_Alarm()
    {
        Log.d("Action Alarm", "Canceling alarm this work");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, Alarm_Service.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Alarm_Id, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

}
