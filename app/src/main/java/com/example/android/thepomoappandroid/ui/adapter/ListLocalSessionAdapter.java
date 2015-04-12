package com.example.android.thepomoappandroid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.vo.LocalSession;

import java.util.List;

/**
 * Created by Enric on 11/04/2015.
 */
public class ListLocalSessionAdapter extends BaseAdapter {

    private class ViewHolder {
        TextView sbNum;
        TextView sbHeader;
        TextView sbDetail;
    }

    Context context;

    protected List<LocalSession> listLocalSessions;
    LayoutInflater inflater;

    public ListLocalSessionAdapter(Context context, List<LocalSession> listLocalSessions) {
        this.listLocalSessions = listLocalSessions;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return listLocalSessions.size();
    }

    public LocalSession getItem(int position) {
        return listLocalSessions.get(position);
    }

    public long getItemId(int position) {
        return listLocalSessions.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.session_brief_layout, parent, false);

            holder.sbNum = (TextView) convertView.findViewById(R.id.sb_num);
            holder.sbHeader = (TextView) convertView.findViewById(R.id.sb_header);
            holder.sbDetail = (TextView) convertView.findViewById(R.id.sb_detail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocalSession localSession = listLocalSessions.get(position);
        holder.sbNum.setText(Integer.toString(localSession.getNum()));
        int module = position%4;
        switch (module) {
            case 0 : holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_orange));
                break;
            case 1: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_blue));
                break;
            case 2: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_red));
                break;
            case 3: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_green));
                break;
        }
        holder.sbHeader.setText(localSession.getDescription());
        holder.sbDetail.setText(Integer.toString(localSession.getNum()) + " " + context.getResources().getString(R.string.pomodoros));

        return convertView;
    }
}
