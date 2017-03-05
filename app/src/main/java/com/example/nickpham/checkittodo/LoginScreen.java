package com.example.nickpham.checkittodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nickpham.checkittodo.DataManager.Account_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_AccountInfo;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;

import java.util.List;

public class LoginScreen extends AppCompatActivity {

    EditText edt_import_username, edt_import_password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Anhxa();

    }


    public int Anhxa()
    {

        edt_import_username = (EditText) findViewById(R.id.Import_UserName_View);
        edt_import_password = (EditText) findViewById(R.id.Import_PassWord_View);

        return 0;

    }

    // Login App dont't want account
    public void COME_Manager_Screen_onclick(View v)
    {

        this.startActivity(new Intent(LoginScreen.this, MainActivity.class));

        this.finish();

    }

    public void Login_onclick(View v)
    {
        if(edt_import_username.getText().toString().isEmpty() == false && edt_import_password.getText().toString().isEmpty() == false)
        {
            Log.d("AppAction", "Logining");

            // Send Message to Service

            List<GAS_AccountInfo> accountInfo_list = new Account_Data_Manager(this).Show_All_Account();

            if (accountInfo_list != null)
            {
                int AccountPosition = getPosition_AccountItem(accountInfo_list, edt_import_username.getText().toString());

                if(AccountPosition != -1)
                {
                    GAS_AccountInfo get_account = accountInfo_list.get(AccountPosition);

                    if(get_account.getPassWord().equals(edt_import_password.getText().toString()))
                    {
                        Toast.makeText(this, "Login OK!", Toast.LENGTH_SHORT).show();
                        Start_Activity(edt_import_username.getText().toString(), edt_import_password.getText().toString());
                    }else
                    {
                        Log.d("LoginAction", "Login Not Ok");
                    }
                }else
                {
                    Log.d("LoginAction", "Login Not Ok");
                }
            }else
                Log.d("LoginAction", "Login Not Ok");
            }

    }



    public void SignUp_onclick(View v)
    {
        if(edt_import_username.getText().toString().isEmpty() == false && edt_import_password.getText().toString().isEmpty() == false)
        {
            boolean Result = new Account_Data_Manager(this).isAddNewAccount(edt_import_username.getText().toString(), edt_import_password.getText().toString());
            if (Result)
            {
                Start_Activity(edt_import_username.getText().toString(), edt_import_password.getText().toString());
            }
        }
    }

    void Start_Activity(String UserName, String PassWord)
    {
        Intent main_intent = new Intent(this, MainActivity.class);

        Bundle bundle_AccountBundle = new Bundle();

        bundle_AccountBundle.putString("Account_UserName", UserName);
        bundle_AccountBundle.putString("Account_PassWord", PassWord);

        main_intent.putExtra("AccountBundle", bundle_AccountBundle);

        startActivity(main_intent);

        finish();
    }

    int getPosition_AccountItem(List<GAS_AccountInfo> account_list, String Name)
    {
        int Result = -1;

        for (int i = 0; i < account_list.size(); i++)
        {
            if (account_list.get(i).getAccount().equals(Name))
            {
                Result = i;
            }
        }

        return Result;
    }

}
