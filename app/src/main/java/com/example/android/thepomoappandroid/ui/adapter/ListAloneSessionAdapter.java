package com.example.android.thepomoappandroid.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.AloneSession;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by Enric on 11/04/2015.
 */
public class ListAloneSessionAdapter extends RealmBaseAdapter<AloneSession> implements ListAdapter {

    private class ViewHolder {
        TextView sbNum;
        TextView sbHeader;
        TextView sbDetail;
    }

    public ListAloneSessionAdapter(Context context, int resId, RealmResults<AloneSession> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_session, parent, false);

            holder = new ViewHolder();
            holder.sbNum = (TextView) convertView.findViewById(R.id.num);
            holder.sbHeader = (TextView) convertView.findViewById(R.id.header);
            holder.sbDetail = (TextView) convertView.findViewById(R.id.detail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AloneSession aloneSession = realmResults.get(position);
        holder.sbNum.setText(Integer.toString(aloneSession.getNum()));
        int module = position%9;
        switch (module) {
            case 0 : holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_1));
                break;
            case 1: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_2));
                break;
            case 2: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_3));
                break;
            case 3: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_4));
                break;
            case 4: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_5));
                break;
            case 5: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_6));
                break;
            case 6: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_7));
                break;
            case 7: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_8));
                break;
            case 8: holder.sbNum.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_9));
                break;
        }
        holder.sbHeader.setText(aloneSession.getName());
        holder.sbDetail.setText(Integer.toString(aloneSession.getNum()) + " " + context.getResources().getString(R.string.pomodoros));

        return convertView;
    }

    public RealmResults<AloneSession> getRealmResults() {
        return realmResults;
    }
}
