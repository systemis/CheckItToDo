package com.example.nickpham.checkittodo.DataManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nickpham.checkittodo.GAS.GAS_AccountInfo;
import com.example.nickpham.checkittodo.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickpham on 07/01/2017.
 */

public class Account_Data_Manager extends SQLiteOpenHelper
{

    public static final String DB_TableName       = "DB_AccountStronge";
    public static final String DB_AccountName     = "DB_AccountName";
    public static final String DB_AccountPassWord = "DB_AccountPassWord";

    Context context;

    public Account_Data_Manager(Context context)
    {
        super(context, "AccountDataManager", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        String sql = "Create table if not exists " + DB_TableName + " ( "
                + DB_AccountName     + " text primary key, "
                + DB_AccountPassWord + " text" + ")";

        sqLiteDatabase.execSQL(sql);

    }


    public List<GAS_AccountInfo> Show_All_Account()
    {

        SQLiteDatabase db = getWritableDatabase();

        String sql = "Select * from " + DB_TableName;

        Cursor c   = db.rawQuery(sql, null);

        if (c.moveToNext())
        {

            List<GAS_AccountInfo> account_list = new ArrayList<>();

            do
            {

                GAS_AccountInfo accountInfo = new GAS_AccountInfo();

                accountInfo.setAccount (c.getString(c.getColumnIndex(DB_AccountName)));
                accountInfo.setPassWord(c.getString(c.getColumnIndex(DB_AccountPassWord)));

                account_list.add(accountInfo);

            }while(c.moveToNext());

            return account_list;

        }else
        {
            return null;
        }

    }


    public boolean isAddNewAccount(String AccountName, String AccountPassWord)
    {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DB_AccountName    , AccountName);
        values.put(DB_AccountPassWord, AccountPassWord);

        return db.insert(DB_TableName, null, values) != -1;

    }


    public boolean isUpdateAccount(String AccountName, GAS_AccountInfo accountInfo)
    {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DB_AccountName    , accountInfo.getAccount());
        values.put(DB_AccountPassWord, accountInfo.getPassWord());

        return db.update(DB_TableName, values, DB_AccountName + "=?", new String[]{AccountName}) != -1;

    }


    public void DeleteAccount(String AccountName)
    {
        SQLiteDatabase db = getWritableDatabase();

        MainActivity.Account_Bundle[0] = "";

        db.delete(DB_TableName, DB_AccountName + "=?", new String[]{AccountName});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
