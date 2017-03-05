package com.example.nickpham.checkittodo.AdapterManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.nickpham.checkittodo.DataManager.Work_Data_Manager;
import com.example.nickpham.checkittodo.GAS.GAS_CoverString_Time;
import com.example.nickpham.checkittodo.GAS.GAS_WorkInFormation;
import com.example.nickpham.checkittodo.MainActivity;
import com.example.nickpham.checkittodo.R;

import java.util.List;

/**
 * Created by nickpham on 31/12/2016.
 */

public class Adapter_Listviews_Show_Works_List extends BaseAdapter
{

    Context context;
    List<GAS_WorkInFormation> gas_workInFormationList;

    public Adapter_Listviews_Show_Works_List(Context context, List<GAS_WorkInFormation> gas_workInFormationList)
    {

        this.context                 = context;
        this.gas_workInFormationList = gas_workInFormationList;

    }

    public class ViewHolder
    {

        CheckBox cb_show_complete;
        TextView show_work_name, show_work_time;

    }

    @Override
    public int getCount() {
        return gas_workInFormationList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup)
    {

        View viewRop = view;

        ViewHolder holder = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            viewRop = inflater.inflate(R.layout.adapter_works_list, viewGroup, false);

            holder = new ViewHolder();

            holder.cb_show_complete = (CheckBox) viewRop.findViewById(R.id.Show_Work_Complte_ListView_Adapter);
            holder.show_work_name   = (TextView) viewRop.findViewById(R.id.Show_Work_Name_ListView_Adapter);
            holder.show_work_time   = (TextView) viewRop.findViewById(R.id.Show_Work_Time_ListView_Adapter);

            viewRop.setTag(holder);

        }
        holder = (ViewHolder) viewRop.getTag();


        if (Integer.parseInt(gas_workInFormationList.get(position).getComplete()) != -1) holder.cb_show_complete.setChecked(true);

        holder.show_work_name.setText(gas_workInFormationList.get(position).getName());
        holder.show_work_time.setText(new GAS_CoverString_Time().Get_Time(gas_workInFormationList.get(position).getAlarm()));

        if(Integer.parseInt(gas_workInFormationList.get(position).getComplete()) != -1) { holder.cb_show_complete.setChecked(true); } else { holder.cb_show_complete.setChecked(false); }

        holder.cb_show_complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                Update_Work(position, (CheckBox) compoundButton);
            }
        });

        notifyDataSetChanged();

        return viewRop;

    }

    public void Update_Work(int position, CheckBox cb_complete_work)
    {

        GAS_WorkInFormation gas_workInFormation = gas_workInFormationList.get(position);

        if (cb_complete_work.isChecked() == false) { gas_workInFormation.setComplete("-1"); } else { gas_workInFormation.setComplete("1"); }

        new Work_Data_Manager(context, "").Update_Folder(gas_workInFormation.getName(), gas_workInFormation);


    }

}
