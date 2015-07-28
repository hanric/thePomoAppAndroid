package com.example.android.thepomoappandroid.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.Setting;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by hanric on 25/7/15.
 */
public class SettingAdapter extends RealmBaseAdapter<Setting> implements ListAdapter {

    private class ViewHolder {
        public TextView name;
        public TextView workTime;
        public TextView restTime;
        public TextView largeRestTime;
    }

    public SettingAdapter(Context context, int resId, RealmResults<Setting> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_setting, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.prefName);
            holder.workTime = (TextView) convertView.findViewById(R.id.prefWork);
            holder.restTime = (TextView) convertView.findViewById(R.id.prefRest);
            holder.largeRestTime = (TextView) convertView.findViewById(R.id.prefLargeRest);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Setting setting = realmResults.get(position);
        holder.name.setText(setting.getName());
        holder.workTime.setText(String.valueOf(setting.getWorkTime()) + "'");
        holder.restTime.setText(String.valueOf(setting.getRestTime()) + "'");
        holder.largeRestTime.setText(String.valueOf(setting.getLargeRestTime()) + "'");
        return convertView;
    }

    public RealmResults<Setting> getRealmResults() {
        return realmResults;
    }

}
