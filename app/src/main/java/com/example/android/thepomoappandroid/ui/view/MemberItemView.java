package com.example.android.thepomoappandroid.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;

/**
 * Created by Enric on 26/04/2015.
 */
public class MemberItemView extends LinearLayout {

    private TextView name;
    private TextView admin;

    public MemberItemView(Context context, PersonDTO personDTO, boolean isAdmin) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_popup_members, this, true);

        name = (TextView) findViewById(R.id.members_item_name);
        admin = (TextView) findViewById(R.id.members_item_admin);

        name.setText(personDTO.getEmail());
        if (isAdmin) admin.setVisibility(VISIBLE);
        else admin.setVisibility(INVISIBLE);
    }
}
