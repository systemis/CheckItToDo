package com.example.nickpham.checkittodo.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.nickpham.checkittodo.CustomWidget.CircleClock;
import com.example.nickpham.checkittodo.DataManager.Work_Data_Manager;
import com.example.nickpham.checkittodo.Dialog_Manager.Dialog_Add_New_Usually_Work;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;
import com.example.nickpham.checkittodo.MainActivity;
import com.example.nickpham.checkittodo.R;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickpham on 30/12/2016.
 */

public class Usually_Fragment  extends Fragment implements View.OnClickListener
{

    Context context;
    View View_This;

    RelativeLayout rl_show_data;
    ViewFlipper    vf_show_work_info;

    TextView txt_no_data = null, show_current_position_viewflipper;

    FloatingActionButton flt_add_new_usually_work, flt_remove_all_usually_work;
    ImageView imv_delete_work, imv_edit_work, btn_next_usually_work, btn_previous_usually_work;;

    public Usually_Fragment(){}

    @SuppressLint("ValidFragment")
    public Usually_Fragment(Context context)
    {

        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View_This = inflater.inflate(R.layout.usually_fragment_layout, null, false);

        Anhxa_This();

        return View_This;

    }


    void Anhxa_This()
    {

        rl_show_data                      = (RelativeLayout)       View_This.findViewById(R.id.Show_Data_Usually);
        vf_show_work_info                 = (ViewFlipper)          View_This.findViewById(R.id.VF_Content_Clocks);

        imv_edit_work                     = (ImageView)            View_This.findViewById(R.id.IMV_Edit_Usually_Work);
        imv_delete_work                   = (ImageView)            View_This.findViewById(R.id.IMV_Delete_Usually_Work);
        btn_previous_usually_work         = (ImageView)            View_This.findViewById(R.id.Btn_Previous_Work_Usually_Screen);
        btn_next_usually_work             = (ImageView)            View_This.findViewById(R.id.Btn_Next_Work_Usually_Screen);

        flt_add_new_usually_work          = (FloatingActionButton) View_This.findViewById(R.id.Flt_Add_New_Usually_Work);
        flt_remove_all_usually_work       = (FloatingActionButton) View_This.findViewById(R.id.Flt_Remove_Work_All_Usually_Work);

        show_current_position_viewflipper = (TextView)             View_This.findViewById(R.id.Show_ViewFlipper_Current_Posision);

        show_current_position_viewflipper.setVisibility(View.GONE);

        flt_add_new_usually_work   .setOnClickListener(this);
        flt_remove_all_usually_work.setOnClickListener(this);
        imv_edit_work              .setOnClickListener(this);
        imv_delete_work            .setOnClickListener(this);
        btn_next_usually_work      .setOnClickListener(this);
        btn_previous_usually_work  .setOnClickListener(this);

        Handling_Get_Data();

    }


    List<GAS_WorkInFormation> UsuallyWorkInfo_list = null;
    public void Handling_Get_Data()
    {

        UsuallyWorkInfo_list = DataList_F_Database();

        vf_show_work_info.removeAllViews();

        if (UsuallyWorkInfo_list != null && UsuallyWorkInfo_list.size() > 0)
        {

            if (txt_no_data != null && txt_no_data.isShown())
            {
                imv_edit_work.setVisibility(View.VISIBLE);
                imv_delete_work.setVisibility(View.VISIBLE);
                txt_no_data.setVisibility(View.GONE);
            }

            int TypeShown;
            if (UsuallyWorkInfo_list.size() > 1) { TypeShown = View.VISIBLE; } else { TypeShown = View.GONE; }

            btn_next_usually_work            .setVisibility(TypeShown);
            btn_previous_usually_work        .setVisibility(TypeShown);
            show_current_position_viewflipper.setVisibility(TypeShown);

            if (TypeShown == 0) show_current_position_viewflipper.setText("1");

            Setup_isHaveData(DataList_F_Database());

        }else
        {

            if (txt_no_data == null)
            {
                txt_no_data = Setup_isNotHaveData();
                rl_show_data.addView(txt_no_data);

            } else
            {
                if (txt_no_data.isShown() == false) txt_no_data.setVisibility(View.VISIBLE);
            }

            imv_edit_work.setVisibility(View.GONE);
            imv_delete_work.setVisibility(View.GONE);
            btn_next_usually_work    .setVisibility(View.GONE);
            btn_previous_usually_work.setVisibility(View.GONE);

        }

    }


    List<GAS_WorkInFormation> DataList_F_Database()
    {

        List<GAS_WorkInFormation> all_data         = new Work_Data_Manager(context, "").Show_All_Work();
        List<GAS_WorkInFormation> all_data_usually = new ArrayList<>();

        if (all_data != null)
        {

            for (int i = 0; i < all_data.size(); i++)
            {

                if (all_data.get(i).getType().equals("Usually"))
                {

                    all_data_usually.add(all_data.get(i));

                }

            }

        }

        return all_data_usually;

    }


    TextView Setup_isNotHaveData()
    {
        TextView txt_no_data = new TextView(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        txt_no_data.setTextSize(TypedValue.COMPLEX_UNIT_DIP, context.getResources().getDimension(R.dimen.TextSize_Center_Remind_ANY));
        txt_no_data.setText(context.getResources().getString(R.string.Title_Notifi_NoHaveWorkDay));
        txt_no_data.setLayoutParams(params);

        return txt_no_data;

    }


    void Setup_isHaveData(List<GAS_WorkInFormation> usually_work_info_list)
    {

        for (int i = 0; i < usually_work_info_list.size(); i++)
        {

            vf_show_work_info.addView(rl_chill_viewflipper(usually_work_info_list.get(i), i));

        }

    }


    RelativeLayout rl_chill_viewflipper(GAS_WorkInFormation usually_work_info, int Tag)
    {

        RelativeLayout rl_chill = new RelativeLayout(context);
        rl_chill.setTag(Tag);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        rl_chill.setLayoutParams(params);

        rl_chill.addView(new CircleClock(context, usually_work_info).Create_Circle_Clock());

        return rl_chill;

    }


    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {

            case R.id.Flt_Add_New_Usually_Work:

                new Dialog_Add_Usually_Work(context);

                break;

            case R.id.Flt_Remove_Work_All_Usually_Work:

                if (UsuallyWorkInfo_list != null && UsuallyWorkInfo_list.size() > 0)
                {

                    Log.d("ActionTest", "Are Deleting All Usually Work");
                    new Work_Data_Manager(context, "").Delete_All_Work(UsuallyWorkInfo_list.get((Integer) vf_show_work_info.getCurrentView().getTag()).getAlarm() + "Usually");
                    Handling_Get_Data();

                }else
                {
                    Toast.makeText(context, "Bạn không có hành động nào trong mục này cả!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.IMV_Edit_Usually_Work:

                // Show dialog to show selected usually work info
                new Dialog_Add_Usually_Work(context, UsuallyWorkInfo_list.get((Integer) vf_show_work_info.getCurrentView().getTag()));

                break;


            case R.id.IMV_Delete_Usually_Work:

                // Delete usually work at outside usually work dialog
                new Work_Data_Manager(context, "").Delete_Work_F_Data(UsuallyWorkInfo_list.get((Integer) vf_show_work_info.getCurrentView().getTag()).getId());

                Handling_Get_Data();

                break;


            case R.id.Btn_Previous_Work_Usually_Screen:

                vf_show_work_info.showPrevious();
                OnChangeScreenViewFlipperListenner();

                break;

            case R.id.Btn_Next_Work_Usually_Screen:

                vf_show_work_info.showNext();
                OnChangeScreenViewFlipperListenner();

                break;

        }

    }

    void OnChangeScreenViewFlipperListenner()
    {
        show_current_position_viewflipper.setText(String.valueOf((Integer) vf_show_work_info.getCurrentView().getTag() + 1));
    }

    class Dialog_Add_Usually_Work extends Dialog_Add_New_Usually_Work
    {

        public Dialog_Add_Usually_Work(Context context, GAS_WorkInFormation usually_work_selected)
        {
            super(context, usually_work_selected);
            Action_DismissDialog();
        }

        public Dialog_Add_Usually_Work(Context context)
        {
            super(context);
            Action_DismissDialog();
        }


        public void Action_DismissDialog()
        {

            Dialog_Add_UsuallyWork_This.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialogInterface)
                {

                    Handling_Get_Data();

                }
            });

        }


    }

}
