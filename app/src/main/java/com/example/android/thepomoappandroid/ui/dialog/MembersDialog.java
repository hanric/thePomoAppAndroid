package com.example.android.thepomoappandroid.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.ui.view.MemberItemView;

/**
 * Created by Enric on 26/04/2015.
 */
public class MembersDialog extends DialogFragment {

    private GroupDTO groupDTO;
    private LinearLayout linearLayout;

    public static MembersDialog newInstance(GroupDTO groupDTO) {
        MembersDialog membersDialog = new MembersDialog();
        membersDialog.setGroupDTO(groupDTO);
        return membersDialog;
    }

    public void setGroupDTO(GroupDTO groupDTO) {
        this.groupDTO = groupDTO;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_members, container,
                false);
        linearLayout = (LinearLayout) view.findViewById(R.id.popup_members_ll);
        fillViews();
        return view;
    }

    private void fillViews() {
        int adminId = groupDTO.getAdminId();
        for (PersonDTO personDTO : groupDTO.getPeople()) {
            boolean isAdmin = (adminId == personDTO.getId());
            MemberItemView memberItemView = new MemberItemView(getActivity(), personDTO, isAdmin);
            linearLayout.addView(memberItemView);
        }
    }
}
