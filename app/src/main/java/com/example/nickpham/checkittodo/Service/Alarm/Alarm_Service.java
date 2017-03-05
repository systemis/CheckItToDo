package com.example.nickpham.checkittodo.Service.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.nickpham.checkittodo.DataManager.Account_Data_Manager;
import com.example.nickpham.checkittodo.DataManager.Work_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_AccountInfo;
import com.example.nickpham.checkittodo.GAS.GAS_CoverString_Time;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;
import com.example.nickpham.checkittodo.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nickpham on 03/01/2017.
 */

public class Alarm_Service extends BroadcastReceiver {

    Context context;
    int Pending_Id;
    String TableName;

    public void onReceive(final Context context, final Intent intent)
    {

        this.context = context;

        Pending_Id = intent.getBundleExtra("Data").getInt("DulieuID");
        TableName  = intent.getBundleExtra("Data").getString("TableName");

        String AlarmType = intent.getBundleExtra("Data").getString("DulieuType");

        //Setup_Notification(context, Pending_Id, Message);
        Intent Dialog_Intent = new Intent(context, Dialog_WL_Complete_Alarm.class);

        Bundle bundle = new Bundle();

        bundle.putInt("DulieuID", Pending_Id);
        bundle.putString("DulieuType", AlarmType);
        bundle.putString("TableName", TableName);

        Dialog_Intent.putExtra("Data", bundle);

        Log.d("Alarm_table", TableName);

        List<String> accountnamelist = new ArrayList<>();
        List<GAS_AccountInfo> accountlist = new Account_Data_Manager(context).Show_All_Account();
        if (accountlist != null) for (int i = 0; i < accountlist.size(); i++) accountnamelist.add(accountlist.get(i).getAccount());

        if (accountnamelist != null && accountnamelist.size() > 0 && accountnamelist.indexOf(TableName) != -1 || TableName.isEmpty())
        {
            if (AlarmType.equals("Usually"))
            {
                if (HandlingCheckCurrentDay(Pending_Id))
                {
                    context.startActivity(Dialog_Intent);
                }else
                {
                    Toast.makeText(context, "Not today", Toast.LENGTH_SHORT).show();
                }
            } else
            {
                context.startActivity(Dialog_Intent);
            }
        }else if (TableName.isEmpty() == false && accountnamelist == null && accountnamelist.indexOf(TableName) == -1)
        {
            // No End
            new Alarm_Manager(context, null, Pending_Id).Delete_Alarm();
        }

        //((MainActivity) context).finish();

    }


    boolean HandlingCheckCurrentDay(int Pending_Id)
    {

        boolean Result = false;

        GAS_WorkInFormation workInfo = null;

        for (int i = 0; i < new Work_Data_Manager(context, "").Show_All_Work().size(); i++)
        {
            if (new Work_Data_Manager(context, "").Show_All_Work().get(i).getId().equals(String.valueOf(Pending_Id)))
            {
                workInfo = new Work_Data_Manager(context, "").Show_All_Work().get(i);
            }
        }

        List<String> current_week = new GAS_CoverString_Time().GetCurrentWeek(workInfo.getAlarm());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        Log.d("Hom nay la thu: ", dayOfTheWeek);

        if (workInfo != null)
        {
            Log.d("current_wekk:   ", current_week.get(0));
            if (current_week.size() > 0)
            {
                if (current_week.indexOf(dayOfTheWeek) != -1)
                {
                    Result = true;
                }
            }
        }
        return Result;
    }

}

