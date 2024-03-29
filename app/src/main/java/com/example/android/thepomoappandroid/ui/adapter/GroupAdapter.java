package com.example.android.thepomoappandroid.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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
import com.example.android.thepomoappandroid.ui.adapter.holder.GroupViewHolder;
import com.example.android.thepomoappandroid.ui.dialog.EditGroupDialog;
import com.example.android.thepomoappandroid.ui.dialog.MembersDialog;
import com.example.android.thepomoappandroid.ui.fragment.GroupFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;

/**
 * Created by Enric on 25/04/2015.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder>
    implements GroupViewHolder.ViewHolderClicks {

    private GroupFragment groupFragment;
    private Context context;
    private Callback<ResponseCallback> onDeleteGroupCallback;
    private List<GroupDTO> groupList;

    public GroupAdapter(GroupFragment groupFragment, List<GroupDTO> groupList) {
        this.groupFragment = groupFragment;
        this.context = groupFragment.getActivity();
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
        if (context != null && Utils.getUserId(context) == groupDTO.getAdminId()) {
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
        int module = i%5;
        switch (module) {
            case 0 : cardView.setCardBackgroundColor(context.getResources().getColor(R.color.card_1));
                break;
            case 1: cardView.setCardBackgroundColor(context.getResources().getColor(R.color.card_2));
                break;
            case 2: cardView.setCardBackgroundColor(context.getResources().getColor(R.color.card_3));
                break;
            case 3: cardView.setCardBackgroundColor(context.getResources().getColor(R.color.card_4));
                break;
            case 4: cardView.setCardBackgroundColor(context.getResources().getColor(R.color.card_5));
                break;
        }
    }

    private String formatMembersText(GroupDTO groupDTO) {
        return Integer.toString(groupDTO.getPeople().size()) + " " + context.getResources().getString(R.string.group_card_members);
    }

    public void setOnDeleteGroupCallback(Callback<ResponseCallback> callback) {
        this.onDeleteGroupCallback = callback;
    }

    @Override
    public void onEditClick(int position) {
        EditGroupDialog.newInstance(groupFragment, groupList.get(position)).show(((FragmentActivity) context).getFragmentManager(), "dialog");
    }

    @Override
    public void onDeleteClick(final int position) {
        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.delete_group))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GroupsService.getInstance().delete(Utils.getToken(context), groupList.get(position).getId(), onDeleteGroupCallback);
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
        GroupDTO groupDTO = groupList.get(position);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonGroup = gson.toJson(groupDTO);
        intent.putExtra(GroupActivity.EXTRA_GROUP, jsonGroup);
        context.startActivity(intent);
    }

    @Override
    public void onMembersClick(int position) {
        MembersDialog membersDialog = MembersDialog.newInstance(groupList.get(position));
        membersDialog.show(((Activity)context).getFragmentManager(), "dialog");
    }
}
