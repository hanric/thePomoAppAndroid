package com.example.android.thepomoappandroid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanric on 23/6/15.
 */
public class UserSearchAdapter extends ArrayAdapter<PersonDTO> {

    private ArrayList<PersonDTO> personDTOs;
    private Filter filter;
    private Context context;
    private UserSearchAdapter userSearchAdapter;

    private class ViewHolder {
        ImageView profileImg;
        TextView profileUsername;
        TextView profileEmail;
    }

    public UserSearchAdapter(Context context, int resId, ArrayList<PersonDTO> personDTOArrayList) {
        super(context, resId, personDTOArrayList);
        this.personDTOs = personDTOArrayList;
        this.userSearchAdapter = this;
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

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new UserFilter();
        }
        return filter;
    }

    private class UserFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PersonDTO> personDTOList = new ArrayList<>(personDTOs);
            FilterResults result = new FilterResults();
            //if no constraint is given, return the whole list
            if (constraint == null || constraint.toString().toLowerCase().length() == 0) {
                result.values = personDTOList;
                result.count = personDTOList.size();
            } else { // iterate over the list of persons and find if the person matches the constraint. if it does, add to the result list
                final ArrayList<PersonDTO> retList = new ArrayList<>();
                for (PersonDTO personDTO : personDTOList) {
                    if (personDTO.getUsername().contains(constraint) || personDTO.getEmail().contains(constraint)) {
                        retList.add(personDTO);
                    }
                }
                result.values = retList;
                result.count = retList.size();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            // we clear the adapter and then pupulate it with the new results
            userSearchAdapter.clear();
            if (filterResults.count > 0) {
                for (PersonDTO personDTO : (ArrayList<PersonDTO>) filterResults.values) {
                    userSearchAdapter.add(personDTO);
                }
            }
            userSearchAdapter.notifyDataSetChanged();
        }
    }
}
