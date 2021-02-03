package com.bardisammar.elsalamcity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

public class ProgressDialogCustom {

    private Context context;
    private Dialog dialog;

    public ProgressDialogCustom(Context context) {
        this.context = context;

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void showProgressDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                context.getResources().getDrawable(
                        R.drawable.draw_dialog));
        dialog.setContentView(R.layout.layout_progress);
        dialog.setCancelable(false);
        dialog.show();
    }

    public TextView getTextView()
    {
        return (TextView)dialog.findViewById(R.id.textView1);
    }

    public Dialog getDialog()
    {
        return dialog;
    }

    public void dismissProgressDialog() {
        dialog.dismiss();
    }
}
