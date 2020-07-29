package spa.lyh.cn.spaplayerdemo.xigua;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;

public class XiguaActivity extends AppCompatActivity {
    XiguaPlayer player;
    String url;
    String pic;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xigua);

        player = findViewById(R.id.xiguaplayer);

        url = Global.url;
        pic = Global.pic;
        title = "聪明的小学神";

        player.setUp(url,pic,title);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("qwer","测试第二次");
                player.setUp("http://jzvd.nathen.cn/63f3f73712544394be981d9e4f56b612/69c5767bb9e54156b5b60a1b6edeb3b5-5287d2089db37e62345123a1be272f8b.mp4","http://jzvd-pic.nathen.cn/jzvd-pic/1d935cc5-a1e7-4779-bdfa-20fd7a60724c.jpg","压爆气球");
            }
        },10000);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.goOnPlayOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
    }
}
