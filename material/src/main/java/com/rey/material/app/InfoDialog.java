package com.rey.material.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rey.material.R;
import com.rey.material.widget.Button;

/**
 * Created by aabdelwahab on 12/15/13.
 */
@SuppressLint("ValidFragment")
public class InfoDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    Context context;
    com.rey.material.widget.TextView tvExit;
    com.rey.material.widget.TextView tvExitConfirm;
    Button btnYes;
    Button btnNo;
    Dialog dialog;
    String dTitle;
    String dMessage;
    String dPositive;
    String dNegative;
    private View.OnClickListener listener1;
    private View.OnClickListener listener2;
    private DialogListener listener;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnYes) {
            listener.onItemClick(R.id.btnYes);
        } else if (i == R.id.btnNo) {
            listener.onItemClick(R.id.btnNo);
            ;


        } else {
        }

    }


    public interface DialogListener {
        void onItemClick(int id);
    }

    public InfoDialog(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.WHITE));
        // dismiss dialog on click outside
        dialog.setCanceledOnTouchOutside(true);
        btnYes = (Button) dialog.findViewById(R.id.btnYes);
        btnNo = (Button) dialog.findViewById(R.id.btnNo);
        tvExit = (com.rey.material.widget.TextView) dialog.findViewById(R.id.tv_exit);
        tvExitConfirm = (com.rey.material.widget.TextView) dialog.findViewById(R.id.tv_exit_confirm);
        tvExitConfirm.setText(dMessage);
        tvExit.setText(dTitle);
        btnYes.setText(dPositive);
        btnNo.setText(dNegative);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        dialog.show();
        btnNo.setOnClickListener(this);


        return dialog;
    }

    public void setMessage(String message) {
        dMessage = message;
    }

    public void setTitle(String title) {
        dTitle = title;
    }

    public void setPositiveAction(String ok) {
        dPositive = ok;
    }

    public void setNegativeAction(String no) {
        dNegative = no;
    }

    public void positiveActionClickListener(View.OnClickListener listener) {
        listener1 = listener;
    }

    public void NegativeActionClickListener(View.OnClickListener listener) {
        listener2 = listener;
    }

    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    public void setNegativeVisible(int visible) {
        this.btnNo.setVisibility(visible);
    }
}
