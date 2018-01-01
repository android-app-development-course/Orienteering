package com.scnu.sihao.orienteering.register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scnu.sihao.orienteering.R;
import com.scnu.sihao.orienteering.Entity.competiton;

import java.util.List;

/**
 * Created by XW on 2017/12/22.
 */

public class CompetitionAdapter extends ArrayAdapter<competiton> {
    private int resourceId;
    public CompetitionAdapter(Context context, int textViewResourceId, List<competiton> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        competiton competiton=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView item_tv=(TextView) view.findViewById(R.id.item_tv);
        TextView num_people=(TextView) view.findViewById(R.id.num_people);
        TextView time_begin=(TextView) view.findViewById(R.id.time_begin);
        TextView time_over=(TextView) view.findViewById(R.id.time_over);
        item_tv.setText(competiton.getName());
        num_people.setText(competiton.getNum_people());
        time_begin.setText(competiton.getBegintime());
        time_over.setText(competiton.getEndtime());
        return view;
    }
}

