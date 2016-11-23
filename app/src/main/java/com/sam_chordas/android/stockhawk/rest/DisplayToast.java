package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Prateek on 26-09-2016.
 */
public class DisplayToast implements Runnable {
    private final Context mContext;
    String mText;

    public DisplayToast(Context mContext, String text) {
        this.mContext = mContext;
        mText = text;
    }

    public void run() {
        Toast toast = Toast.makeText(mContext, mText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        toast.show();
    }
}