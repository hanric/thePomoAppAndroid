package com.example.android.thepomoappandroid.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.services.GroupsService;
import com.example.android.thepomoappandroid.ui.activity.GroupActivity;
import com.example.android.thepomoappandroid.ui.activity.MainActivity;
import com.example.android.thepomoappandroid.ui.dialog.MembersDialog;

import java.util.List;

/**
 * Created by Enric on 25/04/2015.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder>
    implements GroupViewHolder.ViewHolderClicks {

    Context context;
    GroupsService.OnDeleteGroup onDeleteGroup;
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
                inflate(R.layout.card_group, viewGroup, false);

        setBackgroundColor((CardView) itemView, i);
        return new GroupViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder viewHolder, int i) {
        GroupDTO groupDTO = groupList.get(i);
        viewHolder.title.setText(groupDTO.getName());
        viewHolder.description.setText(groupDTO.getDescription());
        viewHolder.members.setText(formatMembersText(groupDTO));
        if (context != null && Utils.getInstance().getUserId(context) == groupDTO.getAdminId()) {
            viewHolder.editButton.setVisibility(View.VISIBLE);
            viewHolder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.editButton.setVisibility(View.GONE);
            viewHolder.deleteButton.setVisibility(View.GONE);
        }
//        viewHolder.image.setImageDrawable(); TODO add image or iconIds to server
        setBackgroundColor(viewHolder.cardView, i);

    }

    private void setBackgroundColor(CardView cardView, int i) {
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

    private String formatMembersText(GroupDTO groupDTO) {
        return Integer.toString(groupDTO.getPeople().size()) + " " + context.getResources().getString(R.string.group_card_members);
    }

    public void setOnDeleteGroup(GroupsService.OnDeleteGroup onDeleteGroup) {
        this.onDeleteGroup = onDeleteGroup;
    }

    @Override
    public void onEditClick(int position) {
        Toast.makeText(context, "onEditClick Position: " + Integer.toString(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(final int position) {
        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.delete_group))
                .setPositiveButton(R.string.delete_group_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GroupsService.getInstance().delete(Utils.getInstance().getToken(context), groupList.get(position).getId(), onDeleteGroup);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    /**
     * ----------------------------------------------
     * ----------------------------------------------
     */

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(context, GroupActivity.class);
        ((MainActivity) context).startActivity(intent);
    }

    @Override
    public void onMembersClick(int position) {
        MembersDialog membersDialog = MembersDialog.newInstance(groupList.get(position));
        membersDialog.show(((Activity)context).getFragmentManager(), "dialog");
    }
}
