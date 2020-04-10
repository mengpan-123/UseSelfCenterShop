package com.example.selfshopcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PayFailActivity  extends AppCompatActivity {


    private   String reason="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payfail);


        Intent intent = getIntent();                 //获取Intent对象 07
        Bundle bundle = intent.getExtras();

        try {
            reason = bundle.getString("reason");
        }
        catch (Exception ex) {
            //因为有可能是 轮询支付结果的时候失败了走过来，是没有参数的
            reason = "很抱歉，用户支付失败，请重试！";
        }

        TextView edit= findViewById(R.id.ErrReason);

        edit.setText(reason);



        //重新支付的话，需要 修改一下业务单号，否则，传给支付端的业务单号相同的话，无法再次完成支付
        findViewById(R.id.newtopay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PayFailActivity.this, CarItemsActivity.class);
                startActivity(intent);

            }
        });

    }


    /**
     * 返回首页
     */
    public void return_home(View view) {
        Intent intent = new Intent(PayFailActivity.this, IndexActivity.class);
        startActivity(intent);
    }

}
