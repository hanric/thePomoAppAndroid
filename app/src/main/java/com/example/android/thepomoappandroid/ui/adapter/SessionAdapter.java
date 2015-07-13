package com.example.android.thepomoappandroid.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.Session;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.ResponseCallback;

/**
 * Created by Enric on 02/05/2015.
 */
public class SessionAdapter extends RealmBaseAdapter<Session> implements ListAdapter {

    private class ViewHolder {
        public TextView num;
        public TextView header;
        public TextView detail;
        public TextView startTime;
    }

    public SessionAdapter (Context context, int resId, RealmResults<Session> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_session, parent, false);

            holder = new ViewHolder();
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.header = (TextView) convertView.findViewById(R.id.header);
            holder.detail = (TextView) convertView.findViewById(R.id.detail);
            holder.startTime = (TextView) convertView.findViewById(R.id.startTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Session session = realmResults.get(position);
        holder.num.setText(Integer.toString(session.getNPomos()));
        setNumBackgroundColor(holder.num, position);
        holder.header.setText(session.getName());
        holder.detail.setText(formatDetailText(session));
        holder.startTime.setVisibility(View.VISIBLE);
        holder.startTime.setText(session.getStartTime());
        return convertView;
    }

    private void setNumBackgroundColor(TextView textView, int i) {
        int module = i%9;
        switch (module) {
            case 0 : textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_1));
                break;
            case 1: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_2));
                break;
            case 2: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_3));
                break;
            case 3: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_4));
                break;
            case 4 : textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_5));
                break;
            case 5: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_6));
                break;
            case 6: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_7));
                break;
            case 7: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_8));
                break;
            case 8: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_9));
                break;
        }
    }

    private String formatDetailText(Session session) {
        return Integer.toString(session.getNPomos()) + " " + context.getResources().getString(R.string.pomodoros);
    }
}
