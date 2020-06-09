package spa.lyh.cn.spaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import spa.lyh.cn.spaplayer.activity.SingleActivity;
import spa.lyh.cn.spaplayer.fragment.FragmentActivity;
import spa.lyh.cn.spaplayer.recyclerview.RecyclerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1,btn2,btn3;

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
        }
    }
}