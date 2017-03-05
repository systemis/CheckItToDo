package com.example.nickpham.checkittodo.Dialog_Manager;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nickpham.checkittodo.DataManager.Account_Data_Manager;
import com.example.nickpham.checkittodo.DataManager.Work_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_CoverString_Time;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;
import com.example.nickpham.checkittodo.MainActivity;
import com.example.nickpham.checkittodo.R;
import com.example.nickpham.checkittodo.Service.Alarm.Alarm_Manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nickpham on 05/01/2017.
 */

public class Dialog_Add_New_Usually_Work implements View.OnClickListener {

    public Context context;
    public GAS_WorkInFormation usually_work_selected = null;

    public String Type = "Usually";

    public Dialog Dialog_Add_UsuallyWork_This;

    public LinearLayout layout_main_this, manager_checkBox_currentWeek;
    public EditText     import_usually_work_name;
    public Spinner      spn_select_usually_work_hour, spn_select_usually_work_minute, spn_select_sound;
    public LinearLayout ln_comprise_checkboxs_week_time;
    public ImageView    imv_done_this, imv_delete_this, imv_exit_this;

    public Dialog_Add_New_Usually_Work(Context context) {

        this.context = context;

        CreateDialog();

        imv_delete_this.setVisibility(View.INVISIBLE);

        Dialog_Add_UsuallyWork_This.show();

    }


    public Dialog_Add_New_Usually_Work(Context context, GAS_WorkInFormation usually_work_selected) {

        this.context = context;
        this.usually_work_selected = usually_work_selected;

        CreateDialog();

        Get_Set_Value_F_Data();

        Dialog_Add_UsuallyWork_This.show();

    }


    // This method only use for update contruct
    public void Get_Set_Value_F_Data() {

        if (usually_work_selected != null)
        {

            import_usually_work_name.setText(usually_work_selected.getName());

            String Hour_Work_Selected   = String.valueOf(new GAS_CoverString_Time().Get_Time_info(usually_work_selected.getAlarm())[0]);
            String Minute_Work_Selected = String.valueOf(new GAS_CoverString_Time().Get_Time_info(usually_work_selected.getAlarm())[1]);

            spn_select_usually_work_hour  .setSelection(Position_Item_Time_SPN(hour_array, Hour_Work_Selected));
            spn_select_usually_work_minute.setSelection(Position_Item_Time_SPN(minute_array, Minute_Work_Selected));

            String[] SoundAlarms = new String[AlarmSoundArray.length];
            for (int i = 0; i < SoundAlarms.length; i++) SoundAlarms[i] = String.valueOf(AlarmSoundArray[i]);
            spn_select_sound              .setSelection(Position_Item_Time_SPN(SoundAlarms, usually_work_selected.getSoundAlarm()));

            Handling_Set_Current_Week();

        }

    }


    public void Handling_Set_Current_Week()
    {

        cb_current_week_list.get(0).setChecked(false);

        Log.d("DAlarm", usually_work_selected.getAlarm());

        if (usually_work_selected.getAlarm().indexOf("%") != -1)
        {

            Log.d("Current_Count", String.valueOf(new GAS_CoverString_Time().GetCurrentWeek(usually_work_selected.getAlarm()).size()));

            List<String> current_week_list = new GAS_CoverString_Time().GetCurrentWeek(usually_work_selected.getAlarm());

            for (int i = 0; i < current_week_list.size(); i++)
            {

                Log.d("Current in week", current_week_list.get(i));

                int position = Position_Item_Time_SPN(new GAS_CoverString_Time().text_com, current_week_list.get(i));

                if (position != -1) { cb_current_week_list.get(position).setChecked(true); }

                Log.d("Current_Count", String.valueOf(new GAS_CoverString_Time().GetCurrentWeek(usually_work_selected.getAlarm()).size()));

            }

        }

    }


    public void CreateDialog()
    {

        Dialog_Add_UsuallyWork_This = new Dialog(context);

        Dialog_Add_UsuallyWork_This.setContentView(R.layout.dialog_add_new_usually_work);

        Dialog_Add_UsuallyWork_This.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Dialog_Add_UsuallyWork_This.setCancelable(false);


        AnhxaDialogThis();

    }

    public void AnhxaDialogThis()
    {

        layout_main_this                = (LinearLayout) Dialog_Add_UsuallyWork_This.findViewById(R.id.Layout_Main_Work_Usually_Dialog);
        import_usually_work_name        = (EditText)     Dialog_Add_UsuallyWork_This.findViewById(R.id.ImportuUsuallyWorkNameDialog);
        spn_select_usually_work_hour    = (Spinner)      Dialog_Add_UsuallyWork_This.findViewById(R.id.SPN_Select_Hour_UsuallyWork);
        spn_select_usually_work_minute  = (Spinner)      Dialog_Add_UsuallyWork_This.findViewById(R.id.SPN_select_Minute_UsuallyWork);
        spn_select_sound                = (Spinner)      Dialog_Add_UsuallyWork_This.findViewById(R.id.SPN_Select_Work_Sound_Alarm_UsuallyWorkDialog);
        ln_comprise_checkboxs_week_time = (LinearLayout) Dialog_Add_UsuallyWork_This.findViewById(R.id.Manager_CheckBox_CurrentWeek);

        imv_done_this                   = (ImageView)    Dialog_Add_UsuallyWork_This.findViewById(R.id.Btn_Done_Dialog_UsuallyWork);
        imv_delete_this                 = (ImageView)    Dialog_Add_UsuallyWork_This.findViewById(R.id.Btn_Delete_Dialog_UsuallyWork);
        imv_exit_this                   = (ImageView)    Dialog_Add_UsuallyWork_This.findViewById(R.id.Btn_Exit_Dialog_UsuallWork);
        manager_checkBox_currentWeek    = (LinearLayout) Dialog_Add_UsuallyWork_This.findViewById(R.id.Manager_CheckBox_CurrentWeek);

        Handling_Add_Check_Box_CurrentWidth();

        Setup_SPN_This();

        imv_done_this            .setOnClickListener(this);
        imv_delete_this          .setOnClickListener(this);
        imv_exit_this            .setOnClickListener(this);

        Custom_Adapter_Alarm_Sound();

    }




    //int[] AlarmSound_Array = {R.raw.default, R.raw.cuckoo , R.raw.default, R.raw.default, R.raw.default, R.raw.default, R.raw.default};
    int[] AlarmSoundArray = {R.raw.macdinh, R.raw.cuckoo_clock, R.raw.harp_strumming, R.raw.nuclear_warning, R.raw.rooster, R.raw.woman_screaming, R.raw.zombie};
    MediaPlayer media_alarm_sound_tes = null;
    void Custom_Adapter_Alarm_Sound()
    {

        spn_select_sound.setTag(0);

        String[] AlarmSoundS_Array = {"Default", "Cuckoo", "Harp Struming", "Nuclear warring", "Rooster", "Wonman screaming", "Zombie"};

        ArrayAdapter<String> spn_alarm_sound_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, AlarmSoundS_Array);
        spn_alarm_sound_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        spn_select_sound.setAdapter(spn_alarm_sound_adapter);

        spn_select_sound.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (Integer.parseInt(String.valueOf(spn_select_sound.getTag())) != 0)
                {
                    if(media_alarm_sound_tes != null) media_alarm_sound_tes.stop();

                    media_alarm_sound_tes = MediaPlayer.create(context, AlarmSoundArray[i]);

                    media_alarm_sound_tes.start();
                }

                spn_select_sound.setTag(1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    List<CheckBox> cb_current_week_list = new ArrayList<>();
    void Handling_Add_Check_Box_CurrentWidth()
    {

        for (int i = 0; i < new GAS_CoverString_Time().text_uri.length; i++)
        {

            cb_current_week_list.add(checkBox_show_current_day_week(i + 1, context.getResources().getString(new GAS_CoverString_Time().text_uri[i])));

            manager_checkBox_currentWeek.addView(cb_current_week_list.get(i));

        }

    }

    CheckBox checkBox_show_current_day_week(int position, String current)
    {

        CheckBox checkBox = new CheckBox(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        checkBox.setLayoutParams(params);

        checkBox.setTag(position);

        checkBox.setText(current);

        if (usually_work_selected == null)
        {
            checkBox.setChecked(true);
        }else
        {
            checkBox.setChecked(false);
        }

        return checkBox;
    }



    public String[] hour_array = new String[24], minute_array = new String[60];
    public void Setup_SPN_This()
    {

        spn_select_usually_work_hour  .setTag("Hour");
        spn_select_usually_work_minute.setTag("Minute");

        for (int i = 0; i < minute_array.length; i++)
        {
            if (i < hour_array.length)
            {
                hour_array[i] = String.valueOf(i + 1);
            }

            if (i < 10) minute_array[i] = "0" + String.valueOf(i); else { minute_array[i] = String.valueOf(i); }

        }


        spn_select_usually_work_hour  .setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, hour_array));
        spn_select_usually_work_minute.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, minute_array));

    }

    int Position_Item_Time_SPN(String[] array_w_c, String text) {

        int result = -1;

        boolean isHave = false;

        for (int i = 0; i < array_w_c.length; i++)
        {
            if (array_w_c[i].equals(text))
            {
                result = i;
                isHave = true;
            }
        }

        if (isHave == false){
            if (array_w_c == hour_array || array_w_c == minute_array)
            {
                result = 0;
            }else
            {
                result = -1;
            }
        }

        return result;

    }


    @Override
    public void onClick(View view)
    {


        switch (view.getId())
        {

            case R.id.Btn_Done_Dialog_UsuallyWork:

                if (import_usually_work_name.getText().toString().isEmpty() == false)
                {

                    if (usually_work_selected == null)
                    {

                        if (Add_New_Work())
                        {

                            Toast.makeText(context, "Them Thanh cong", Toast.LENGTH_SHORT).show();

                            Dialog_Add_UsuallyWork_This.dismiss();

                        }else
                        {
                            Toast.makeText(context, "Work is early created!", Toast.LENGTH_SHORT).show();
                        }


                    }else
                    {

                        if (Update_New_Work())
                        {
                            Toast.makeText(context, "Cap nhap thanh cong", Toast.LENGTH_SHORT).show();
                            Dialog_Add_UsuallyWork_This.dismiss();
                        }

                    }


                }else
                {
                    import_usually_work_name.setError(context.getResources().getString(R.string.RemindNotWorkName));
                }

                break;

            case R.id.Btn_Delete_Dialog_UsuallyWork:

                new Work_Data_Manager(context, MainActivity.Account_Bundle[0]).Delete_Work_F_Data(usually_work_selected.getId());

                Dialog_Add_UsuallyWork_This.dismiss();

                break;

            case R.id.Btn_Exit_Dialog_UsuallWork:

                Dialog_Add_UsuallyWork_This.dismiss();

                break;

        }

    }

    void Start_Alarm(int Id)
    {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour_array  [spn_select_usually_work_hour  .getSelectedItemPosition()]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minute_array[spn_select_usually_work_minute.getSelectedItemPosition()]));

        new Alarm_Manager(context, calendar, Id).Start_Alarm_For_Usually_Work();
    }

    public boolean Add_New_Work()
    {

        GAS_WorkInFormation workInFormation = new GAS_WorkInFormation();

        workInFormation.setId(String.valueOf(new Work_Data_Manager(context, "").Get_Id_Work_To_Add()));
        workInFormation.setName(Handling_Remove_LastChar_SP(import_usually_work_name.getText().toString()));

        workInFormation.setAlarm(
                hour_array[spn_select_usually_work_hour.getSelectedItemPosition()] + "h" + minute_array[spn_select_usually_work_minute.getSelectedItemPosition()]
                        + "CUR" + Handling_Get_Current_Time_To_Set());

        workInFormation.setType(Type);
        workInFormation.setCurrentTimeWeek("1");
        workInFormation.setSoundAlarm(String.valueOf(AlarmSoundArray[spn_select_sound.getSelectedItemPosition()]));

        boolean Result = new Work_Data_Manager(context, "").Add_New_Work(workInFormation);

        if (Result)
        {
            Start_Alarm(Integer.parseInt(workInFormation.getId()));
        }

        return Result;
    }


    public boolean Update_New_Work()
    {

        GAS_WorkInFormation workInFormation = usually_work_selected;

        workInFormation.setName(Handling_Remove_LastChar_SP(import_usually_work_name.getText().toString()));

        workInFormation.setAlarm(
                hour_array[spn_select_usually_work_hour.getSelectedItemPosition()] + "h" + minute_array[spn_select_usually_work_minute.getSelectedItemPosition()]
                + "CUR" + Handling_Get_Current_Time_To_Set());

        workInFormation.setCurrentTimeWeek("1");
        workInFormation.setSoundAlarm(String.valueOf(AlarmSoundArray[spn_select_sound.getSelectedItemPosition()]));

        boolean Result = new Work_Data_Manager(context, "").Update_Folder(workInFormation.getId(), workInFormation);

        if (Result)
        {

            new Alarm_Manager(context, null, Integer.parseInt(workInFormation.getId())).Delete_Alarm();

            Start_Alarm(Integer.parseInt(workInFormation.getId()));
        }

        return Result;

    }


    // Remove chars if lats char of text is empty
    String Handling_Remove_LastChar_SP(String Text)
    {

        for (int i = Text.toCharArray().length; i >= 1; i--)
        {

            if(String.valueOf(Text.toCharArray()[i - 1]).equals(" "))
            {

                Text = Text.substring(0, i - 1);
            }else
            {
                i = -1;
            }

        }

        return Text;

    }


    String Handling_Get_Current_Time_To_Set()
    {

        String Current_Time = "";

        if (cb_current_week_list.size() > 0)
        {

            for (int i = 0; i < cb_current_week_list.size(); i++)
            {

                if (cb_current_week_list.get(i).isChecked())
                {

                    Current_Time = Current_Time + new GAS_CoverString_Time().text_com[i] + "%" ;

                }

            }

        }

        return Current_Time;
    }

}
