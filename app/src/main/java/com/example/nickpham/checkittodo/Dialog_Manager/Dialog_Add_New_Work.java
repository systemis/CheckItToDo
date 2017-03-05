package com.example.nickpham.checkittodo.Dialog_Manager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nickpham.checkittodo.AdapterManager.Adapter_Spiner_Time;
import com.example.nickpham.checkittodo.DataManager.Folder_Data_Manager;
import com.example.nickpham.checkittodo.DataManager.Work_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_CoverString_Time;
import com.example.nickpham.checkittodo.GAS.GAS_FolderNameInfo;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;
import com.example.nickpham.checkittodo.MainActivity;
import com.example.nickpham.checkittodo.R;
import com.example.nickpham.checkittodo.Service.Alarm.Alarm_Manager;

import java.util.Calendar;
import java.util.List;

public class Dialog_Add_New_Work implements View.OnClickListener
{

    public Context context;
    public int   Choice_Add;
    public GAS_WorkInFormation work_data_w_day;
    public String SelectedDate;

    public Dialog Dialog_This;

    public EditText  Import_WorkName, Import_WorkNote;
    public CheckBox  cb_complete_work;
    public TextView  Show_Time_Remind_Work, Show_Location_Remind_Work;
    public Spinner   Ranting_Imfortant_Work, spn_select_type, spn_select_sound;
    public ImageView imv_done_dialog, imv_delete_this_work, imv_exit_this_dialog, imv_delete_alarm;


    GAS_WorkInFormation gas_workInFormation;

    // Show_Information
    public Dialog_Add_New_Work(Context context, GAS_WorkInFormation work_data_w_day, String SelectedDate)
    {

        this.context              = context;
        this.work_data_w_day      = work_data_w_day;
        this.SelectedDate         = SelectedDate;

        gas_workInFormation = new GAS_WorkInFormation();

        Create_Dialog_This();

        Dialog_This.show();


        Log.d("Name Clicked", work_data_w_day.getName());

        //if (work_data_w_day != null) Set_Value_Database_Work_Info(gas_workInFormation);

        Import_WorkName          .setText(work_data_w_day.getName());
        Show_Location_Remind_Work.setText(work_data_w_day.getLocation());
        Import_WorkNote          .setText(work_data_w_day.getNote());

        Show_Time_Remind_Work    .setText(new GAS_CoverString_Time().Get_Time_Work_Form(work_data_w_day.getAlarm()));
        Ranting_Imfortant_Work   .setSelection(Get_Position_Item_SPN(Prioritys  , work_data_w_day.getPriority()));
        spn_select_sound         .setSelection(Get_Position_Item_Sound_SPN(Integer.parseInt(work_data_w_day.getSoundAlarm())));

        spn_select_type          .setSelection(Get_Position_Item_SPN(Works_Types, work_data_w_day.getType()));

        int Position_Type_Select = Get_Position_Item_SPN(Works_Types, work_data_w_day.getType());
        if (Position_Type_Select == -1)
        {
            spn_select_type.setSelection(Works_Types.length - 1);
        }else {
            spn_select_type.setSelection(Position_Type_Select);
        }

        Handling_Setup_View_W_DataBase();

    }



    // Only on update data method
    void Handling_Setup_View_W_DataBase()
    {
        if (work_data_w_day.getAlarm().isEmpty())
        {

            Show_Time_Remind_Work.setText(context.getResources().getString(R.string.Remind_User_Time));
            cb_complete_work.setEnabled(false);

        }else
        {

            Show_Time_Remind_Work.setText(new GAS_CoverString_Time().Get_Time_Work_Form(work_data_w_day.getAlarm()));

            if (work_data_w_day.getComplete().equals("-1"))
            {

                cb_complete_work.setChecked(false);

            }else
            {

                cb_complete_work.setChecked(true);
                Show_Time_Remind_Work.setPaintFlags(Show_Location_Remind_Work.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }

        }

    }


    // Create new Work
    public Dialog_Add_New_Work(Context context, int Choice_Add, String SelectedDate)
    {

        this.context      = context;
        this.Choice_Add   = Choice_Add;
        this.SelectedDate = SelectedDate;

        gas_workInFormation = new GAS_WorkInFormation();

        Create_Dialog_This();


        // Remove this viewgrop from layout of this dialog
        imv_delete_this_work.setVisibility(View.GONE);

        cb_complete_work.setEnabled(false);

        Dialog_This.show();
    }




    int Get_Position_Item_SPN(String[] ARR_W, String Text)
    {
        for (int i = 0; i < ARR_W.length; i++)
        {
            if (ARR_W[i].equals(Text))
            {
                return i;
            }
        }

        if (ARR_W == Works_Types)
        {
            return -1;
        }else {
            return 0;
        }

    }

    int Get_Position_Item_Sound_SPN(int Sound_get)
    {
        for (int i = 0; i < AlarmSoundArray.length; i++)
        {
            if (Sound_get == AlarmSoundArray[i])
            {
                return i;
            }
        }

        return 0;
    }


    // Xử lý để gán sự kiện cho CheckBox để biểu thị sự hoàn thoành của công việc.
    public void J_J_Handling_Enable_Checkbox_Complete()
    {

        cb_complete_work.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                // Only Update
                if (work_data_w_day != null)
                {
                    if (cb_complete_work.isChecked())
                    {
                        Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
                        if (work_data_w_day.getAlarm().isEmpty() == false)
                        {
                            Show_Time_Remind_Work.setPaintFlags(Show_Location_Remind_Work.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }else
                        {
                            Show_Time_Remind_Work.setText(context.getResources().getString(R.string.Remind_User_Time));

                            cb_complete_work.setEnabled(false);
                        }
                    }else
                    {
                        if (work_data_w_day.getAlarm().isEmpty() == false)
                        {
                            if (new GAS_CoverString_Time().Check_Valid_Time(Show_Time_Remind_Work.getText().toString()))
                            {
                                Show_Time_Remind_Work.setPaintFlags(0);
                            }else
                            {
                                cb_complete_work.setChecked(true);
                            }
                        }
                    }
                }
            }

        });

        cb_complete_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (work_data_w_day != null)
                {
                    if (Show_Time_Remind_Work.getText().toString().equals(context.getResources().getString(R.string.Remind_User_Time)))
                    {
                        Toast.makeText(context
                                , "Hành động này được thực hiện chỉ khi bạn đặt thời gian cho công việc này, hãy click vào dòng chữ '"
                                        + context.getResources().getString(R.string.Remind_User_Time) + "' trên màn hình", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(context, "Hành động này chỉ được thực hiện trong sự việc cập nhập công việc của bạn mà thôi !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    // Create properties for dialog add new work
    public void Create_Dialog_This()
    {

        Dialog_This = new Dialog(context); // new AlertDialog.Builder(context).setTitle("Create new Work").create();

        Dialog_This.setContentView(R.layout.dialog_add_new_work_layout);

        Dialog_This.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Dialog_This.setCancelable(false);

        Annhxa_Dialog_This();

        J_J_Handling_Enable_Checkbox_Complete();

    }


    void Annhxa_Dialog_This()
    {

        Import_WorkName           = (EditText)  Dialog_This.findViewById(R.id.Import_Work_Name);
        Import_WorkNote           = (EditText)  Dialog_This.findViewById(R.id.Import_Work_Note);

        Show_Time_Remind_Work     = (TextView)  Dialog_This.findViewById(R.id.Show_Time_Remind);
        Show_Location_Remind_Work = (TextView)  Dialog_This.findViewById(R.id.Show_Location_Remind);

        Ranting_Imfortant_Work    = (Spinner)   Dialog_This.findViewById(R.id.Ranting_This_Work);
        spn_select_type           = (Spinner)   Dialog_This.findViewById(R.id.SPN_Select_Work_Type);
        spn_select_sound          = (Spinner)   Dialog_This.findViewById(R.id.SPN_Select_Work_Sound_Alarm);

        imv_done_dialog           = (ImageView) Dialog_This.findViewById(R.id.Btn_Done_Dialog_Add_Work);
        imv_delete_this_work      = (ImageView) Dialog_This.findViewById(R.id.Btn_Delete_This_Work);
        imv_exit_this_dialog      = (ImageView) Dialog_This.findViewById(R.id.Btn_Exit_Dialog_This_Work);
        imv_delete_alarm          = (ImageView) Dialog_This.findViewById(R.id.Btn_Remove_Work_TimeRemind);

        imv_delete_alarm.setVisibility(View.INVISIBLE);

        cb_complete_work          = (CheckBox)  Dialog_This.findViewById(R.id.Check_Complete);


        if (work_data_w_day != null)
        {
            imv_delete_alarm.setEnabled(false);
        }else
        {
            imv_delete_alarm.setOnClickListener(this);
        }

        // if 9 - 2 = true

        Show_Time_Remind_Work.setOnClickListener(this);
        imv_exit_this_dialog .setOnClickListener(this);

        Custom_Adapter_Type_Spinner();

        Custom_Adapter_Priority_Spinner();

        Custom_Adapter_Alarm_Sound();
    }



    // Action of views of this dialog
    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {

            case R.id.Show_Time_Remind:

                new com.example.nickpham.checkittodo.Dialog_Manager.Dialog_Add_New_Work.Dialog_Select_Time(context, SelectedDate);

                break;


            case R.id.Btn_Remove_Work_TimeRemind:

                if (Show_Time_Remind_Work.getText().toString().equals(context.getResources().getString(R.string.Remind_User_Time)) == false)
                {

                    Show_Time_Remind_Work.setText(context.getResources().getString(R.string.Remind_User_Time));

                    imv_delete_alarm.setVisibility(View.INVISIBLE);

                }

                break;

            case R.id.Btn_Exit_Dialog_This_Work:

                Dialog_This.dismiss();

                break;

        }

    }



    // Type of work user want to add
    String[] Works_Types, Prioritys;
    void Custom_Adapter_Type_Spinner()
    {

        List<GAS_FolderNameInfo> Work_TypesList = new Folder_Data_Manager(context).Show_All_Folder();

        Works_Types = new String[Work_TypesList.size()];

        // Lopp to Get all folder name in database
        for (int i = 0; i < Work_TypesList.size(); i++)
        {
            Works_Types[i] = Work_TypesList.get(i).getFolderName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Works_Types);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        spn_select_type.setAdapter(adapter);
    }


    // Priority of the work user want to add
    void Custom_Adapter_Priority_Spinner()
    {

        Prioritys = new String[]{"Không quan trọng", "Cũng quan trọng", "Rất quan trọng"};

        //Ranting_Imfortant_Work.setAdapter(new Adapter_Spiner_Time(context, Prioritys));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Prioritys);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        Ranting_Imfortant_Work.setAdapter(adapter);

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


    boolean Set_Value_GAS()
    {

        boolean Result_SetValue = true;

        if (Show_Time_Remind_Work.getText().toString().equals(context.getResources().getString(R.string.Remind_User_Time)) == false)
        {

            boolean Result_Check_Valid_Time = new GAS_CoverString_Time().Check_Valid_Time(Show_Time_Remind_Work.getText().toString());

            if (Result_Check_Valid_Time)
            {
                gas_workInFormation.setAlarm(new GAS_CoverString_Time().Get_Time_Work(Show_Time_Remind_Work.getText().toString()));
                Result_SetValue = true;
            }else
            {
                Result_SetValue = false;
            }

        } else
        {
            gas_workInFormation.setAlarm("");
        }


        if(work_data_w_day != null) gas_workInFormation.setId(work_data_w_day.getId());

        if (cb_complete_work.isChecked() == false) { gas_workInFormation.setComplete("-1"); } else { gas_workInFormation.setComplete("1"); }


        // Set Value from view's values
        gas_workInFormation.setName(Import_WorkName.getText().toString());
        gas_workInFormation.setName(Import_WorkName.getText().toString());
        gas_workInFormation.setNote(Import_WorkNote.getText().toString());

        gas_workInFormation.setSoundAlarm(String.valueOf(AlarmSoundArray[spn_select_sound.getSelectedItemPosition()]));

        Log.d("SoundAlarmURI", gas_workInFormation.getSoundAlarm());

        gas_workInFormation.setLocation(Show_Location_Remind_Work.getText().toString());
        gas_workInFormation.setType(Works_Types[spn_select_type.getSelectedItemPosition()]);
        gas_workInFormation.setPriority(Prioritys[Ranting_Imfortant_Work.getSelectedItemPosition()]);


        return Result_SetValue;

    }

    void Start_Alarm_FT()
    {

        String Time_Database = gas_workInFormation.getAlarm();

        Calendar calendar = Calendar.getInstance();

        GAS_CoverString_Time coverString_time = new GAS_CoverString_Time();

        calendar.set(Calendar.HOUR_OF_DAY , coverString_time.Get_Time_info(Time_Database)[0]);
        calendar.set(Calendar.MINUTE      , coverString_time.Get_Time_info(Time_Database)[1]);

        calendar.set(Calendar.DAY_OF_MONTH, coverString_time.Get_Date_Info(Time_Database)[0]);
        calendar.set(Calendar.MONTH       , coverString_time.Get_Date_Info(Time_Database)[1] - 1);
        calendar.set(Calendar.YEAR        , coverString_time.Get_Date_Info(Time_Database)[2]);


        // WIll delete old alarm of this work --> start new alarm with new time
        if (work_data_w_day != null && work_data_w_day.getAlarm().isEmpty() == false)
        {
            new Alarm_Manager(context, null, Integer.parseInt(gas_workInFormation.getId())).Delete_Alarm();
        }

        new Alarm_Manager(context, calendar, Integer.parseInt(gas_workInFormation.getId())).Start_Alarm();
    }

    public void Add_New_Work()
    {

        if (Import_WorkName.getText().toString().isEmpty() == false)
        {

            if (Set_Value_GAS())
            {
                // Get id method only in Add new work Method
                gas_workInFormation.setId(String.valueOf(new Work_Data_Manager(context, "").Get_Id_Work_To_Add()));

                boolean Result_Add = new Work_Data_Manager(context, "").Add_New_Work(gas_workInFormation);

                if (Result_Add)
                {
                    Toast.makeText(context, "Them Thanh cong", Toast.LENGTH_SHORT).show();

                    if (gas_workInFormation.getAlarm().isEmpty() == false)
                    {
                        Start_Alarm_FT();
                    }

                    Dialog_This.dismiss();
                }else
                {
                    Toast.makeText(context, "Them that bai", Toast.LENGTH_SHORT).show();
                }
            }else
            {
                Toast.makeText(context, "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
            }

        }else
        {

            Import_WorkName.setError(context.getResources().getString(R.string.RemindNotWorkName), context.getResources().getDrawable(android.R.drawable.stat_sys_warning));
        }
    }


    public void Update_Work()
    {
        if (Import_WorkName.getText().toString().isEmpty() == false)
        {
            if (Set_Value_GAS())
            {
                boolean Result_Add = new Work_Data_Manager(context, "").Update_Folder(work_data_w_day.getId(), gas_workInFormation);

                if (Result_Add)
                {
                    Toast.makeText(context, "Cập nhập thành công", Toast.LENGTH_SHORT).show();

                    // Handling to turn on alarm
                    if (gas_workInFormation.getAlarm().isEmpty() == false && cb_complete_work.isChecked() == false)
                    {
                        if (new GAS_CoverString_Time().Check_Valid_Time(Show_Time_Remind_Work.getText().toString()))
                        {
                            Start_Alarm_FT();
                        }
                    }

                    // Handling to turn off alarm
                    if (work_data_w_day.getAlarm().isEmpty() != true)
                    {
                        if (cb_complete_work.isChecked())
                        {
                            new Alarm_Manager(context, null, Integer.parseInt(work_data_w_day.getId())).Delete_Alarm();
                        }

                    }
                    Dialog_This.dismiss();
                }
            }else
            {
                Toast.makeText(context, "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Import_WorkName.setError("Bạn chưa đặt tên cho công việc !", context.getResources().getDrawable(android.R.drawable.stat_sys_warning));
        }
    }



    // Delete work is selected by user
    public void Delete_Work()
    {

        Log.d("ActionS", work_data_w_day.getId());

        if (work_data_w_day.getAlarm().isEmpty() == false && work_data_w_day.getComplete().equals("-1"))
        {

            new Alarm_Manager(context, null, Integer.parseInt(work_data_w_day.getId())).Delete_Alarm();

        }

        new Work_Data_Manager(context, "").Delete_Work_F_Data(work_data_w_day.getId());

    }

    // this Dialog will show dialog for user choose Date and Time to set Alarm for work that user want add
    // Show this Dialog while user click time textview
    public class Dialog_Select_Time extends com.example.nickpham.checkittodo.Dialog_Manager.Dialog_Select_Time
    {

        public Dialog_Select_Time(final Context context, String Selected_Date_Time)
        {

            super(context, Selected_Date_Time);

            Create_Dialog();

            int Year, Month, Dayofmonth = 0;

            if (this.Selected_Date_Time.isEmpty())
            {

                Year       = Calendar.getInstance().get(Calendar.YEAR);
                Month      = Calendar.getInstance().get(Calendar.MONTH);
                Dayofmonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            } else
            {

                Year       = Get_Date_Info(Selected_Date_Time)[2];
                Month      = Get_Date_Info(Selected_Date_Time)[1] - 1;
                Dayofmonth = Get_Date_Info(Selected_Date_Time)[0];

            }

            // Custom for date picker to select date
            dp_select_date.init(Year, Month, Dayofmonth, new DatePicker.OnDateChangedListener()
            {

                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfmonth)
                {

                    String Hour_Selected   = hour_array[spn_select_hour.getSelectedItemPosition()];
                    String Minute_Selected = minute_array[spn_select_minute.getSelectedItemPosition()];
                    String Time_To_Set     = Hour_Selected + "h" + Minute_Selected + " " + String.valueOf(dayOfmonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);

                    if (new GAS_CoverString_Time().Check_Valid_Time(Time_To_Set))
                    {
                        Show_Time_Remind_Work.setPaintFlags(0);

                        Show_Time_Remind_Work.setText(Time_To_Set);

                        // Handling to on checked checkbox to show Dau Hieu alarm
                        if (work_data_w_day != null && work_data_w_day.getAlarm().isEmpty() == false)
                        {
                            cb_complete_work.setChecked(false);

                            cb_complete_work.setEnabled(true);
                        }else
                        {
                            imv_delete_alarm.setVisibility(View.VISIBLE);
                        }
                    }else
                    {
                        Toast.makeText(context, "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                    }


                    dialog_this.dismiss();

                }

            });



            ((Button) dialog_this.findViewById(R.id.Cancel_Choose_Time)).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    dialog_this.dismiss();
                }
            });

            dialog_this.setCancelable(false);
            dialog_this.show();

        }

    }

}
