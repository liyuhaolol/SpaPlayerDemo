package spa.lyh.cn.spaplayer.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.RequestOptions;

import cn.jzvd.Jzvd;
import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.Global;
import spa.lyh.cn.spaplayer.R;
import spa.lyh.cn.spaplayer.SpaPlayer;

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
        player.setUp(Global.url,"聪明的小学神");
        ImageLoadUtil.displayImage(this,Global.pic,player.posterImageView);

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
