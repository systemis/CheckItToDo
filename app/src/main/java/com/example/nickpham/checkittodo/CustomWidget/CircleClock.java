package com.example.nickpham.checkittodo.CustomWidget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nickpham.checkittodo.GAS.GAS_CoverString_Time;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;
import com.example.nickpham.checkittodo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickpham on 05/01/2017.
 */


// This class use to show time of works in usually fragment screen
//
public class CircleClock
{
    Context context;


    LinearLayout circle_clock_face;
    TextView Show_Work_Title, Show_Work_Hour, Show_Work_Minute, Show_Work_Time_Week;

    GAS_WorkInFormation usually_work_info;

    public CircleClock(Context context, GAS_WorkInFormation usually_work_info)
    {

        this.context           = context;
        this.usually_work_info = usually_work_info;

    }


    public View Create_Circle_Clock()
    {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.tesing_circle_clock_face, null, false);

        Anhxa(view);

        //Custom_ClockFace_UI();

        return view;

    }


    void Custom_ClockFace_UI()
    {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(circle_clock_face.getWidth(), circle_clock_face.getHeight());

        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        circle_clock_face.setLayoutParams(params);

    }

    public void Anhxa(View view)
    {

        circle_clock_face            = (LinearLayout) view.findViewById(R.id.Circle_Clock_Face);
        Show_Work_Title              = (TextView)     view.findViewById(R.id.Show_Work_Name_Usually);
        Show_Work_Hour               = (TextView)     view.findViewById(R.id.Show_Work_Hour);
        Show_Work_Minute             = (TextView)     view.findViewById(R.id.Show_Work_Minute);
        Show_Work_Time_Week          = (TextView)     view.findViewById(R.id.Show_Work_Time_Week);

        SetValueThis();

    }

    public void SetValueThis()
    {

        Handling_To_Set_Text_Show_Work_Name();

        Handling_To_Set_Text_View_Show_Time();

        Handling_To_Set_Current_inWeek();

    }

    void Handling_To_Set_Text_Show_Work_Name()
    {

        Show_Work_Title.setText(usually_work_info.getName());

        if (usually_work_info.getName().length() > 12)
        {
            Show_Work_Title.setText(usually_work_info.getName().substring(0, 12) + "...");
        }

    }

    void Handling_To_Set_Text_View_Show_Time()
    {
        String Hour_Work_Selected   = String.valueOf(new GAS_CoverString_Time().Get_Time_info(usually_work_info.getAlarm())[0]);

        String Minute_Work_Selected = String.valueOf(new GAS_CoverString_Time().Get_Time_info(usually_work_info.getAlarm())[1]);

        if (Hour_Work_Selected  .length() < 2) Hour_Work_Selected   = "0" + Hour_Work_Selected;
        if (Minute_Work_Selected.length() < 2) Minute_Work_Selected = "0" + Minute_Work_Selected;

        Show_Work_Hour  .setText(Hour_Work_Selected);
        Show_Work_Minute.setText(Minute_Work_Selected);
    }


    void Handling_To_Set_Current_inWeek()
    {

        List<String> Current_Day = new GAS_CoverString_Time().GetCurrentWeek(usually_work_info.getAlarm());

        Log.d("SizeH", String.valueOf(Current_Day.size()));

        if (Current_Day.size() < 3)
        {

            List<String> hm_current = Current_Day_List_get(Current_Day);

            for (int i = 0; i < hm_current.size(); i++)
            {
                if (i < hm_current.size())
                {
                    Show_Work_Time_Week.setText(Show_Work_Time_Week.getText().toString() + hm_current.get(i) + ", ");
                }

                if (i == hm_current.size() - 1)
                {
                    Show_Work_Time_Week.setText(Show_Work_Time_Week.getText().toString() + "Weekly");
                }
            }

        }else
        {

            Show_Work_Time_Week.setText(String.valueOf(Current_Day.size()) + " ngày trong tuần.");

        }

    }

    List<String> Current_Day_List_get(List<String> Current_Day_Com_List)
    {

        List<String> Result = new ArrayList<>();

        for (int i = 0; i < Current_Day_Com_List.size(); i++)
        {

            Result.add(context.getResources().getString(new GAS_CoverString_Time().text_uri[Position_Item_Time_Indext(new GAS_CoverString_Time().text_com, Current_Day_Com_List.get(i))]));

        }

        return Result;

    }


    int Position_Item_Time_Indext(String[] array_w_c, String text) {

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

        if (isHave == false)
        {
             result = -1;
        }

        return result;

    }


}
