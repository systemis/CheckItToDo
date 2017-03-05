package com.example.nickpham.checkittodo.DataManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nickpham.checkittodo.GAS.GAS_AccountInfo;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;
import com.example.nickpham.checkittodo.MainActivity;
import com.example.nickpham.checkittodo.Service.Alarm.Alarm_Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickpham on 30/12/2016.
 */

public class Work_Data_Manager extends SQLiteOpenHelper
{

    Context context;

    public static String DB_TableName;
    public static final String DB_ID           = "DB_Work_ID";
    public static final String DB_WorkName     = "DB_WorkName";
    public static final String DB_WorkAlarm    = "DB_WorkAlarmTime";
    public static final String DB_WorkLocate   = "DB_WorkLocate";
    public static final String DB_WorkPriority = "DB_WorkPriority";
    public static final String DB_WorkNote     = "DB_WorkNote";
    public static final String DB_WorkType     = "DB_WorkType";
    public static final String DB_CurrentTWeek = "DB_CurrentTimeWeek";
    public static final String DB_Complete     = "DB_WorkComplete";
    public static final String DB_AlarmSound   = "DB_WorkAlarmSound";


    public Work_Data_Manager(Context context, String TBName)
    {
        super(context, "Work_Data_Manager", null, 1);
        this.context      = context;

        this.DB_TableName = "TableWork_" + MainActivity.Account_Bundle[0];;

        if (TBName.isEmpty() == false)
        {
            this.DB_TableName = "TableWork_" + TBName;
        }

        Log.d("DB_TableName", this.DB_TableName);

        onCreate(getWritableDatabase());
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        //primary key   AUTOINCREMENT

        String sql = "Create table if not exists " + DB_TableName + " ( "
                + DB_ID           + " text primary key NOT NULL, "
                + DB_WorkName     + " text, "
                + DB_WorkAlarm    + " text, "
                + DB_WorkLocate   + " text, "
                + DB_WorkPriority + " text, "
                + DB_WorkNote     + " text, "
                + DB_WorkType     + " text, "
                + DB_CurrentTWeek + " text, "
                + DB_Complete     + " text, "
                + DB_AlarmSound   + " text" + ")";

        sqLiteDatabase.execSQL(sql);

    }

    public boolean Add_New_Work(GAS_WorkInFormation gas_workInFormation)
    {

        if (Check_Time_List(gas_workInFormation))
        {

            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(DB_ID          ,gas_workInFormation.getId());
            values.put(DB_WorkName    ,gas_workInFormation.getName());
            values.put(DB_WorkAlarm   ,gas_workInFormation.getAlarm());
            values.put(DB_WorkLocate  ,gas_workInFormation.getLocation());
            values.put(DB_WorkPriority,gas_workInFormation.getPriority());
            values.put(DB_WorkNote    ,gas_workInFormation.getNote());
            values.put(DB_WorkType    ,gas_workInFormation.getType());
            values.put(DB_CurrentTWeek,gas_workInFormation.getCurrentTimeWeek());
            values.put(DB_Complete    ,gas_workInFormation.getComplete());
            values.put(DB_AlarmSound  ,gas_workInFormation.getSoundAlarm());

            return db.insert(DB_TableName, null, values) != -1;

        }else
        {
            return false;
        }

    }


    public List<GAS_WorkInFormation> Show_All_Work()
    {
        SQLiteDatabase db = getWritableDatabase();

        //db.execSQL(sql_(this.DB_TableName));

        String sql = "Select * from " + DB_TableName;

        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {

            List<GAS_WorkInFormation> Data = new ArrayList<GAS_WorkInFormation>();

            do {

                GAS_WorkInFormation ga = new GAS_WorkInFormation();

                ga.setId(c.getString(c.getColumnIndex(DB_ID)));
                ga.setName(c.getString(c.getColumnIndex(DB_WorkName)));
                ga.setAlarm(c.getString(c.getColumnIndex(DB_WorkAlarm)));
                ga.setLocation(c.getString(c.getColumnIndex(DB_WorkLocate)));
                ga.setPriority(c.getString(c.getColumnIndex(DB_WorkPriority)));
                ga.setNote(c.getString(c.getColumnIndex(DB_WorkNote)));
                ga.setType(c.getString(c.getColumnIndex(DB_WorkType)));
                ga.setCurrentTimeWeek(c.getString(c.getColumnIndex(DB_CurrentTWeek)));
                ga.setComplete(c.getString(c.getColumnIndex(DB_Complete)));
                ga.setSoundAlarm(c.getString(c.getColumnIndex(DB_AlarmSound)));

                Data.add(ga);

                ga = null;

            } while (c.moveToNext());

            return Data;
        }else {

            return null;

        }

    }

    public boolean Update_Folder(String Work_ID, GAS_WorkInFormation gas_workInFormation)
    {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DB_ID          ,gas_workInFormation.getId());
        values.put(DB_WorkName    ,gas_workInFormation.getName());
        values.put(DB_WorkAlarm   ,gas_workInFormation.getAlarm());
        values.put(DB_WorkLocate  ,gas_workInFormation.getLocation());
        values.put(DB_WorkPriority,gas_workInFormation.getPriority());
        values.put(DB_WorkNote    ,gas_workInFormation.getNote());
        values.put(DB_WorkType    ,gas_workInFormation.getType());
        values.put(DB_CurrentTWeek,gas_workInFormation.getCurrentTimeWeek());
        values.put(DB_Complete    ,gas_workInFormation.getComplete());
        values.put(DB_AlarmSound  ,gas_workInFormation.getSoundAlarm());

        return db.update(DB_TableName, values, DB_ID + "=?", new String[]{Work_ID}) != -1;

    }


    public void Delete_Work_F_Data(String Work_Id)
    {

        Log.d("Action", "Delete--------------->App");

        SQLiteDatabase db = getWritableDatabase();

        //db.execSQL(sql_(this.DB_TableName));

        if (Show_All_Work() != null)
        {
            GAS_WorkInFormation work_delete = Show_All_Work().get(Get_Position_With_Id(Work_Id));

            if (work_delete.getComplete() != null && work_delete.getAlarm().isEmpty() == false && work_delete.getComplete() == "-1" || work_delete.getType().equals("Usually"))
            {
                new Alarm_Manager(context, null, Integer.parseInt(Work_Id)).Delete_Alarm();
            }
        }

        db.delete(DB_TableName, DB_ID + "=?", new String[]{Work_Id});
    }


    boolean Check_Time_List(GAS_WorkInFormation gas_workInFormation)
    {
        boolean Result = true;

        if (Show_All_Work() != null)
        {
            List<GAS_WorkInFormation> gas_workInFormationList = Show_All_Work();
            for (int i = 0; i < gas_workInFormationList.size(); i++)
            {
                if (gas_workInFormationList.get(i).getAlarm().equals(gas_workInFormation.getAlarm()) && gas_workInFormationList.get(i).getName().equals(gas_workInFormation.getName()))
                {
                    Result = false;
                }
            }
        }

        return Result;

    }


    // This method will remove all work from database with 2 choice
    // Choice 1: Delete All Work
    // Choice 2: Delete All work is working in the selected date
    public void Delete_All_Work(String Date)
    {
        List<GAS_WorkInFormation> workInfo_list = Show_All_Work();

        if (workInfo_list != null)
        {

            for (int i = 0; i < workInfo_list.size(); i++)
            {

                if (Date.isEmpty()) // Delete all data at all notes fragment
                {
                    Log.d("DataManager", "Delete Work");

                    Delete_Work_F_Data(workInfo_list.get(i).getId());

                } else if(Date.isEmpty() == false && Date.indexOf("Usually") == -1) // Delete with date at home screen fragment
                {

                    String Date_Work = workInfo_list.get(i).getAlarm().substring(workInfo_list.get(i).getAlarm().indexOf("&") + 1);

                    if (Date_Work.equals(Date))
                    {

                        new Alarm_Manager(context, null, Integer.parseInt(workInfo_list.get(i).getId())).Delete_Alarm();

                        Delete_Work_F_Data(workInfo_list.get(i).getId());

                    }

                } else if (Date.indexOf("Usually") != -1)
                {

                    if (workInfo_list.get(i).getType().equals("Usually"))
                    {

                        Delete_Work_F_Data(workInfo_list.get(i).getId());

                    }

                }

            }

        }

    }



    // This method will handling to get work user want to add will be comprise what id
    public int Get_Id_Work_To_Add() {
        int Result = 1;

        // Actually
        if (MainActivity.Account_Bundle[0].isEmpty() == false) {
            int PositionAccountName = -1;
            List<GAS_AccountInfo> account_list = new Account_Data_Manager(context).Show_All_Account();
            if (account_list != null) {
                for (int i = 0; i < account_list.size(); i++) {
                    if (MainActivity.Account_Bundle[0].equals(account_list.get(i).getAccount())) {
                        PositionAccountName = i + 2;
                    }
                }
            }

            if (PositionAccountName != -1) {
                Result = Integer.parseInt(String.valueOf(PositionAccountName));
            }
        } else {
            Result = 1;
        }

        List<GAS_WorkInFormation> gas_workInFormationList = Show_All_Work();

        if (gas_workInFormationList != null)
        {
            int position_item    = 0;
            boolean Result_Check = true;

            for (int i = 0; i < gas_workInFormationList.size(); i++)
            {
                if (gas_workInFormationList.get(i).getId().equals(String.valueOf(Result) + "" + String.valueOf(i + 1)) == false)
                {
                    if (Check_Have_Id_Item(gas_workInFormationList, String.valueOf(i+ 1)))
                    {
                        Result_Check  = false;

                        position_item = i + 1;

                        Log.d("Check", String.valueOf(position_item));
                    }
                }
            }

            if (Result_Check)
            {
                position_item = gas_workInFormationList.size() + 1;
            }

            Result = Integer.parseInt(String.valueOf(Result) + "" + String.valueOf(position_item));

        } else{
            Result = Integer.parseInt(String.valueOf(Result) + "" + String.valueOf(1));
        }

        return Result;

    }


    public boolean Check_Have_Id_Item(List<GAS_WorkInFormation> workInFormations_w_check, String Id)
    {

        boolean result = true;


        for (int i = 0; i < workInFormations_w_check.size(); i++)
        {
            if (workInFormations_w_check.get(i).getId().equals(Id))
            {
                result = false;

            }
        }

        return result;

    }


    int Get_Position_With_Id(String id)
    {

        if (Show_All_Work()!= null && Show_All_Work().size() > 0)
        {

            for (int i = 0; i < Show_All_Work().size(); i++)
            {

                if (Show_All_Work().get(i).equals(id))
                {
                    return i;
                }

            }

        }

        return 0;
    }

    public void DeleteTable()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ DB_TableName);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
