package com.example.nickpham.checkittodo.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nickpham.checkittodo.DataManager.Account_Data_Manager;
import com.example.nickpham.checkittodo.DataManager.Folder_Data_Manager;
import com.example.nickpham.checkittodo.Dialog_Manager.Dialog_AD_Account;
import com.example.nickpham.checkittodo.Dialog_Manager.Dialog_AD_WorkType;
import com.example.nickpham.checkittodo.GAS.GAS_AccountInfo;
import com.example.nickpham.checkittodo.GAS.GAS_FolderNameInfo;
import com.example.nickpham.checkittodo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickpham on 07/01/2017.
 */

public class Settings_Fragment extends Fragment implements View.OnClickListener
{

    Context context;

    public Settings_Fragment(){}

    @SuppressLint("ValidFragment")
    public Settings_Fragment(Context context)
    {

        this.context = context;

    }

    View viewRop;

    LinearLayout show_account_data, show_work_type_data;
    ImageView    btn_add_new_account, btn_add_new_work_type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        viewRop = inflater.inflate(R.layout.settings_fragment, null, false);

        Anhxa();

        return viewRop;

    }

    void Anhxa()
    {

        show_account_data     = (LinearLayout) viewRop.findViewById(R.id.Show_Account_Data);
        show_work_type_data   = (LinearLayout) viewRop.findViewById(R.id.Show_Work_Type_Data);

        btn_add_new_account   = (ImageView) viewRop.findViewById(R.id.Btn_add_New_Account);
        btn_add_new_work_type = (ImageView) viewRop.findViewById(R.id.Btn_Add_New_Work_Type);


        btn_add_new_account  .setOnClickListener(this);
        btn_add_new_work_type.setOnClickListener(this);

        // Handling to show value on show_account_data view
        Handling_Show_Account_Data();

        // Handling to show value on show_folder_data view
        Handling_Show_Work_Type();


    }


    public void Handling_Show_Account_Data()
    {

        // Account
        final List<GAS_AccountInfo> account_list = new Account_Data_Manager(context).Show_All_Account();
        show_account_data.removeAllViews();

        if (account_list != null)
        {

            ListView lst_show_account_list = new ListView(context);
            RelativeLayout.LayoutParams params_lst_account = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            lst_show_account_list.setLayoutParams(params_lst_account);

            // Get Accouts Name
            List<String> Account_Name = new ArrayList<>();
            for (int i = 0; i < account_list.size(); i++) Account_Name.add(account_list.get(i).getAccount());

            ArrayAdapter<String> adapter_lst_show_account = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, Account_Name);
            adapter_lst_show_account.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

            lst_show_account_list.setAdapter(adapter_lst_show_account);

            // Action when user click items of listview
            lst_show_account_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    new Dialog_Add_Update_Account(context, account_list.get(i));
                }

            });

            show_account_data.addView(lst_show_account_list);

        }else
        {

            show_account_data.addView(TXT_Setup_isNotHaveData(1));

        }

    }


    public void Handling_Show_Work_Type()
    {

        // Work Type
        final List<GAS_FolderNameInfo> folder_list = new Folder_Data_Manager(context).Show_All_Folder();

        show_work_type_data.removeAllViews();

        if (folder_list != null)
        {


            ListView lst_show_work_type = new ListView(context);

            RelativeLayout.LayoutParams params_lst_work_type = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            lst_show_work_type.setLayoutParams(params_lst_work_type);

            // Get folder Name
            List<String> Type_Works_Name = new ArrayList<>();
            for (int i = 0; i < folder_list.size(); i++) Type_Works_Name.add(folder_list.get(i).getFolderName());

            ArrayAdapter<String> adapter_lst_show_account = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, Type_Works_Name);
            adapter_lst_show_account.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

            lst_show_work_type.setAdapter(adapter_lst_show_account);

            // Action when user click items of listview
            lst_show_work_type.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {

                    new Dialog_Add_Update_Type(context, folder_list.get(i));

                }

            });


            show_work_type_data.addView(lst_show_work_type);

        }else
        {

            show_work_type_data.addView(TXT_Setup_isNotHaveData(2));

        }

    }


    public TextView TXT_Setup_isNotHaveData(int choice)
    {

        String text_to_set = "";

        switch (choice)
        {

            case 1: text_to_set = "Chưa có tài khoản nào cả!"    ; break;

            case 2: text_to_set = "Chưa có kiểu công việc nào cả"; break;

        }


        TextView txt_no_data = new TextView(context);

        txt_no_data.setTextSize(10);
        txt_no_data.setText(text_to_set);

        return txt_no_data;

    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {

            case R.id.Btn_add_New_Account:

                new Dialog_Add_Update_Account(context);

                break;

            case R.id.Btn_Add_New_Work_Type:

                new Dialog_Add_Update_Type(context);

                break;

        }

    }


    class Dialog_Add_Update_Account extends Dialog_AD_Account
    {
        public Dialog_Add_Update_Account(Context context, GAS_AccountInfo account_list_get)
        {
            super(context, account_list_get);
            onDismissDialogThis();
        }

        public Dialog_Add_Update_Account(Context context)
        {
            super(context);
            onDismissDialogThis();
        }


        void onDismissDialogThis()
        {

            Dialog_This.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialogInterface)
                {
                    Handling_Show_Account_Data();
                }
            });

        }


    }


    class Dialog_Add_Update_Type extends Dialog_AD_WorkType {
        public Dialog_Add_Update_Type(Context context, GAS_FolderNameInfo folder_get) {
            super(context, folder_get);
            onDismissDialogThis();
        }

        public Dialog_Add_Update_Type(Context context) {
            super(context);
            onDismissDialogThis();
        }
        void onDismissDialogThis()
        {

            dialog_this.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialogInterface)
                {
                    Handling_Show_Work_Type();
                }
            });

        }
    }

}
