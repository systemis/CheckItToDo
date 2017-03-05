package com.example.nickpham.checkittodo.Dialog_Manager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nickpham.checkittodo.DataManager.Account_Data_Manager;
import com.example.nickpham.checkittodo.DataManager.Work_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_AccountInfo;
import com.example.nickpham.checkittodo.MainActivity;
import com.example.nickpham.checkittodo.R;

/**
 * Created by nickpham on 07/01/2017.
 */

public class Dialog_AD_Account implements View.OnClickListener {

    public Context context;
    public GAS_AccountInfo account_list_get = null;

    public Dialog Dialog_This;

    LinearLayout     ln_main_layout_this;
    public EditText  ip_account_name, ip_account_pass, ip_new_accout_pass;
    public Button    btn_add_account, btn_exit_aacount;
    public ImageView imv_delete_account_selected;

    public Dialog_AD_Account(Context context, GAS_AccountInfo account_list_get)
    {

        this.context = context;
        this.account_list_get = account_list_get;
        Create_Dialog();

        btn_add_account.setText("Update");

        // Update construc
        Set_Value();

    }


    public Dialog_AD_Account(Context context)
    {

        this.context = context;
        Create_Dialog();

        imv_delete_account_selected.setVisibility(View.GONE);
        ip_new_accout_pass         .setVisibility(View.GONE);

    }

    void Set_Value()
    {

        ip_account_name.setText(account_list_get.getAccount());

    }


    void Create_Dialog()
    {

        Dialog_This = new Dialog(context);

        Dialog_This.setContentView(R.layout.dialog_ad_account);

        Dialog_This.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Dialog_This.setCancelable(false);

        AnhxaDialog();


        Dialog_This.show();
    }

    void AnhxaDialog()
    {


        ln_main_layout_this         = (LinearLayout) Dialog_This.findViewById(R.id.Layout_Main_Dialog_AD_Account);
        ip_account_name             = (EditText)     Dialog_This.findViewById(R.id.Import_Account_Name_AD);
        ip_account_pass             = (EditText)     Dialog_This.findViewById(R.id.Import_Account_Pass_AD);
        ip_new_accout_pass          = (EditText)     Dialog_This.findViewById(R.id.Import_New_Account_Pass_AD);
        btn_add_account             = (Button)       Dialog_This.findViewById(R.id.Btn_AP_ADD_ialog);
        btn_exit_aacount            = (Button)       Dialog_This.findViewById(R.id.Btn_Exit_AD_Dialog);
        imv_delete_account_selected = (ImageView)    Dialog_This.findViewById(R.id.Btn_Remove_Account_Selected_AD);


        btn_add_account            .setOnClickListener(this);
        btn_exit_aacount           .setOnClickListener(this);
        imv_delete_account_selected.setOnClickListener(this);

    }

    boolean Add_New_Account()
    {
        GAS_AccountInfo accountInfo = new GAS_AccountInfo();

        accountInfo.setAccount(ip_account_name.getText().toString());
        accountInfo.setPassWord(ip_account_pass.getText().toString());

        return new Account_Data_Manager(context).isAddNewAccount(accountInfo.getAccount(), accountInfo.getPassWord());
    }


    boolean Update_Account ()
    {

        GAS_AccountInfo accountInfo = new GAS_AccountInfo();

        accountInfo.setAccount(ip_account_name.getText().toString());
        accountInfo.setPassWord(ip_account_pass.getText().toString());

        if (ip_account_pass.getText().toString().equals(account_list_get.getPassWord()))
        {

            accountInfo.setPassWord(ip_new_accout_pass.getText().toString());
            return new Account_Data_Manager(context).isUpdateAccount(accountInfo.getAccount(), accountInfo);

        }else
        {
            return false;
        }

    }


    void Remove_Account()
    {
        MainActivity.Account_Bundle[0] = account_list_get.getAccount();

        new Work_Data_Manager(context, "").Delete_All_Work("");

        new Work_Data_Manager(context, "").DeleteTable();

        new Account_Data_Manager(context).DeleteAccount(account_list_get.getAccount());

        if (account_list_get.getAccount().equals(MainActivity.Account_Bundle[0]))
        {
            MainActivity.Account_Bundle[0] = "";
        }
    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {

            case R.id.Btn_AP_ADD_ialog:

                if (ip_account_name.getText().toString().isEmpty() == false)
                {
                    if (account_list_get == null)
                    {
                        Add_New_Account();
                    }else
                    {
                        boolean isUpdate = Update_Account();

                        if (isUpdate)
                        {
                            Toast.makeText(context, "Cap nhap thanh cong", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Cap nhap that bai", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                Dialog_This.dismiss();

                break;

            case R.id.Btn_Exit_AD_Dialog:

                Dialog_This.dismiss();

                break;

            case R.id.Btn_Remove_Account_Selected_AD:

                if (account_list_get != null)
                {

                    Remove_Account();

                    Dialog_This.dismiss();

                }

                break;

        }

    }

}
