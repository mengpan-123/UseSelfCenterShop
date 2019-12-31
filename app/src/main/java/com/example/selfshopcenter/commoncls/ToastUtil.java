package com.example.selfshopcenter.commoncls;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selfshopcenter.R;

public class ToastUtil {

    private static final String TAG = "ToastUtil";
    private static Toast toast;


    //如果只想在主线程中弹出自定义toast,则直接调用此方法即可
    public static void showToast(Context context, String titles, String messages) {
        toastProcess(context, titles, messages);
    }

    //如果想在子线程中和子线程中都能使用，则调用此方法即可（前提是在Activity中，因为runOnUiThread属于Activity中的方法）
    public static void showToast1(final Activity context, final String titles, final String messages) {
        if ("main".equals(Thread.currentThread().getName())) {
            toastProcess(context, titles, messages);
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastProcess(context, titles, messages);
                }
            });
        }
    }

    /**
     * 自定义toast
     *
     * @param context  上下文对象
     * @param titles   toast 标题
     * @param messages toast内容
     */
    private static void toastProcess(Context context, String titles, String messages) {

        final Dialog dialog_paycode = new Dialog(context, R.style.myNewsDialogStyle);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.custom_toast, null);
        dialog_paycode.setContentView(view);
        TextView title = view.findViewById(R.id.toast_title);
        TextView text = view.findViewById(R.id.toast_content);
        Button require_close=view.findViewById(R.id.require_close);

        Window window = dialog_paycode.getWindow();
        window.setGravity(Gravity.CENTER);
//        window.setDimAmount(0f);
        title.setText(titles); //toast的标题
        text.setText(messages); //toast内容
        dialog_paycode.show();

        require_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_paycode.dismiss();
            }
        });

    }


}
