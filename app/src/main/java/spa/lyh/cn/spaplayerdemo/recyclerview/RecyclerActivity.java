package spa.lyh.cn.spaplayerdemo.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoManager;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.adapter.NewNewAdapter;
import spa.lyh.cn.spaplayerdemo.adapter.NewRecyclerViewAdapter;
import spa.lyh.cn.spaplayerdemo.adapter.RecyclerViewAdapter;
import spa.lyh.cn.spaplayerdemo.listener.OnItemClickListener;
import spa.lyh.cn.spaplayerdemo.listener.OnStartPositionClickListener;
import spa.lyh.cn.spaplayerdemo.listener.VideoStartListener;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;


/**
 * 在单独的Activity中演示
 * AndroidManifest中必须添加configChanges
 * keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize|uiMode
 * onBackPressed()
 * onPause()
 * onDestroy()
 * 这三个方法必须重写
 * <p>
 * RecyclerView添加对应的监听，用来停止视频的播放
 */
public class RecyclerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NewRecyclerViewAdapter adapter;

    LinearLayoutManager manager;

    List<VideoModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrtivity_recyclerview);
        recyclerView = findViewById(R.id.recycler);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        list = new ArrayList<>();

        addData();

        adapter = new NewRecyclerViewAdapter(this, list);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = manager.findFirstVisibleItemPosition();
                lastVisibleItem = manager.findLastVisibleItemPosition();
                if (VideoManager.getInstance().getSpaPlayer() != null) {
                    SpaPlayer spaPlayer = VideoManager.getInstance().getSpaPlayer();
                    int position = spaPlayer.playPosition;
                    if (position < firstVisibleItem || position > lastVisibleItem) {
                        //页面滑出了屏幕
                        if (Jzvd.CURRENT_JZVD != null
                                && spaPlayer.jzDataSource != null
                                && spaPlayer.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())
                                && spaPlayer.mediaInterface != null) {
                            JZMediaSystem system = (JZMediaSystem) spaPlayer.mediaInterface;//只是用框架的话，是mediaplayer，没有第三方,如果有第三方，这里需要改
                            if (system.mediaPlayer != null) {
                                if (system.isPlaying()) {
                                    if (Jzvd.CURRENT_JZVD != null &&
                                            Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                                        Jzvd.releaseAllVideos();
                                    }
                                } else {
                                    Jzvd.releaseAllVideos();
                                }
                            } else {
                                if (spaPlayer.isStarted) {
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
                Toast.makeText(RecyclerActivity.this, "初始化", Toast.LENGTH_SHORT).show();
                player.setUp(position, list.get(position).videoUrl, list.get(position).title);
                player.startVideo();
            }
        });


        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("qwer", "添加数据");
                        addData();
                        adapter.notifyDataSetChanged();
                        adapter.getLoadMoreModule().loadMoreComplete();
                    }
                }, 7000);
            }
        });
        adapter.getLoadMoreModule().setEnableLoadMore(true);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("qwer", "执行刷新");
                VideoModel model = new VideoModel();
                model.videoUrl = Global.url;
                model.picUrl = Global.pic;
                model.title = "聪明的小笨蛋";
                list.remove(0);
                list.add(0, model);
                adapter.notifyDataSetChanged();
                //Jzvd.releaseAllVideos();
                //adapter.notifyItemRemoved(0);
                //adapter.notifyItemInserted(1);
                //loadMore();
            }
        }, 15000);
    }

    private void addData() {

        VideoModel model1 = new VideoModel();
        model1.title = "聪明的小学神";
        model1.videoUrl = "http://jzvd.nathen.cn/df6096e7878541cbbea3f7298683fbed/ef76450342914427beafe9368a4e0397-5287d2089db37e62345123a1be272f8b.mp4";
        model1.picUrl = "http://jzvd-pic.nathen.cn/jzvd-pic/ccd86ca1-66c7-4331-9450-a3b7f765424a.png";
        VideoModel model2 = new VideoModel();
        model2.title = "压爆气球";
        model2.videoUrl = "http://jzvd.nathen.cn/63f3f73712544394be981d9e4f56b612/69c5767bb9e54156b5b60a1b6edeb3b5-5287d2089db37e62345123a1be272f8b.mp4";
        model2.picUrl = "http://jzvd-pic.nathen.cn/jzvd-pic/1d935cc5-a1e7-4779-bdfa-20fd7a60724c.jpg";
        VideoModel model3 = new VideoModel();
        model3.title = "美美的做饭";
        model3.videoUrl = "http://jzvd.nathen.cn/35b3dc97fbc240219961bd1fccc6400b/8d9b76ab5a584bce84a8afce012b72d3-5287d2089db37e62345123a1be272f8b.mp4";
        model3.picUrl = "http://jzvd-pic.nathen.cn/jzvd-pic/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg";
        VideoModel model4 = new VideoModel();
        model4.title = "舞也能这么跳";
        model4.videoUrl = "http://jzvd.nathen.cn/384d341e000145fb82295bdc54ecef88/103eab5afca34baebc970378dd484942-5287d2089db37e62345123a1be272f8b.mp4";
        model4.picUrl = "http://jzvd-pic.nathen.cn/jzvd-pic/2adde364-9be1-4864-b4b9-0b0bcc81ef2e.jpg";
        VideoModel model5 = new VideoModel();
        model5.title = "练习瑜伽";
        model5.videoUrl = "http://jzvd.nathen.cn/6340efd1962946ad80eeffd19b3be89c/65b499c0f16e4dd8900497e51ffa0949-5287d2089db37e62345123a1be272f8b.mp4";
        model5.picUrl = "http://jzvd-pic.nathen.cn/jzvd-pic/aaeb5da9-ac50-4712-a28d-863fe40f1fc6.png";

        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);
        list.add(model5);
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
