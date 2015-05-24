package com.example.android.thepomoappandroid.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;

/**
 * Created by Enric on 02/05/2015.
 */
public class SessionViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener,
        View.OnLongClickListener {
    public ViewHolderClicks listener;

    public TextView num;
    public TextView header;
    public TextView detail;
    public TextView startTime;

    public SessionViewHolder(View view, ViewHolderClicks listener) {
        super(view);
        this.listener = listener;

        num = (TextView) view.findViewById(R.id.num);
        header = (TextView) view.findViewById(R.id.header);
        detail = (TextView) view.findViewById(R.id.detail);
        startTime = (TextView) view.findViewById(R.id.startTime);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onViewClick(getPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        listener.onLongViewClick(getPosition());
        return true;
    }

    public interface ViewHolderClicks {
        void onViewClick(int position);
        void onLongViewClick(int position);
    }
}
