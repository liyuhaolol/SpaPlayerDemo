package spa.lyh.cn.spaplayerdemo.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.adapter.RecyclerViewAdapter;
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

    private int currentPlayPosition = -1;

    private boolean isNotify;

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
            public void onChildViewAttachedToWindow(@NotNull View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(@NotNull View view) {
                int position = recyclerView.getChildViewHolder(view).getLayoutPosition();
                Jzvd jzvd = view.findViewById(R.id.spaplayer);
                if (jzvd != null
                        && Jzvd.CURRENT_JZVD != null
                        && jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())
                        && jzvd.mediaInterface != null) {
                    JZMediaSystem system = (JZMediaSystem) jzvd.mediaInterface;//只是用框架的话，是mediaplayer，没有第三方,如果有第三方，这里需要改
                    if (system.mediaPlayer != null){
                        if (system.isPlaying()){
                            if (Jzvd.CURRENT_JZVD != null &&
                                    Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                                releaseVideo(position);
                            }
                        }else {
                            releaseVideo(position);
                        }
                    }else {
                        releaseVideo(position);
                    }
                }
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    //拖拽状态
                    isNotify = false;
                }
                /*else if (newState == RecyclerView.SCROLL_STATE_SETTLING){
                    Log.e("qwer","回弹状态");
                }else if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    Log.e("qwer","静止状态");
                }*/
            }
        });

        adapter.setVideoStartListener(new VideoStartListener() {
            @Override
            public void onStart(int position) {
                currentPlayPosition = position;
            }
        });

/*        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //adapter.notifyItemChanged(0);
                notifyDataSetChanged();
                //adapter.notifyItemRemoved(0);
                //adapter.notifyItemInserted(1);
                //loadMore();
            }
        },5000);*/
    }

    private void releaseVideo(int viewPosition){
        if (viewPosition == currentPlayPosition ){
            //当view跟播放是同一个的时候在执行操作
            if (!isNotify){
                Jzvd.releaseAllVideos();
            }
        }
    }

    private void notifyDataSetChanged(){
        isNotify = true;
        adapter.notifyDataSetChanged();
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
