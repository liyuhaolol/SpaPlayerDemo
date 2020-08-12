package spa.lyh.cn.spaplayerdemo.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoManager;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.adapter.RecyclerViewAdapter;
import spa.lyh.cn.spaplayerdemo.listener.OnItemClickListener;
import spa.lyh.cn.spaplayerdemo.listener.OnStartPositionClickListener;
import spa.lyh.cn.spaplayerdemo.listener.VideoStartListener;


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

    LinearLayoutManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrtivity_recyclerview);
        recyclerView = findViewById(R.id.recycler);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, lastVisibleItem;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = manager.findFirstVisibleItemPosition();
                lastVisibleItem = manager.findLastVisibleItemPosition();
                if (VideoManager.getInstance().getSpaPlayer() != null){
                    SpaPlayer spaPlayer = VideoManager.getInstance().getSpaPlayer();
                    int position = spaPlayer.playPosition;
                    if (position < firstVisibleItem || position > lastVisibleItem){
                        //页面滑出了屏幕
                        if (Jzvd.CURRENT_JZVD != null
                                && spaPlayer.jzDataSource != null
                                && spaPlayer.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())
                                && spaPlayer.mediaInterface != null) {
                            JZMediaSystem system = (JZMediaSystem) spaPlayer.mediaInterface;//只是用框架的话，是mediaplayer，没有第三方,如果有第三方，这里需要改
                            if (system.mediaPlayer != null){
                                if (system.isPlaying()){
                                    if (Jzvd.CURRENT_JZVD != null &&
                                            Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                                        Jzvd.releaseAllVideos();
                                    }
                                }else {
                                    Jzvd.releaseAllVideos();
                                }
                            }else {
                                if (spaPlayer.isStarted){
                                    //当前播放器已经被启动
                                    Jzvd.releaseAllVideos();
                                }
                            }
                        }
                    }
                }
            }
        });

        adapter.setVideoPlayClickListener(new OnStartPositionClickListener() {
            @Override
            public void startButtonClicked(SpaPlayer player, int position) {
                Toast.makeText(RecyclerActivity.this, "初始化",Toast.LENGTH_SHORT).show();
                player.setUp(position,Global.url,"聪明的小学神");
                player.startVideo();
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //adapter.notifyItemChanged(0);
                Log.e("qwer","执行刷新");
                adapter.notifyDataSetChanged();
                //Jzvd.releaseAllVideos();
                //adapter.notifyItemRemoved(0);
                //adapter.notifyItemInserted(1);
                //loadMore();
            }
        },5000);
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
