package com.example.selfshopcenter.net;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.selfshopcenter.R;

public class UpdateDialog   extends AlertDialog {

    private Context mContext;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private View view;
    public UpdateDialog(Context context) {
        super(context);
        this.mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置对话框样式
        setStyle();
        //初始化控件
        initView();
    }

    private void initView() {
        view = View.inflate(mContext, R.layout.activity_update,null);
        mTextView = (TextView)view.findViewById(R.id.mTextView);
        mProgressBar = (ProgressBar)view.findViewById(R.id.mProgressBar);
        setContentView(view);
    }

    private void setStyle() {
        //设置对话框不可取消
        this.setCancelable(false);
        //设置触摸对话框外面不可取消
        this.setCanceledOnTouchOutside(false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //获得应用窗口大小
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        //设置对话框居中显示
        layoutParams.gravity = Gravity.CENTER;
        //设置对话框宽度为屏幕的3/5
        layoutParams.width = (displaymetrics.widthPixels/5)*3;
    }

    //设置进度条
    public void setProgress(int progress){
        mTextView.setText(progress+"%");
        mProgressBar.setProgress(progress);
    }

}
