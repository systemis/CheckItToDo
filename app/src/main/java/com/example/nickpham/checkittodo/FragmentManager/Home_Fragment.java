package com.example.nickpham.checkittodo.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Systemis on 30/12/2016.
 */

public class Home_Fragment extends Fragment
{

    Context context;
    int Choice;
    View View_This;

    LinearLayout Main_Layout_Home_Fragment;
    com.github.clans.fab.FloatingActionButton flc_add_new_work, flc_remove_work_w_date;
    Spinner spn_select_day, spn_select_month, spn_select_year;
    ImageButton btn_next_day, btn_previous_day;
    Button btn_to_day;

    public Home_Fragment(){}

    @SuppressLint("ValidFragment")
    public Home_Fragment(Context context, int Choice)
    {

        this.context = context;
        this.Choice = Choice;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View_This = inflater.inflate(R.layout.home_fragment_layout, container, false);

        Log.d("Test", "Today");

        Anhxa();

        Custom_Time_Picker(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.YEAR));

        Selected_Action_SPN();

        Handling_Data_Works();

        Log.d("Test", "TestDay");

        return View_This;

    }

    void Anhxa()
    {

        Main_Layout_Home_Fragment = (LinearLayout) View_This.findViewById(R.id.Layout_Main_Home_Fragment);

        flc_add_new_work          = (com.github.clans.fab.FloatingActionButton) View_This.findViewById(R.id.Flt_Add_New_Work);
        flc_remove_work_w_date    = (com.github.clans.fab.FloatingActionButton) View_This.findViewById(R.id.Flt_Remove_Work_All_Home_Frgament);

        spn_select_day            = (Spinner) View_This.findViewById(R.id.SPN_Select_Day);
        spn_select_month          = (Spinner) View_This.findViewById(R.id.SPN_Select_Month);
        spn_select_year           = (Spinner) View_This.findViewById(R.id.SPN_Select_Year);

        btn_next_day              = (ImageButton) View_This.findViewById(R.id.Btn_NextDay);
        btn_previous_day          = (ImageButton) View_This.findViewById(R.id.Btn_PreviousDay);

        btn_to_day                = (Button) View_This.findViewById(R.id.btn_today_home);

        flc_add_new_work      .setOnClickListener(new connectMethod(flc_add_new_work));
        flc_remove_work_w_date.setOnClickListener(new connectMethod(flc_remove_work_w_date));
        btn_next_day          .setOnClickListener(new connectMethod(btn_next_day));
        btn_previous_day      .setOnClickListener(new connectMethod(btn_previous_day));
        btn_to_day            .setOnClickListener(new connectMethod(btn_to_day));

    }

    // ---------------------------------------- Handling about Spnner Picker ---------------------------------------------

    String[] Years_Count, Months_Count_in_Year, Days_Count_in_Month;

    public void Custom_Time_Picker(int Day_Set, int Month_Set, int Year_Set) {

        spn_select_day.setAdapter(null);
        spn_select_month.setAdapter(null);
        spn_select_year.setAdapter(null);

        // Calendar.DAY_OF_MONTH, 0, Calendar.YEAR
        int Day_Of_Month = new GregorianCalendar(Calendar.DAY_OF_MONTH, Month_Set - 1, Calendar.YEAR).getActualMaximum(Calendar.DAY_OF_MONTH);


        // For Adapter
        Years_Count = new String[100];
        Months_Count_in_Year = new String[12];
        Days_Count_in_Month = new String[Day_Of_Month];

        // This app have created since 2016 so this app will active into 100 year late
        for (int i = 0; i < 100; i++) { Years_Count[i] = String.valueOf(2016 + i); }

        for (int i = 0; i < Day_Of_Month; i++) { Days_Count_in_Month[i] = String.valueOf(i + 1);}

        for (int i = 0; i < 12; i++) { Months_Count_in_Year[i] = String.valueOf(i + 1); }

        spn_select_year.setAdapter(new Adapter_Spiner_Time(context, Years_Count));
        spn_select_day.setAdapter(new Adapter_Spiner_Time(context, Days_Count_in_Month));
        spn_select_month.setAdapter(new Adapter_Spiner_Time(context, Months_Count_in_Year));

        if (Day_Set > Day_Of_Month) Day_Set = 1;

        spn_select_day.setSelection(Get_Position_W_T(Days_Count_in_Month, String.valueOf(Day_Set)));
        spn_select_month.setSelection(Get_Position_W_T(Months_Count_in_Year, String.valueOf(Month_Set)));

        if (Year_Set < Integer.parseInt(Years_Count[Years_Count.length - 1]) && Year_Set >= 2016)
            spn_select_year.setSelection(Get_Position_W_T(Years_Count, String.valueOf(Year_Set)));

        Handling_Data_Works();

    }

    // Get position item with text
    public int Get_Position_W_T(String[] spn, String text) {
        int Pos = 0;

        for (int i = 0; i < spn.length; i++) {

            if (spn[i].equals(text)) {
                Pos = i;
            }

        }

        return Pos;

    }


    // Action of Spinners ( Day, Month, Year)
    void Selected_Action_SPN()
    {

        // Action of Month Spinner
        spn_select_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {

                Log.d("Action_Select_Month", "Change Day Count");

                int Position_Now_Day = spn_select_day.getSelectedItemPosition();

                spn_select_day.setAdapter(null);

                // Get day count in the month has been selected by user
                int days_in_month = new GregorianCalendar(Calendar.DAY_OF_MONTH, Integer.parseInt(Months_Count_in_Year[position]) - 1, Calendar.YEAR).getActualMaximum(Calendar.DAY_OF_MONTH);

                Days_Count_in_Month = new String[days_in_month];

                for (int i = 0; i < days_in_month; i++) { Days_Count_in_Month[i] = String.valueOf(i + 1); }

                if (Position_Now_Day > days_in_month) Position_Now_Day  = days_in_month - 1;

                spn_select_day.setAdapter(new Adapter_Spiner_Time(context, Days_Count_in_Month));

                spn_select_day.setSelection(Position_Now_Day);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Action of Day Spinner and Year Spinner
        AdapterView.OnItemSelectedListener lis = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                Handling_Data_Works();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        spn_select_day.setOnItemSelectedListener(lis);
        spn_select_year.setOnItemSelectedListener(lis);


    }


    // ---------------------------------------- Handling about Spnner Picker ---------------------------------------------


    // ---------------------------------------- Handling to Next and Previous Day ----------------------------------------

    public String[] Handling_Next_Day(String Day_Set, String Month_Set, String Year_Set)
    {
        String[] Date_Bundle_Handling = new String[3];

        int Day = Integer.parseInt(Day_Set);
        int Month = Integer.parseInt(Month_Set);
        int Year = Integer.parseInt(Year_Set);

        int Day_Count_In_Month = new GregorianCalendar(Calendar.DAY_OF_MONTH, Month - 1, Calendar.YEAR).getActualMaximum(Calendar.DAY_OF_MONTH);

        Log.d("CountDay", String.valueOf(Day_Count_In_Month));

        if (Day < Day_Count_In_Month)
        {
            Day += 1;
        } else
        {
            Day = 1;
            if (Month < 12)
            {
                Month += 1;
            } else
            {
                Month = 1;
                Year += 1;
            }
        }

        Date_Bundle_Handling[0] = String.valueOf(Day);
        Date_Bundle_Handling[1] = String.valueOf(Month);
        Date_Bundle_Handling[2] = String.valueOf(Year);


        Log.d("Day_Set", Date_Bundle_Handling[0]);
        Log.d("Month_Set", Date_Bundle_Handling[1]);
        Log.d("YEAR_Set", Date_Bundle_Handling[2]);

        return Date_Bundle_Handling;
    }

    public String[] Handling_Previous_Day(String Day_Set, String Month_Set, String Year_Set) {


        String[] Date_Bundle_Handling = new String[3];

        int Day = Integer.parseInt(Day_Set);
        int Month = Integer.parseInt(Month_Set);
        int Year = Integer.parseInt(Year_Set);

        int Day_Count_In_Month = new GregorianCalendar(Calendar.DAY_OF_MONTH, Month - 1, Calendar.YEAR).getActualMaximum(Calendar.DAY_OF_MONTH);

        Log.d("CountDay", String.valueOf(Day_Count_In_Month));

        if (Day > 1) {
            Day -= 1;
        } else {
            if (Month > 1) {
                Month -= 1;
            } else {
                Month = 12;
                Year -= 1;
            }

            Day_Count_In_Month = new GregorianCalendar(Calendar.DAY_OF_MONTH, Month - 1, Calendar.YEAR).getActualMaximum(Calendar.DAY_OF_MONTH);
            Day = Day_Count_In_Month;

        }

        Date_Bundle_Handling[0] = String.valueOf(Day);
        Date_Bundle_Handling[1] = String.valueOf(Month);
        Date_Bundle_Handling[2] = String.valueOf(Year);


        Log.d("Day_Set", Date_Bundle_Handling[0]);
        Log.d("Month_Set", Date_Bundle_Handling[1]);
        Log.d("YEAR_Set", Date_Bundle_Handling[2]);

        return Date_Bundle_Handling;

    }

    // ---------------------------------------- Handling to Next and Previous Day ----------------------------------------


    // Work to get data
    Work_Data_Manager work_data_manager;      
    boolean isHaveData_w_day = false;
    public void Handling_Data_Works()
    {
        Main_Layout_Home_Fragment.removeAllViews();

        Log.d("TableName", MainActivity.Account_Bundle[0]);

        work_data_manager = new Work_Data_Manager(context, "");

        List<GAS_FolderNameInfo> Type_List = new Folder_Data_Manager(context).Show_All_Folder();

        for (int i = 0; i < Type_List.size(); i++)
        {
            List<GAS_WorkInFormation> works_data_w_day = Get_Work_W_The_Day(Type_List.get(i).getFolderName());

            // Phân loại kiểu công việc ( hai kiểu mặc định: Gia đình, công việc)
            if (works_data_w_day != null && works_data_w_day.size() > 0)
            {
                isHaveData_w_day = true;

                Main_Layout_Home_Fragment.addView(txt_title_type(Type_List.get(i).getFolderName()));

                Setup_isHaveData(works_data_w_day);
            }

        }

        // Check Other
        if (isHaveData_w_day == false)
        {
            Setup_isNotHaveData();
        }
    }


    // This TextView will show type value
    TextView txt_title_type(String Type)
    {

        TextView txt_result = new TextView(context);

        txt_result.setText(Type);

        return txt_result;

    }


    void Setup_isNotHaveData()
    {
        RelativeLayout rl_comprise_textview_no = new RelativeLayout(context);

        rl_comprise_textview_no.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView txt_no_data = new TextView(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        txt_no_data.setTextSize(TypedValue.COMPLEX_UNIT_DIP, context.getResources().getDimensionPixelSize(R.dimen.TextSize_Center_Remind_ANY));

        txt_no_data.setText(context.getResources().getString(R.string.Title_Notifi_NoHaveWorkDay));
        txt_no_data.setLayoutParams(params);

        rl_comprise_textview_no.addView(txt_no_data);
        Main_Layout_Home_Fragment.addView(rl_comprise_textview_no);

    }


    // Handling for layout if the date selected have any work
    // This method will create a listview
    // This listview will be used to show name, alarm time and type of work in the date selected
    void Setup_isHaveData(final List<GAS_WorkInFormation> works_data_w_day)
    {

        ListView lst_show_view_work = new ListView(context);

        Adapter_Listview_Show_Works_W_Date adapter_listview_show_works_list = new Adapter_Listview_Show_Works_W_Date(context, works_data_w_day);

        lst_show_view_work.setAdapter(adapter_listview_show_works_list);

        Main_Layout_Home_Fragment.addView(lst_show_view_work);

        for (int i = 0; i < works_data_w_day.size(); i++) { Log.d("name work", works_data_w_day.get(i).getName()); }

        lst_show_view_work.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) 
            {

                Log.d("Name Clicked", works_data_w_day.get(i).getName());

                new Dialog_Add_New_Work(context, works_data_w_day.get(i),
                          Days_Count_in_Month[spn_select_day.getSelectedItemPosition()] + "M"
                        + Months_Count_in_Year[spn_select_month.getSelectedItemPosition()] + "Y"
                        + Years_Count[spn_select_year.getSelectedItemPosition()]);

                //new Alarm_Manager(context, i + 1);

            }
        });
        
    }


    List<GAS_WorkInFormation> Get_Work_W_The_Day(String Type)
    {

        if (work_data_manager.Show_All_Work() != null)
        {
            List<GAS_WorkInFormation> All_Works_List_W_Day = work_data_manager.Show_All_Work();

            List<GAS_WorkInFormation> Works_List_W_Fay = new ArrayList<>();

            String Slected_Day = Days_Count_in_Month[spn_select_day.getSelectedItemPosition()] + "M" + Months_Count_in_Year[spn_select_month.getSelectedItemPosition()] + "Y" + Years_Count[spn_select_year.getSelectedItemPosition()];

            Log.d("Select_Daxy", Slected_Day);

            for (int i = 0; i < All_Works_List_W_Day.size(); i++)
            {
                // Loop to Get Works with the day

                GAS_WorkInFormation gas_workInFormation_single = All_Works_List_W_Day.get(i);

                String Get_Date_Work_Data = gas_workInFormation_single.getAlarm().substring(gas_workInFormation_single.getAlarm().indexOf("&") + 1);

                //Log.d("Select_Daxy", Get_Date_Work_Data);

                Log.d("T_Data"    , gas_workInFormation_single.getType());
                Log.d("T_Handling", Type);

//                if (Type.equals(context.getResources().getString(R.string.other_folder_title)))
//                {
//                    if (Get_Date_Work_Data.equals(Slected_Day) && gas_workInFormation_single.getComplete().equals("-1"))
//                    {
//                        if (gas_workInFormation_single.getType().equals(context.getResources().getString(R.string.other_folder_title)) || new Folder_Data_Manager(context).Show_All_Folder().indexOf(gas_workInFormation_single.getType()) == -1)
//                        {
//                            Works_List_W_Fay.add(gas_workInFormation_single);
//                        }
//                    }
//                }else
//                {
//                    if (Get_Date_Work_Data.equals(Slected_Day) && gas_workInFormation_single.getComplete().equals("-1") && gas_workInFormation_single.equals(Type))
//                    {
//                        Works_List_W_Fay.add(gas_workInFormation_single);
//                    }
//                }


                if (Slected_Day.equals(Get_Date_Work_Data) && gas_workInFormation_single.getType().equals(Type) && gas_workInFormation_single.getComplete().equals("-1"))
                {
                    Works_List_W_Fay.add(gas_workInFormation_single);
                }

                List<String> foldername = new ArrayList<>();
                List<GAS_FolderNameInfo> foldeList = new Folder_Data_Manager(context).Show_All_Folder();
                for (int ia = 0; ia < foldeList.size(); ia++) foldername.add(foldeList.get(ia).getFolderName());

                if (Slected_Day.equals(Get_Date_Work_Data)
                        && gas_workInFormation_single.getComplete().equals("-1")
                        && foldername.indexOf(gas_workInFormation_single.getType()) == -1
                        && Type.equals(context.getResources().getString(R.string.other_folder_title)))
                {
                    Works_List_W_Fay.add(gas_workInFormation_single);
                }

            }

            return Works_List_W_Fay;

        } else
        {
            return null;
        }
    }


    public class Dialog_Add_New_Work extends com.example.nickpham.checkittodo.Dialog_Manager.Dialog_Add_New_Work  implements View.OnClickListener
    {

        // Show_Information
        public Dialog_Add_New_Work(Context context, GAS_WorkInFormation gas_work_informarion, String SelectedDate)
        {
            super(context, gas_work_informarion, SelectedDate);
            Connect_Action_L();
        }


        // Create new Work
        public Dialog_Add_New_Work(Context context, int Choice_Add, String SelectedDate)
        {
            super(context, Choice_Add, SelectedDate);
            Connect_Action_L();
        }



        // Gắn sự kiện khi click cho Button (Yes, Cancel)
        public void Connect_Action_L()
        {

            View.OnClickListener lis = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.Btn_Done_Dialog_Add_Work:

                            // Nếu work_data_w_day == null có nghĩa là người dùng muốn thêm công việc mới
                            // Sau đó ta sẽ gọi hàm;lp[ Add_New_Work() để thêm công việc mới vào Database cho người dùng
                            // Nếu work_data_w_day != null có nghĩa là người dùng muốn sửa đổi thông tin của công việc đã chọn
                            if (work_data_w_day == null)
                            {

                                Add_New_Work();

                            } else
                            {

                                Update_Work();

                            }

                            Handling_Data_Works();

                            break;

                        case R.id.Btn_Delete_This_Work:

                            // Nếu work_data_w_day != null có nghĩa là người dùng muốn xoá công việc đã chọn
                            // Nếu work_data_w_day == null có nghĩa là người dùng muốn thoát ra dialog thêm công việc
                            Log.d("Action_Dialog_New_Work", "Delete Work");

                            if (work_data_w_day != null)
                            {
                                Delete_Work();
                                Handling_Data_Works();
                            }

                            Dialog_This.dismiss();

                            break;
                    }
                }
            };

            imv_done_dialog     .setOnClickListener(lis);
            imv_delete_this_work.setOnClickListener(lis);
        }

    }



    /**

     Custom Adapter for lisview
     this listview for showtion work with date selection

     */


    public class Adapter_Listview_Show_Works_W_Date extends BaseAdapter
    {
        Context context;
        List<GAS_WorkInFormation> gas_workInFormationList;

        public Adapter_Listview_Show_Works_W_Date(Context context, List<GAS_WorkInFormation> gas_workInFormationList)
        {

            this.context                 = context;
            this.gas_workInFormationList = gas_workInFormationList;

        }

        public class ViewHolder
        {

            CheckBox cb_show_complete;
            TextView show_work_name, show_work_time;
            RelativeLayout rlv_show_priority;

        }

        @Override
        public int getCount() {
            return gas_workInFormationList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup)
        {

            View viewRop = view;

            ViewHolder holder = null;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            if (view == null)
            {
                viewRop = inflater.inflate(R.layout.adapter_works_list, viewGroup, false);

                holder = new ViewHolder();

                holder.cb_show_complete  = (CheckBox)       viewRop.findViewById(R.id.Show_Work_Complte_ListView_Adapter);
                holder.show_work_name    = (TextView)       viewRop.findViewById(R.id.Show_Work_Name_ListView_Adapter);
                holder.show_work_time    = (TextView)       viewRop.findViewById(R.id.Show_Work_Time_ListView_Adapter);
                holder.rlv_show_priority = (RelativeLayout) viewRop.findViewById(R.id.Show_Priority_Work_ListView_Adapter);

                viewRop.setTag(holder);
            }

            holder = (ViewHolder) viewRop.getTag();

            // Set Work Name
            holder.show_work_name.setText(gas_workInFormationList.get(position).getName());

            // Set work alarm
            holder.show_work_time.setText(new GAS_CoverString_Time().Get_Time(gas_workInFormationList.get(position).getAlarm()));


            if(Integer.parseInt(gas_workInFormationList.get(position).getComplete()) != -1)
            {
                holder.cb_show_complete.setChecked(true);
            } else
            {
                holder.cb_show_complete.setChecked(false);
            }


            // Handling to set fot CheckBox show work priority
            Handling_Set_Show_Priority(holder.rlv_show_priority, position);

            // Handling to set action show complete
            Handling_S_Action_CheckBox_Show_Complete(holder.cb_show_complete, position);

            notifyDataSetChanged();

            return viewRop;

        }

        void Handling_Set_Show_Priority(RelativeLayout rlv_priority_content, int position)
        {
            String Priority = gas_workInFormationList.get(position).getPriority();

            // Prioritys = new String[]{"Không quan trọng", "Cũng quan trọng", "Rất quan trọng"};

            if (Priority.equals("Không quan trọng")) { ((GradientDrawable)rlv_priority_content.getBackground()).setColor((Color.YELLOW));}
            if (Priority.equals("Cũng quan trọng"))  { ((GradientDrawable)rlv_priority_content.getBackground()).setColor((context.getResources().getColor(android.R.color.holo_orange_light)));}
            if (Priority.equals("Rất quan trọng"))   { ((GradientDrawable)rlv_priority_content.getBackground()).setColor((Color.RED));}
        }


        // Action CheckBox Complete
        // Because this listview show at home fragment so this checkbox only  checked method
        // Checked method will --> Update_Work() --> Handling and turn off alarm with work's id selected
        void Handling_S_Action_CheckBox_Show_Complete(CheckBox checkBox_complete_content, final int position)
        {
            checkBox_complete_content.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    Update_Work(position, (CheckBox) compoundButton);

                    Handling_Data_Works();
                }
            });

        }

        // Update Work and Cacel alarm with work's Id
        void Update_Work(int position, CheckBox cb_complete_work)
        {
            GAS_WorkInFormation gas_workInFormation = gas_workInFormationList.get(position);

            if (cb_complete_work.isChecked() == false) { gas_workInFormation.setComplete("-1"); } else { gas_workInFormation.setComplete("1"); }

            boolean Update_Result = new Work_Data_Manager(context, "").Update_Folder(gas_workInFormation.getName(), gas_workInFormation);


            if (Update_Result)
            {

                if (gas_workInFormation.getAlarm().isEmpty() == false)
                {

                    new Alarm_Manager(context, null, Integer.parseInt(gas_workInFormation.getId())).Delete_Alarm();

                }

            }

        }
    }



    public class connectMethod implements View.OnClickListener
    {

        public connectMethod(View view)
        {
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            switch (view.getId()) {

                case R.id.Flt_Add_New_Work:

                    new Dialog_Add_New_Work(
                            context, Choice
                            , Days_Count_in_Month[spn_select_day.getSelectedItemPosition()] + "M"
                            + Months_Count_in_Year[spn_select_month.getSelectedItemPosition()] + "Y"
                            + Years_Count[spn_select_year.getSelectedItemPosition()]
                    );

                    break;

                case R.id.Flt_Remove_Work_All_Home_Frgament:

                    if (isHaveData_w_day)
                    {
                        DL_Remind_W_Remove_All_Work().show();
                    } else
                    {
                        Toast.makeText(context, "Bạn không có công việc nào trong ngày hôm này để xoá cả!", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.Btn_NextDay:

                    String[] Bundler_Time_Next = Handling_Next_Day(
                            Days_Count_in_Month[spn_select_day.getSelectedItemPosition()],//((TextView)spn_select_day.getSelectedView()).getText().toString(),
                            Months_Count_in_Year[spn_select_month.getSelectedItemPosition()],
                            Years_Count[spn_select_year.getSelectedItemPosition()]);

                    Custom_Time_Picker(Integer.parseInt(Bundler_Time_Next[0]), Integer.parseInt(Bundler_Time_Next[1]), Integer.parseInt(Bundler_Time_Next[2]));


                    break;

                case R.id.Btn_PreviousDay:

                    String[] Bundler_Time_Previous = Handling_Previous_Day(
                            ((TextView) spn_select_day.getSelectedView())  .getText().toString(),
                            ((TextView) spn_select_month.getSelectedView()).getText().toString(),
                            ((TextView) spn_select_year.getSelectedView()) .getText().toString());

                    Custom_Time_Picker(Integer.parseInt(Bundler_Time_Previous[0]), Integer.parseInt(Bundler_Time_Previous[1]), Integer.parseInt(Bundler_Time_Previous[2]));


                    break;

                case R.id.btn_today_home:

                    Calendar Today_Info = Calendar.getInstance();

                    Custom_Time_Picker(Today_Info.get(Calendar.DAY_OF_MONTH), Today_Info.get(Calendar.MONTH) + 1, Today_Info.get(Calendar.YEAR));

                    break;


            }

        }

        // This dialog will show to remind user while user press remove floatingbutton
        Dialog DL_Remind_W_Remove_All_Work()
        {
            final String Slected_Day = Days_Count_in_Month[spn_select_day.getSelectedItemPosition()] + "M" + Months_Count_in_Year[spn_select_month.getSelectedItemPosition()] + "Y" + Years_Count[spn_select_year.getSelectedItemPosition()];

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Warring")
                    .setMessage("Bạn có chắc chắn là xoá hết công việc trong ngày hôm nay?")
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {

                            dialogInterface.dismiss();

                        }
                    })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            new Work_Data_Manager(context, "").Delete_All_Work(Slected_Day);
                            Handling_Data_Works();
                            dialogInterface.dismiss();

                        }
                    });

            return builder.create();
        }
    }

}


