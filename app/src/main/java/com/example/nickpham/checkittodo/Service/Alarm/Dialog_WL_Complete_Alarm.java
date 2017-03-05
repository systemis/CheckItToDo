package com.example.nickpham.checkittodo.Service.Alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.nickpham.checkittodo.DataManager.Work_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;

import java.util.List;

public class Dialog_WL_Complete_Alarm extends Activity
{

    boolean Update_Result = true;
    int Pending_Id;
    String TableName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Pending_Id        = getIntent().getBundleExtra("Data").getInt("DulieuID");
        String Alarm_Type = getIntent().getBundleExtra("Data").getString("DulieuType");
        TableName         = getIntent().getBundleExtra("Data").getString("TableName");

        Custom_Start_AlarmSound();

        if (Alarm_Type.equals("Normal"))
        {
            Update_Result  = Update_Data();
            Setup_Notification_C(this, Pending_Id, "Anh yeu, em nho anh lam !");
        }else
        {
            Setup_Notification_C(this, Pending_Id, "Anh yeu, em nho anh lam !, toi gio lam cong viec thuong ngay cua anh roi!");
        }

    }

    // Setup UI
    void Setup_Notification_C(final Context context, final int ID, String Messgae)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dialog_WL_Complete_Alarm.this, android.R.style.Theme_Holo_Dialog);

        builder .setTitle("Notification")
                .setMessage(Messgae)
                .setNegativeButton("Ok, my love", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                        dialogInterface.dismiss();

                    }
                });



        Dialog diadl = builder.create();

        diadl.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //Cancel_Alarm(context, ID);
                //Dialog_WL_Complete_Alarm.this.startActivity(new Intent(context, MainActivity.class));
                new Alarm_Manager(context, null, ID).Delete_Alarm();

                if (Update_Result) { Toast.makeText(context, "Rất chi là chăm chỉ anh yêu ", Toast.LENGTH_SHORT).show();           }
                else               { Toast.makeText(context, "Không chăm chỉ chút nào, anh thịnh ơi", Toast.LENGTH_SHORT).show(); }

                if (md != null) { md.pause(); md.stop(); md = null;}

                // Check to finish MainActivity screen
                finish();

                //if (MainActivity.active) { MainActivity.fin}

            }
        });

        diadl.show();

        //Custom_Start_AlarmSound();
    }

    boolean Update_Data()
    {
        GAS_WorkInFormation work_alarm_picked = work_info_alarmed();

        Toast.makeText(this, work_alarm_picked.getName(), Toast.LENGTH_SHORT).show();
        
        work_alarm_picked.setComplete("1");

        return new Work_Data_Manager(this, "").Update_Folder(work_alarm_picked.getId(), work_alarm_picked);
    }


    MediaPlayer md =  null;
    void Custom_Start_AlarmSound()
    {

        final GAS_WorkInFormation work_alarm_picked = work_info_alarmed();

        if (work_alarm_picked != null)
        {
            final int URISound = Integer.parseInt(work_alarm_picked.getSoundAlarm());

            if (work_alarm_picked.getSoundAlarm() != null)
            {
                if (md == null) {
                    md = MediaPlayer.create(this, URISound);
                    md.start();
                    md.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (md != null)
                            {
                                md.start();
                            }
                        }
                    });
                }
            }
        }
    }


    GAS_WorkInFormation work_info_alarmed()
    {
        GAS_WorkInFormation work_info = new GAS_WorkInFormation();

        List<GAS_WorkInFormation> work_list = new Work_Data_Manager(this, TableName).Show_All_Work();

        Log.d("PendingId", String.valueOf(Pending_Id));

        if (work_list != null)
        {
            Log.d("CheckAlarm", "Yess Data");

            for (int i = 0; i < work_list.size(); i++)
            {

                Log.d("LoppID", work_list.get(i).getId());

                if (work_list.get(i).getId().equals(String.valueOf(Pending_Id)))
                {
                    Log.d("IDAlarmWork", work_list.get(i).getId());
                    work_info = work_list.get(i);
                }
            }
        }

        Toast.makeText(this, "DD:" + work_info.getName(), Toast.LENGTH_SHORT).show();

        return work_info;
    }

}


