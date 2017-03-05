package com.example.nickpham.checkittodo.GAS;

import android.util.Log;

import com.example.nickpham.checkittodo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nickpham on 31/12/2016.
 */

public class GAS_CoverString_Time
{

    public int[]    text_uri = {R.string.Monday, R.string.TuesDay, R.string.WebnesDay, R.string.ThursDay, R.string.FriDay, R.string.Saturday, R.string.Sunday};
    public String[] text_com = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public String Get_Time_Work(String Text_G_Form)
    {

        String Time_Form_Text = Text_G_Form;

        Time_Form_Text = "" + Time_Form_Text.replace(" ", "&");

        int So_Dem = 0;
        for (int i = Time_Form_Text.toCharArray().length - 1; i >= 0; i--)
        {

            if (String.valueOf(Time_Form_Text.toCharArray()[i]).equals("/"))
            {

                Log.d("Test", "/");

                if (So_Dem == 0)
                {

                    char[] characters_f = Time_Form_Text.toCharArray();

                    characters_f[i] = 'Y';

                    Time_Form_Text = String.copyValueOf(characters_f);

                    So_Dem ++;

                } else
                {

                    char[] characters_f = Time_Form_Text.toCharArray();

                    characters_f[i] = 'M';

                    Time_Form_Text = String.copyValueOf(characters_f);

                }

            }

        }

        return Time_Form_Text;

    }


    public String Get_Time(String Text_G_DataBase)
    {
        return Text_G_DataBase.substring(0, Text_G_DataBase.indexOf("&"));
    }

    public int[] Get_Time_info(String Date_W)
    {

        if (Date_W.indexOf("&") != -1)
        {

            return new int[]
                    {
                            Integer.parseInt(Date_W.substring(0, Date_W.indexOf("h"))),
                            Integer.parseInt(Date_W.substring(Date_W.indexOf("h") + 1, Date_W.indexOf("&"))),
                    };

        }else
        {

            return new int[]
                    {
                            Integer.parseInt(Date_W.substring(0, Date_W.indexOf("h"))),
                            Integer.parseInt(Date_W.substring(Date_W.indexOf("h") + 1, Date_W.indexOf("CUR"))),
                    };

        }

    }


    public List<String> GetCurrentWeek(String Time)
    {

        List<String> CurrentTime = new ArrayList<>();


        int Count_Current = Count_Text_S(Time, "%");

        Log.d("CountCurrent", String.valueOf(Count_Current));

        if (Count_Current > 0){

            char[] time_array = Time.substring(Time.indexOf("R") + 1).toCharArray();

            int so_dem = 0;

            for (int i = 0; i < Count_Current; i++)
            {

                String s_d = "";


                while (so_dem < time_array.length && String.valueOf(time_array[so_dem]).equals("%") == false)
                {

                    s_d = s_d + "" + String.valueOf(time_array[so_dem]);

                    so_dem ++;
                }

                CurrentTime.add(s_d);

                so_dem += 1;
            }

        }

        return CurrentTime;

    }

    int Count_Text_S(String array, String text)
    {

        int Result = 0;

        for (int i = 0; i < array.toCharArray().length; i++)
        {
            if (String.valueOf(array.toCharArray()[i]).equals(text))
            {
                Result += 1;
            }
        }

        return Result;
    }

    public String Get_Time_Work_Form(String Time_Work)
    {

        Time_Work = "" + Time_Work.replace("&", " ");
        Time_Work = "" + Time_Work.replace("M", "/");
        Time_Work = "" + Time_Work.replace("Y", "/");

        return Time_Work;

    }

    public int[] Get_Date_Info(String Date_W)
    {

        return new int[]
                {
                        Integer.parseInt(Date_W.substring(Date_W.indexOf("&") + 1, Date_W.indexOf("M"))),
                        Integer.parseInt(Date_W.substring(Date_W.indexOf("M") + 1, Date_W.indexOf("Y"))),
                        Integer.parseInt(Date_W.substring(Date_W.indexOf("Y") + 1)),
                };

    }


    public boolean Check_Valid_Time(String Time)
    {

        if (Time.indexOf(" ") != -1) { Time = Get_Time_Work(Time); }

        boolean Result = true;

        int Year_C   = Get_Date_Info(Time)[2];
        int Month_C  = Get_Date_Info(Time)[1];
        int Day_C    = Get_Date_Info(Time)[0];

        Calendar calendar_now = Calendar.getInstance();

        int Year_N  = calendar_now.get(Calendar.YEAR);
        int Month_N = calendar_now.get(Calendar.MONTH) + 1;
        int Day_N   = calendar_now.get(Calendar.DAY_OF_MONTH);

        if (Year_C == Year_N)
        {

            if (Month_C == Month_N)
            {
                if (Day_C == Day_N)
                {

                    int Hour_C   = Get_Time_info(Time)[0];
                    int Minute_C = Get_Time_info(Time)[1];

                    int Hour_N   = calendar_now.get(Calendar.HOUR_OF_DAY);
                    int Minute_N = calendar_now.get(Calendar.MINUTE);

                    if(Hour_C == Hour_N && Minute_C <= Minute_N || Hour_C < Hour_N)
                    {
                        Result = false;
                    }

                }else if (Day_C < Day_N)
                {
                    Result = false;
                }
            }else if (Month_C < Month_N)
            {
                Result = false;
            }

        }else if (Year_C < Year_N)
        {
            Result = false;
        }

        return Result;

    }


}
