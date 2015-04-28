package com.example.android.thepomoappandroid.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.ui.dialog.MembersDialog;

import java.util.List;

/**
 * Created by Enric on 25/04/2015.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder>
    implements GroupViewHolder.ViewHolderClicks {

    Context context;
    private List<GroupDTO> groupList;

    public GroupAdapter(Context context, List<GroupDTO> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.group_card_layout, viewGroup, false);

        setBackgroundColor((CardView) itemView, i);
        return new GroupViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder viewHolder, int i) {
        GroupDTO groupDTO = groupList.get(i);
        viewHolder.title.setText(groupDTO.getName());
        viewHolder.description.setText(groupDTO.getDescription());
        viewHolder.members.setText(formatMembersText(groupDTO));
//        viewHolder.image.setImageDrawable(); TODO add image or iconIds to server
        setBackgroundColor(viewHolder.cardView, i);

    }

    public void setBackgroundColor(CardView cardView, int i) {
        int module = i%3;
        switch (module) {
            case 0 : cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case 1: cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardSecond));
                break;
            case 2: cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardThird));
                break;
        }
    }

    public String formatMembersText(GroupDTO groupDTO) {
        return Integer.toString(groupDTO.getPeople().size()) + " " + context.getResources().getString(R.string.group_card_members);
    }

    @Override
    public void onEditClick(int position) {
        Toast.makeText(context, "onEditClick Position: " + Integer.toString(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCardClick(int position) {
        Toast.makeText(context, "onCardClick Position: " + Integer.toString(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMembersClick(int position) {
        MembersDialog membersDialog = MembersDialog.newInstance(groupList.get(position));
        membersDialog.show(((Activity)context).getFragmentManager(), "dialog");
    }
}
