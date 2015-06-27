package com.example.android.thepomoappandroid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;

import java.util.ArrayList;

/**
 * Created by hanric on 24/6/15.
 */
public class UserAdapter extends ArrayAdapter<PersonDTO> {

    private ArrayList<PersonDTO> personDTOs;
    private Context context;

    private class ViewHolder {
        ImageView profileImg;
        TextView profileUsername;
        TextView profileEmail;
    }

    public UserAdapter(Context context, int resId, ArrayList<PersonDTO> personDTOArrayList) {
        super(context, resId, personDTOArrayList);
        this.personDTOs = personDTOArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.layout_user_info, parent, false);

            holder = new ViewHolder();
            holder.profileImg = (ImageView) convertView.findViewById(R.id.profileImg);
            holder.profileUsername = (TextView) convertView.findViewById(R.id.profileUsername);
            holder.profileEmail = (TextView) convertView.findViewById(R.id.profileEmail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PersonDTO personDTO = personDTOs.get(position);

        holder.profileUsername.setText(personDTO.getUsername());
        holder.profileEmail.setText(personDTO.getEmail());
        return convertView;
    }


}
