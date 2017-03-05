package com.example.nickpham.checkittodo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nickpham.checkittodo.DataManager.Folder_Data_Manager;
import com.example.nickpham.checkittodo.FragmentManager.Home_Fragment;
import com.example.nickpham.checkittodo.FragmentManager.Other_OR_All_Fragment;
import com.example.nickpham.checkittodo.FragmentManager.Settings_Fragment;
import com.example.nickpham.checkittodo.FragmentManager.Usually_Fragment;


public class MainActivity extends AppCompatActivity {

    public static String[] Account_Bundle = new String[2];

    RelativeLayout content_panel;
    LinearLayout toolbar_main_screen;
    ImageButton btn_home, btn_usually, btn_notes, btn_settings;

    android.app.FragmentTransaction frg_cotent_panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Fisrt_Run_App();

        Get_Account_Bundle();

        Anhxa();

    }



    public static boolean active = true;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    // Fisrt Run app will add default folder by artis create
    // This method will add to database 2 default work folder (Work, Family)
    public void Fisrt_Run_App()
    {

        Folder_Data_Manager folder_manager = new Folder_Data_Manager(this);

        if (folder_manager.Show_All_Folder() == null)
        {

            Add_Folder(getResources().getString(R.string.DefaultFolderNameFamily), String.valueOf(Color.BLUE));
            Add_Folder(getResources().getString(R.string.DefaultFolderNameWorks) , String.valueOf(Color.RED));
            Add_Folder(getResources().getString(R.string.other_folder_title)     , String.valueOf(Color.BLACK));
        }

    }

    // Get Information about Account have Signined to app
    public void Get_Account_Bundle() {

        Bundle Get_Data_Content = getIntent().getBundleExtra("AccountBundle");

        if (Get_Data_Content != null)
        {

            Account_Bundle[0] = Get_Data_Content.getString("Account_UserName");
            Account_Bundle[1] = Get_Data_Content.getString("Account_PassWord");

        }else
        {

            Account_Bundle[0] = "";

        }

    }


    public int Anhxa() {

        content_panel       = (RelativeLayout) this.findViewById(R.id.Content_Panel);
        toolbar_main_screen = (LinearLayout)   this.findViewById(R.id.Toolbar_Main_Screen);

        btn_home            = (ImageButton)    this.findViewById(R.id.Btn_Home);
        btn_usually         = (ImageButton)    this.findViewById(R.id.Btn_Usually);
        btn_notes           = (ImageButton)    this.findViewById(R.id.Btn_Notes);
        btn_settings        = (ImageButton)    this.findViewById(R.id.Btn_Settings);

        frg_cotent_panel    = getFragmentManager().beginTransaction();
        frg_cotent_panel.add(R.id.Content_Panel, new Home_Fragment(MainActivity.this, 1));
        frg_cotent_panel.commit();

        return 0;

    }

    public void Add_Folder(String name, String URICOlor)
    {

        boolean result = new Folder_Data_Manager(MainActivity.this).Add_New_Folder(name, URICOlor);

        if (result)
        {
            Log.d("Result Add Folder", "Thanh cong");
        }else
        {
            Log.d("Result Add Folder", "That bai");
        }

    }


    /** Matrix
     *
     * 1: Home
     * 2: Usually
     * 3: Job
     * 4: Familly
     *
     * */

    public void ExChange_Content_Panel(int Mode_Panel, String FolderName)
    {

        Set_Default_Theme();

        Change_Text_Color_TV_Toolbar(Mode_Panel);

        frg_cotent_panel    = getFragmentManager().beginTransaction();

        switch (Mode_Panel)
        {

            case 1:

                frg_cotent_panel.replace(R.id.Content_Panel, new Home_Fragment(this, Mode_Panel));

                break;
            case 2:
                frg_cotent_panel.replace(R.id.Content_Panel, new Usually_Fragment(this));
                break;

            case 3:

                Other_OR_All_Fragment other_or_all_fragment = new Other_OR_All_Fragment(this, FolderName);

                frg_cotent_panel.replace(R.id.Content_Panel, other_or_all_fragment);

                break;

            default:

                frg_cotent_panel.replace(R.id.Content_Panel, new Settings_Fragment(this));

                break;

        }

        frg_cotent_panel.commit();

    }

    public void Change_Text_Color_TV_Toolbar(int Pos)
    {

        int[] ID = {R.id.TV_Home, R.id.TV_Usually, R.id.TV_Notes, R.id.TV_Settings};

        for (int i = 0; i < ID.length; i++)
        {
            ((TextView) this.findViewById(ID[i])).setTextColor(Color.WHITE);
        }

        ((TextView) this.findViewById(ID[Pos - 1])).setTextColor(this.getResources().getColor(R.color.GreenColor));

    }

    public void Set_Default_Theme()
    {

        btn_home     .setImageResource(R.mipmap.home_icon);
        btn_usually  .setImageResource(R.mipmap.usually_icon);
        btn_notes    .setImageResource(R.mipmap.notepad_icon);
        btn_settings.setImageResource(R.mipmap.settings_icon);

    }




    float x1 = 0, x2 = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        switch (event.getAction())
        {

            case MotionEvent.ACTION_DOWN:

                x1 = event.getX();

                break;

            case MotionEvent.ACTION_UP:

                x2 = event.getX();

                if (x1 > x2)
                {

                    Hide_Toolbar();

                }else if (x1 < x2)
                {

                    Show_Toolbar();

                }

                Log.d("X1", String.valueOf(x1));
                Log.d("X2", String.valueOf(x2));

                break;


            default:

                break;

        }

        return super.onTouchEvent(event);

    }

    public void Hide_Toolbar()
    {

        if (toolbar_main_screen.isShown())
        {

            Animation Hide_Animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.left_animation_toolbar);

            toolbar_main_screen.setAnimation(Hide_Animation);

            toolbar_main_screen.setVisibility(View.INVISIBLE);

        }

    }

    public void Show_Toolbar()
    {

        if (toolbar_main_screen.isShown() == false)
        {

            Animation Show_Animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.right_animation_toolbar);

            toolbar_main_screen.setAnimation(Show_Animation);

            toolbar_main_screen.setVisibility(View.VISIBLE);

        }

    }


    public void Btn_Home_onclick(View v)
    {

        ExChange_Content_Panel(1, "Trang chủ");

        ((ImageButton) v).setImageResource(R.mipmap.home_checked_icon);

    }

    public void Btn_Usually_onclick(View v)
    {

        ExChange_Content_Panel(2, "Thường xuyên");

        ((ImageButton) v).setImageResource(R.mipmap.usually_checked_icon);

    }

    public void Btn_Notes_onclick(View v)
    {

        ExChange_Content_Panel(3, "All Note");

        ((ImageButton) v).setImageResource(R.mipmap.notepad_checked_icon);

    }


    public void Btn_Settings_onclick(View v)
    {

        ExChange_Content_Panel(4, "Settings");

        ((ImageButton) v).setImageResource(R.mipmap.settings_checked_icon);

    }


    public void Btn_LogOut_onclick(View v)
    {
        this.startActivity(new Intent(this, LoginScreen.class));
        MainActivity.this.finish();
    }

}
