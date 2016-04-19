package com.rey.material.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.rey.material.R;
import com.rey.material.util.LocaleUtil;
import com.rey.material.util.TypefaceUtil;

/**
 * Created by iabdelmenem on 4/26/2015.
 */
public class ProgressDialog extends Dialog implements Handler.Callback {
    public static final int STYLE_SPINNER = 0;
    /**
     * Creates a ProgressDialog with a horizontal progress bar.
     */
    public static final int STYLE_HORIZONTAL = 1;
    public Context c;
    public Dialog d;
    public Button no;
    int countValue;
    TextView mProgressPercent;
    TextView mProgressNumber;
    ProgressView mProgress;
    int allValue = 0;
    private Handler mHandler;
    float progressValue;
    private static final int MSG_UPDATE_PROGRESS = 1002;
    private static final long PROGRESS_INTERVAL = 7000;
    private static final long PROGRESS_UPDATE_INTERVAL = PROGRESS_INTERVAL / 100;
    private int mProgressStyle = STYLE_SPINNER;
    TextView mMessageView;

    public ProgressDialog(Context a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (mProgressStyle == STYLE_HORIZONTAL) {
            setContentView(R.layout.horizintal_progress);
            mProgress = (ProgressView) findViewById(R.id.loadingBar);
            mProgressPercent = (TextView) findViewById(R.id.tv_progress_present);
            mProgressNumber = (TextView) findViewById(R.id.tv_progress_number);
        } else {
            setContentView(R.layout.circle_progress);
            mMessageView = (TextView) findViewById(R.id.tv_progress);
        }
        mHandler = new Handler(this);
    }


    public void incrementProgressBy(int diff) {
        if (mProgress != null) {
            addProgress(diff);
            onProgressChanged();
        }
    }

    public void addProgress(int value) {
        countValue = value;
        allValue += countValue;
        String allValueAr = TypefaceUtil.getArNum(allValue);
        if (allValue <= 100) {
            if (!LocaleUtil.IsRTL()) {
                mProgressPercent.setText((allValue) + "%");
                mProgressNumber.setText((allValue) + "/100");
            } else {
                mProgressPercent.setText("%" + (allValueAr));
                mProgressNumber.setText(TypefaceUtil.getArNum("100") + "/" + (allValueAr));
            }
        }
        progressValue = (float) value / 100;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {

            case MSG_UPDATE_PROGRESS:
                mProgress.setProgress((float) (mProgress.getProgress() + progressValue));
                break;
        }
        return false;

    }

    public void setMessage(CharSequence message) {
        if (mProgressStyle != STYLE_HORIZONTAL)
            mMessageView.setText(message);
    }

    public void setProgressStyle(int style) {
        mProgressStyle = style;
    }

    private void onProgressChanged() {
        if (mProgressStyle == STYLE_HORIZONTAL) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, PROGRESS_UPDATE_INTERVAL);
            }
        }
    }
}