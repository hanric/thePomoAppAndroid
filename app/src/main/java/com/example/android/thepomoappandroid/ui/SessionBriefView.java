package com.example.android.thepomoappandroid.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;

/**
 * Created by Enric on 11/04/2015.
 */
public class SessionBriefView extends LinearLayout {

    private static final String CLASS_TAG = SessionBriefView.class.getSimpleName();

    private TextView sbNum;
    private TextView sbHeader;
    private TextView sbDetail;

    public SessionBriefView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SessionBriefView, 0, 0);
        Drawable numColor = a.getDrawable(R.styleable.SessionBriefView_numColor);
        Integer numValue = a.getInteger(R.styleable.SessionBriefView_numValue, -1);
        String description = a.getString(R.styleable.SessionBriefView_description);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.session_brief_layout, this, true);

        sbNum = (TextView) findViewById(R.id.sb_num);
        sbHeader = (TextView) findViewById(R.id.sb_header);
        sbDetail = (TextView) findViewById(R.id.sb_detail);

        sbNum.setText(Integer.toString(numValue));
        sbNum.setBackgroundDrawable(numColor);
        sbHeader.setText(description);
        sbDetail.setText(Integer.toString(numValue) + " " + getResources().getString(R.string.pomodoros));
    }

    public SessionBriefView(Context context) {
        this(context, null);
    }
}
