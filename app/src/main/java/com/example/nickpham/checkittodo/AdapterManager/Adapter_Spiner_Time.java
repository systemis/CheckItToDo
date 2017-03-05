package com.example.nickpham.checkittodo.AdapterManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nickpham.checkittodo.R;

/**
 * Created by nickpham on 30/12/2016.
 */

public class Adapter_Spiner_Time extends BaseAdapter
{

    Context context;
    String[] Count_Object;

    public Adapter_Spiner_Time(Context context, String[] Count_Object)
    {

        this.context = context;
        this.Count_Object = Count_Object;

    }

    public class ViewHolder
    {
        TextView tv_set_values_object;
    }

    @Override
    public int getCount() {return Count_Object.length; }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        View view_rop = view;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (view_rop == null)
        {

            view_rop = inflater.inflate(R.layout.adaper_time_layout, viewGroup, false);

            ViewHolder holder = new ViewHolder();
            holder.tv_set_values_object = (TextView) view_rop.findViewById(R.id.Show_Object_Values);

            view_rop.setTag(holder);

        }


        ViewHolder holder = (ViewHolder) view_rop.getTag();

        holder.tv_set_values_object.setText(Count_Object[position]);


        return view_rop;

    }
}
