package com.example.android.thepomoappandroid.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;

/**
 * Created by Enric on 27/04/2015.
 */
public class GroupViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public ViewHolderClicks listener;

    public CardView cardView;
    public TextView title;
    public TextView description;
    public TextView members;
    public TextView editButton;
    public ImageView image;

    public GroupViewHolder(View view, ViewHolderClicks listener) {
        super(view);
        this.listener = listener;

        cardView = (CardView) view;
        title = (TextView) view.findViewById(R.id.group_title);
        description = (TextView) view.findViewById(R.id.group_description);
        members = (TextView) view.findViewById(R.id.group_members);
        editButton = (TextView) view.findViewById(R.id.group_edit);
        image = (ImageView) view.findViewById(R.id.group_image);

        view.setOnClickListener(this);
        editButton.setOnClickListener(this);
        members.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == editButton.getId()) {
            listener.onEditClick(getPosition());
        } else if (id == members.getId()) {
            listener.onMembersClick(getPosition());
        } else {
            listener.onCardClick(getPosition());
        }
    }

    public interface ViewHolderClicks {
        void onEditClick(int position);
        void onCardClick(int position);
        void onMembersClick(int position);
    }
}
