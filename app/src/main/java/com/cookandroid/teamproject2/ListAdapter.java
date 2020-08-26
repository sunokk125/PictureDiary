package com.cookandroid.teamproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    ArrayList<ListItem> arrayList = new ArrayList<ListItem>();

    TextView title, date;

    int layout;

    public ListAdapter(Context context, int layout, ArrayList<ListItem> arrayList){
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout=layout;
        this.arrayList = arrayList;
    }

    public int getCount(){
        return arrayList.size();
    }

    public ListItem getItem(int position){
        return arrayList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final  int finalPosition= position;

        if (convertView==null){
            convertView = inflater.inflate(layout, parent, false);
        }

        title = (TextView)convertView.findViewById(R.id.title);
        date = (TextView)convertView.findViewById(R.id.date);

        title.setText(arrayList.get(position).getDtitle());
        date.setText(arrayList.get(position).getDdate());
        return convertView;
    }
}
