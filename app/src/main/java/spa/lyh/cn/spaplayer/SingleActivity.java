package spa.lyh.cn.spaplayer;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.RequestOptions;

import cn.jzvd.Jzvd;
import spa.lyh.cn.lib_image.app.ImageLoadUtil;


public class SingleActivity extends AppCompatActivity {
    SpaPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        player = findViewById(R.id.player);
        player.setUp(Global.url,"吸猫视频");
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
