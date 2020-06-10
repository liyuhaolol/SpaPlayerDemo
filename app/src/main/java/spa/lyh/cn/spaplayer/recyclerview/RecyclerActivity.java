package spa.lyh.cn.spaplayer.recyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayer.Global;
import spa.lyh.cn.spaplayer.R;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.adapter.RecyclerViewAdapter;


/**
 * 在单独的Activity中演示
 * AndroidManifest中必须添加configChanges
 * keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize|uiMode
 * onBackPressed()
 * onPause()
 * onDestroy()
 * 这三个方法必须重写
 *
 * RecyclerView添加对应的监听，用来停止视频的播放
 */
public class RecyclerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrtivity_recyclerview);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.spaplayer);
                if (jzvd != null
                        && Jzvd.CURRENT_JZVD != null
                        && jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())
                        && jzvd.mediaInterface != null) {
                    JZMediaSystem system = (JZMediaSystem) jzvd.mediaInterface;//只是用框架的话，是mediaplayer，没有第三方,如果有第三方，这里需要改
                    if (system.mediaPlayer != null
                    && system.isPlaying()){
                        if (Jzvd.CURRENT_JZVD != null &&
                                Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                            Jzvd.releaseAllVideos();//这里一定要使用释放视频，而不是暂停之类的，否则会出现很严重的复用问题
                        }
                    }
                }
            }
        });
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
