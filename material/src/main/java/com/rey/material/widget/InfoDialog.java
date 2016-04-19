package com.rey.material.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.rey.material.R;

/**
 * Created by iabdelmenem on 4/29/2015.
 */
public class InfoDialog extends Dialog {
    Context context;
    TextView tvExit;
    TextView tvExitConfirm;
    Button btnYes;
    Button btnNo;

    public InfoDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnNo = (Button) findViewById(R.id.btnNo);
        tvExit = (TextView) findViewById(R.id.tv_exit);
        tvExitConfirm = (TextView) findViewById(R.id.tv_exit_confirm);
    }

    public void setMessage(CharSequence message) {
        tvExitConfirm.setText(message);
    }

    public void setTitle(CharSequence title) {
        tvExit.setText(title);
    }

    public void setPositiveAction(CharSequence ok) {
        btnYes.setText(ok);
    }

    public void setNegativeAction(CharSequence no) {
        btnNo.setText(no);
    }

    public Dialog positiveActionClickListener(View.OnClickListener listener) {
        btnYes.setOnClickListener(listener);
        return this;
    }

    public Dialog NegativeActionClickListener(View.OnClickListener listener) {
        btnNo.setOnClickListener(listener);
        return this;
    }

    public void setNegativeVisible(int visible) {
        this.btnNo.setVisibility(visible);
/*
        RelativeLayout.LayoutParams testLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        testLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
        testLP.addRule(RelativeLayout.BELOW,R.id.tv_exit_confirm);
        testLP.setMargins(24,20,24,5);
        this.btnYes.setLayoutParams(testLP);*/
    }

}