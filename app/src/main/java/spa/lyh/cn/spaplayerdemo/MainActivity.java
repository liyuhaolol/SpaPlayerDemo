package spa.lyh.cn.spaplayerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import spa.lyh.cn.spaplayerdemo.activity.SingleActivity;
import spa.lyh.cn.spaplayerdemo.fragment.FragmentActivity;
import spa.lyh.cn.spaplayerdemo.recyclerview.RecyclerActivity;
import spa.lyh.cn.spaplayerdemo.tiktok.TiktokActivity;
import spa.lyh.cn.spaplayerdemo.web.WebActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1,btn2,btn3,btn4,btn5,btn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn_activity);
        btn1.setOnClickListener(this);
        btn2 = findViewById(R.id.btn_fragment);
        btn2.setOnClickListener(this);
        btn3 = findViewById(R.id.btn_recyclerview);
        btn3.setOnClickListener(this);
        btn4 = findViewById(R.id.btn_floatwindow);
        btn4.setOnClickListener(this);
        btn5 = findViewById(R.id.btn_web);
        btn5.setOnClickListener(this);
        btn6 = findViewById(R.id.btn_tiktok);
        btn6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_activity:
                intent = new Intent(MainActivity.this, SingleActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_fragment:
                intent = new Intent(MainActivity.this, FragmentActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_recyclerview:
                intent = new Intent(MainActivity.this, RecyclerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_floatwindow:
                intent = new Intent(MainActivity.this, RecyclerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_web:
                intent = new Intent(MainActivity.this, WebActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_tiktok:
                intent = new Intent(MainActivity.this, TiktokActivity.class);
                startActivity(intent);
                break;
        }
    }
}