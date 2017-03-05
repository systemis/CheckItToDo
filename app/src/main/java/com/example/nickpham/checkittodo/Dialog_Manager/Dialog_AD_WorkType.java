package com.example.nickpham.checkittodo.Dialog_Manager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nickpham.checkittodo.DataManager.Folder_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_FolderNameInfo;
import com.example.nickpham.checkittodo.R;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by nickpham on 07/01/2017.
 */

public class Dialog_AD_WorkType implements View.OnClickListener
{

    public Context context;

    public GAS_FolderNameInfo folder_get;

    public Dialog dialog_this;


    public LinearLayout     ln_main_layout_this;
    public EditText  ip_work_type_name;
    public Button    btn_add_work_type, btn_exit_dialog_this;
    public ImageView imv_delete_work_type_select;

    public Dialog_AD_WorkType(Context context)
    {
        this.context = context;

        CreateDialog();

        imv_delete_work_type_select.setVisibility(View.GONE);

    }

    public Dialog_AD_WorkType(Context context, GAS_FolderNameInfo folder_get)
    {
        this.context    = context;
        this.folder_get = folder_get;
        CreateDialog();

        btn_add_work_type.setText("Update");

        SetValue();
    }


    void SetValue()
    {

        ip_work_type_name.setText(folder_get.getFolderName());
    }

    public void CreateDialog()
    {

        dialog_this = new Dialog(context);

        dialog_this.setContentView(R.layout.dialog_ap_work_type);

        AnhxaDialog();

        dialog_this.show();
    }


    void AnhxaDialog()
    {

        ln_main_layout_this         = (LinearLayout) dialog_this.findViewById(R.id.Layout_Main_Dialog_AD_Account);
        ip_work_type_name           = (EditText) dialog_this.findViewById(R.id.Import_Type_Name_dialog);
        btn_add_work_type           = (Button) dialog_this.findViewById(R.id.Btn_Add_Type_AU_Dialog);
        btn_exit_dialog_this        = (Button) dialog_this.findViewById(R.id.Btn_Exit_AU_Dialog);
        imv_delete_work_type_select = (ImageView) dialog_this.findViewById(R.id.Btn_Remove_Work_Type_AU);


        btn_add_work_type            .setOnClickListener(this);
        btn_exit_dialog_this         .setOnClickListener(this);
        imv_delete_work_type_select  .setOnClickListener(this);

    }

    boolean Add_New_Work_Type()
    {

        GAS_FolderNameInfo folderNameInfo = new GAS_FolderNameInfo();

        folderNameInfo.setFolderName(ip_work_type_name.getText().toString());
        folderNameInfo.setColorURI(String.valueOf(Color));

        return new Folder_Data_Manager(context).Add_New_Folder(folderNameInfo.getFolderName(), folderNameInfo.getColorURI());
    }

    boolean Update_Work_Type()
    {
        GAS_FolderNameInfo folderNameInfo = new GAS_FolderNameInfo();

        folderNameInfo.setFolderName(ip_work_type_name.getText().toString());
        folderNameInfo.setColorURI(String.valueOf(Color));

        return new Folder_Data_Manager(context).Update_Folder(folder_get.getFolderName(), folderNameInfo);
    }

    void Remove_Work_Type()
    {
        new Folder_Data_Manager(context).Delete_Folder(folder_get.getFolderName());
    }



    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {

            case R.id.Btn_Add_Type_AU_Dialog:

                if (ip_work_type_name.getText().toString().isEmpty() == false)
                {
                    if (folder_get == null)
                    {
                        Add_New_Work_Type();
                    }else
                    {
                        boolean isUpdate = Update_Work_Type();

                        if (isUpdate)
                        {
                            Toast.makeText(context, "Cap nhap thanh cong", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Cap nhap that bai", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                dialog_this.dismiss();

                break;

            case R.id.Btn_Exit_AU_Dialog:

                dialog_this.dismiss();

                break;

            case R.id.Btn_Remove_Work_Type_AU:

                if (folder_get != null)
                {

                    Remove_Work_Type();

                    dialog_this.dismiss();
                }

                break;

        }

    }

    int Color;

    /*

            case R.id.Show_FolderColor:
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(context, android.R.color.black, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color)
                    {
                        btn_show_folder_color.setBackgroundColor(color);
                        Color = color;

                        btn_show_folder_color.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
                });

                    dialog.show();


                break;*/

}
