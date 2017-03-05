package com.example.nickpham.checkittodo.Dialog_Manager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nickpham.checkittodo.GAS.GAS_CoverString_Time;
import com.example.nickpham.checkittodo.R;

import java.util.Calendar;


/**
 * Created by nickpham on 02/01/2017.
 */

public class Dialog_Select_Time
{

    public Context context;
    public String Selected_Date_Time;

    public Dialog dialog_this;

    public Spinner spn_select_hour, spn_select_minute;
    public DatePicker dp_select_date;


    public Dialog_Select_Time(Context context, String Selected_Date_Time)
    {

        this.context = context;
        this.Selected_Date_Time = Selected_Date_Time;

    }

    public void Create_Dialog()
    {

        dialog_this = new Dialog(context);
        dialog_this.setContentView(R.layout.dialog_selected_time);

        dialog_this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        spn_select_hour   = (Spinner)    dialog_this.findViewById(R.id.SPN_Select_Hour);
        spn_select_minute = (Spinner)    dialog_this.findViewById(R.id.SPN_select_Minute);
        dp_select_date    = (DatePicker) dialog_this.findViewById(R.id.DP_SelectDate);

        Setup_SPN_This();

    }


    public String[] hour_array = new String[24], minute_array = new String[60];
    public void Setup_SPN_This()
    {

        spn_select_hour  .setTag("Hour");
        spn_select_minute.setTag("Minute");

        for (int i = 0; i < minute_array.length; i++)
        {
            if (i < hour_array.length)
            {
                if (i < 9) {
                    hour_array[i] = "0" + String.valueOf(i + 1);
                }else {
                    hour_array[i] = String.valueOf(i + 1);
                }
            }

            if (i < 10) minute_array[i] = "0" + String.valueOf(i); else {minute_array[i] = String.valueOf(i); }

        }


        spn_select_hour  .setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, hour_array));
        spn_select_minute.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, minute_array));


        Set_Default_Value_Time_Form();

    }


    void Set_Default_Value_Time_Form()
    {

        int Hour_T_S   = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int Minute_T_S = Calendar.getInstance().get(Calendar.MINUTE);


        if (Minute_T_S == 59)
        {

            Minute_T_S = 0;
            if (Hour_T_S == 24)
            {

                Hour_T_S   = 0;

                dp_select_date.init(Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1,
                        new DatePicker.OnDateChangedListener() {

                            @Override
                            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2)
                            {

                            }
                        });
            }else
            {

                Hour_T_S += 1;

            }

        }else
        {

            Minute_T_S += 1;

        }


        spn_select_hour  .setSelection(Get_Position_SP_W_Value(hour_array  , String.valueOf(Hour_T_S)));
        spn_select_minute.setSelection(Get_Position_SP_W_Value(minute_array, String.valueOf(Minute_T_S)));

    }


    int Get_Position_SP_W_Value(String[] arrays_check, String Text)
    {

        if (Text.length() < 2)
        {
            Text = "0" + Text;
        }

        for (int i = 0; i < arrays_check.length; i++)
        {
            if (arrays_check[i].equals(Text))
            {
                return i;
            }
        }

        return 0;

    }

    // Example 9h30&2M2Y

    public int[] Get_Date_Info(String Date_W)
    {

        return new int[]
                {
                        Integer.parseInt(Date_W.substring(Date_W.indexOf("&") + 1, Date_W.indexOf("M"))),
                        Integer.parseInt(Date_W.substring(Date_W.indexOf("M") + 1, Date_W.indexOf("Y"))),
                        Integer.parseInt(Date_W.substring(Date_W.indexOf("Y") + 1)),
                };

    }
}
