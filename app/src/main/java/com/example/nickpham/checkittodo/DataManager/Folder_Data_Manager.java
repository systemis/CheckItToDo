package com.example.nickpham.checkittodo.DataManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nickpham.checkittodo.GAS.GAS_FolderNameInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickpham on 30/12/2016.
 */

public class Folder_Data_Manager extends SQLiteOpenHelper {

    Context context;

    public static final String DB_TableName      = "DB_FolderManagerData";
    public static final String DB_FolderName     = "DB_FolderName";
    public static final String DB_FolderColorURI = "DB_FolderColorURI";

    public Folder_Data_Manager(Context context) {

        super(context, "Folder_Manager", null, 1);

        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "Create table if not exists " + DB_TableName + " ( "
                + DB_FolderName     + " text primary key, "
                + DB_FolderColorURI + " text"               + ")";

        sqLiteDatabase.execSQL(sql);

    }

    public boolean Add_New_Folder(String Name, String ColorURi) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DB_FolderName    , Name);
        values.put(DB_FolderColorURI, ColorURi);

        return db.insert(DB_TableName, null, values) != -1;

    }

    public List<GAS_FolderNameInfo> Show_All_Folder() {

        SQLiteDatabase db = getWritableDatabase();

        String sql = "Select * from " + DB_TableName;

        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {

            List<GAS_FolderNameInfo> Data = new ArrayList<GAS_FolderNameInfo>();

            do {

                GAS_FolderNameInfo folderNameInfo = new GAS_FolderNameInfo();

                folderNameInfo.setFolderName(c.getString(c.getColumnIndex(DB_FolderName)));
                folderNameInfo.setColorURI  (c.getString(c.getColumnIndex(DB_FolderColorURI)));

                Data.add(folderNameInfo);

            } while (c.moveToNext());

            return Data;
        }

        return null;

    }

    public void Delete_Folder(String Name)
    {

        SQLiteDatabase db = getWritableDatabase();

        db.delete(DB_TableName, DB_FolderName + "=?", new String[]{Name});

    }


    public boolean Update_Folder(String Name, GAS_FolderNameInfo folderNameInfo)
    {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DB_FolderName    , folderNameInfo.getFolderName());
        values.put(DB_FolderColorURI, folderNameInfo.getColorURI());

        return  db.update(DB_TableName, values, DB_FolderName + "=?", new String[]{ Name }) != -1;

    }





    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
