package com.example.nickpham.checkittodo.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nickpham.checkittodo.DataManager.Work_Data_Manager;
import com.example.nickpham.checkittodo.Dialog_Manager.Dialog_Add_New_Usually_Work;
import com.example.nickpham.checkittodo.Dialog_Manager.Dialog_Add_New_Work;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;
import com.example.nickpham.checkittodo.MainActivity;
import com.example.nickpham.checkittodo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickpham on 02/01/2017.
 */

public class Other_OR_All_Fragment extends android.app.Fragment implements View.OnClickListener {

    Context context;
    String Folder_Name;

    View view_rop;
    RelativeLayout layout_main_this;
    com.github.clans.fab.FloatingActionButton flt_add_new_work_this, flt_remove_all_work_this;

    public Other_OR_All_Fragment(){}

    @SuppressLint("ValidFragment")
    public Other_OR_All_Fragment(Context context, String Fnoler_Name) {

        this.context = context;
        this.Folder_Name = Fnoler_Name;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view_rop = inflater.inflate(R.layout.all_notes_fragment, container, false);

        layout_main_this = (RelativeLayout) view_rop.findViewById(R.id.Layout_Main_All_Notes_Fragment);

        flt_add_new_work_this = (com.github.clans.fab.FloatingActionButton) view_rop.findViewById(R.id.Flt_Add_New_Work_All_Notes_Frgament);
        flt_remove_all_work_this = (com.github.clans.fab.FloatingActionButton) view_rop.findViewById(R.id.Flt_Remove_Work_All_Notes_Frgament);

        flt_add_new_work_this.setOnClickListener(this);
        flt_remove_all_work_this.setOnClickListener(this);

        Handling_Get_Data();

        return view_rop;
    }

    List<GAS_WorkInFormation> works_list_w_folder = null;

    public void Handling_Get_Data() {

        layout_main_this.removeAllViews();

        works_list_w_folder = Get_Data(Folder_Name);

        if (works_list_w_folder != null) {

            ListView lst_show_work = new ListView(context);

            final List<String> works_name_list_w_folder = new ArrayList<>();

            for (int i = 0; i < works_list_w_folder.size(); i++)
                works_name_list_w_folder.add(works_list_w_folder.get(i).getName());

            ArrayAdapter adapter_show_works_w_folder = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, works_name_list_w_folder);

            adapter_show_works_w_folder.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

            lst_show_work.setAdapter(adapter_show_works_w_folder);

            lst_show_work.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.d("Item_Selected", works_list_w_folder.get(i).getId());

                    // Do this method when item of listview is clicked by user

                    // This Method will check clicked item is normal item or usually item
                    if (works_list_w_folder.get(i).getType().equals("Usually") != true)
                    {
                        Action_Wh_Item_Click(i);
                    } else
                    {
                        new Dialog_Add_Usually_Work(context, works_list_w_folder.get(i));
                    }

                }
            });

            layout_main_this.addView(lst_show_work);

        } else
        {

            Setup_IsNotHaveData();
        }

    }


    void Setup_IsNotHaveData()
    {
        TextView txt_no_data = new TextView(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        txt_no_data.setTextSize(TypedValue.COMPLEX_UNIT_DIP, context.getResources().getDimension(R.dimen.TextSize_Center_Remind_ANY));
        txt_no_data.setText("Không có công việc nào cả");
        txt_no_data.setLayoutParams(params);

        layout_main_this.addView(txt_no_data);
    }


    List<GAS_WorkInFormation> Get_Data(String Foler_Name) {

        if (new Work_Data_Manager(context, "").Show_All_Work() != null) {

            List<GAS_WorkInFormation> gas_workInFormationList = new Work_Data_Manager(context, "").Show_All_Work();

            if (Foler_Name.equals("All Note")) {
                return gas_workInFormationList;
            } else {
                List<GAS_WorkInFormation> gas_workInFormationList_Want = new ArrayList<GAS_WorkInFormation>();

                for (int i = 0; i < gas_workInFormationList.size(); i++) {
                    if (gas_workInFormationList.get(i).getType().equals(Foler_Name)) {
                        gas_workInFormationList_Want.add(gas_workInFormationList.get(i));
                    }
                }

                return gas_workInFormationList_Want;
            }
        }

        return null;
    }


    void Action_Wh_Item_Click(int position) {

        GAS_WorkInFormation work_selected_info = works_list_w_folder.get(position);

        Log.d("Clicked Item All Note", work_selected_info.getName());

        Dialog_Add_New_Work_Other_Screen dialog_add_new_work_thisfrg = new Dialog_Add_New_Work_Other_Screen(context, work_selected_info, "");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.Flt_Add_New_Work_All_Notes_Frgament:

                Dialog_Add_New_Work_Other_Screen dialog_add_new_work_thisfrg = new Dialog_Add_New_Work_Other_Screen(context, 1, "");

                break;

            case R.id.Flt_Remove_Work_All_Notes_Frgament:

                DL_Remind_W_Remove_All_Work().show();

                break;

        }
    }


    // This dialog will show to remind user while user press remove floatingbutton
    Dialog DL_Remind_W_Remove_All_Work() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Warring")
                .setMessage("Bạn có chắc chắn là xoá hết công việc trong kho dữ liệu không?")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new Work_Data_Manager(context, "").Delete_All_Work("");
                        Handling_Get_Data();
                        dialogInterface.dismiss();

                    }
                });

        return builder.create();

    }


    // This Dialog will show if user press normal items
    class Dialog_Add_New_Work_Other_Screen extends Dialog_Add_New_Work         {


        public Dialog_Add_New_Work_Other_Screen(Context context, GAS_WorkInFormation work_data_w_day, String SelectedDate) {
            super(context, work_data_w_day, SelectedDate);
            Connect_Action_View();
        }

        public Dialog_Add_New_Work_Other_Screen(Context context, int Choice_Selected_User, String SelectedDate) {
            super(context, Choice_Selected_User, SelectedDate);
            Connect_Action_View();
        }

        public void Connect_Action_View() {

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (view.getId()) {

                        case R.id.Btn_Done_Dialog_Add_Work:
                            if (work_data_w_day == null) {
                                Add_New_Work();
                            } else {
                                Update_Work();
                            }

                            Dialog_This.dismiss();
                            Handling_Get_Data();

                            break;

                        case R.id.Btn_Delete_This_Work:

                            if (work_data_w_day != null) {
                                Delete_Work();
                                Handling_Get_Data();
                            }

                            Dialog_This.dismiss();

                            break;

                    }

                }
            };

            imv_done_dialog.setOnClickListener(listener);
            imv_delete_this_work.setOnClickListener(listener);

        }

    }


    // This Dialog will show if user press usually items
    class Dialog_Add_Usually_Work          extends Dialog_Add_New_Usually_Work {

        public Dialog_Add_Usually_Work(Context context, GAS_WorkInFormation usually_work_selected) {
            super(context, usually_work_selected);
            Action_DismissDialog();
        }

        public Dialog_Add_Usually_Work(Context context) {
            super(context);
            Action_DismissDialog();
        }


        public void Action_DismissDialog() {

            Dialog_Add_UsuallyWork_This.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                    Handling_Get_Data();

                }
            });

        }
    }


}