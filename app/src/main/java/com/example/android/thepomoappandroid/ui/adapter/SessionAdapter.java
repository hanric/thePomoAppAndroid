package com.example.android.thepomoappandroid.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.services.SessionsService;
import com.example.android.thepomoappandroid.ui.adapter.holder.SessionViewHolder;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;

/**
 * Created by Enric on 02/05/2015.
 */
public class SessionAdapter extends RecyclerView.Adapter<SessionViewHolder>
    implements SessionViewHolder.ViewHolderClicks {

    private Context context;
    private Callback<ResponseCallback> onDeleteSessionCallback;
    private List<SessionDTO> sessionList;

    public SessionAdapter(Context context, List<SessionDTO> sessionList) {
        this.context = context;
        this.sessionList = sessionList;
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_session, viewGroup, false);
        return new SessionViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder viewHolder, int i) {
        SessionDTO sessionDTO = sessionList.get(i);
        viewHolder.num.setText(Integer.toString(sessionDTO.getNPomos()));
        setNumBackgroundColor(viewHolder.num, i);
        viewHolder.header.setText(sessionDTO.getName());
        viewHolder.detail.setText(formatDetailText(sessionDTO));
        viewHolder.startTime.setVisibility(View.VISIBLE);
        viewHolder.startTime.setText(sessionDTO.getStartTime());
    }

    private void setNumBackgroundColor(TextView textView, int i) {
        int module = i%9;
        switch (module) {
            case 0 : textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_1));
                break;
            case 1: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_2));
                break;
            case 2: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_3));
                break;
            case 3: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_4));
                break;
            case 4 : textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_5));
                break;
            case 5: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_6));
                break;
            case 6: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_7));
                break;
            case 7: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_8));
                break;
            case 8: textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sb_background_9));
                break;
        }
    }

    private String formatDetailText(SessionDTO sessionDTO) {
        return Integer.toString(sessionDTO.getNPomos()) + " " + context.getResources().getString(R.string.pomodoros);
    }

    public void setOnDeleteSessionCallback(Callback<ResponseCallback> callback) {
        this.onDeleteSessionCallback = callback;
    }

    @Override
    public void onViewClick(int position) {
        // TODO show session detail
    }

    @Override
    public void onLongViewClick(final int position) {
        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.delete_session))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SessionsService.getInstance().delete(Utils.getToken(context), sessionList.get(position).getId(), onDeleteSessionCallback);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
}
