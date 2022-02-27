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
 *
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

        adapter = new NewRecyclerViewAdapter(this,list);
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
                    if (spaPlayer.screen == Jzvd.SCREEN_NORMAL){
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
            }
        });

        adapter.setVideoPlayClickListener(new OnStartPositionClickListener() {
            @Override
            public void startButtonClicked(SpaPlayer player, int position) {
                Toast.makeText(RecyclerActivity.this, "初始化",Toast.LENGTH_SHORT).show();
                player.setUp(position,list.get(position).videoUrl,list.get(position).title);
                player.startVideo();
            }
        });


        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("qwer","添加数据");
                        addData();
                        adapter.notifyDataSetChanged();
                        adapter.getLoadMoreModule().loadMoreComplete();
                    }
                },7000);
            }
        });
        adapter.getLoadMoreModule().setEnableLoadMore(true);

/*        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("qwer","执行刷新");
                VideoModel model = new VideoModel();
                model.videoUrl=Global.url;
                model.picUrl = Global.pic;
                model.title = "这位小姐姐拍视频";
                list.remove(0);
                list.add(0,model);
                adapter.notifyDataSetChanged();
                //Jzvd.releaseAllVideos();
                //adapter.notifyItemRemoved(0);
                //adapter.notifyItemInserted(1);
                //loadMore();
            }
        },15000);*/
    }

    private void addData(){

        VideoModel model1 = new VideoModel();
        model1.title = "这位小姐姐拍视频";
        model1.videoUrl = "http://m.newsduan.com/video/VideoSystem/dealVideoSave/2021/4/202104151112379147220.mp4";
        model1.picUrl = "http://res.offshoremedia.net/dxw/image/newsyun/zhuanti/u/cms/www/202104/15111257rt2k.jpg";
        VideoModel model2 = new VideoModel();
        model2.title = "中西文化交流";
        model2.videoUrl = "http://m.newsduan.com/video/transVideo/2022/02/25/1645748814450.mp4";
        model2.picUrl = "http://m.newsduan.com/image/2022-02-25/946709089965182976.jpg";
        VideoModel model3 = new VideoModel();
        model3.title = "老人的警察，谢谢你";
        model3.videoUrl = "http://m.newsduan.com/video/VideoSystem/dealVideoSave/2021/7/20210720143636549229.mp4";
        model3.picUrl = "http://res.offshoremedia.net/dxw/image/newsyun/zhuanti/u/cms/www/202107/20143653cj10.jpg";
        VideoModel model4 = new VideoModel();
        model4.title = "官宣视频来了";
        model4.videoUrl = "http://m.newsduan.com/video/VideoSystem/dealVideoSave/2020/7/202007311023323758351.mp4";
        model4.picUrl = "http://res.offshoremedia.net/dxw/image/newsyun/zhuanti/u/cms/www/202007/31103312gxtv.jpg";
        VideoModel model5 = new VideoModel();
        model5.title = "视频示范稿件";
        model5.videoUrl = "http://m.newsduan.com/video/transVideo/2022/02/22/1645501420938.mp4";
        model5.picUrl = "http://m.newsduan.com/video/videoImg/2022/02/22/1645501420938.jpg";

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
