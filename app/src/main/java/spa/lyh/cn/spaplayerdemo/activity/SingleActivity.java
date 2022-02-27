package spa.lyh.cn.spaplayerdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.jzvd.Jzvd;
import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;

/**
 * 在单独的Activity中演示
 * AndroidManifest中必须添加configChanges
 * keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize|uiMode
 * onBackPressed()
 * onPause()
 * onDestroy()
 * 这三个方法必须重写
 */
public class SingleActivity extends AppCompatActivity {
    SpaPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        player = findViewById(R.id.spaplayer);
        player.setUp(Global.url,"这位小姐姐拍视频");
        ImageLoadUtil.displayImage(this,Global.pic,player.posterImageView);

        //player.startVideo();

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
