package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Intent;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoManager;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;
import spa.lyh.cn.spaplayerdemo.xigua.player.XiguaPlayer;

public class XiguaListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    LinearLayoutManager manager;

    XiguaListAdapter adapter;

    List<VideoModel> list;
    List<VideoModel> minList;

    XiguaPlayer xiguaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrtivity_recyclerview);
        recyclerView = findViewById(R.id.recycler);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        list = new ArrayList<>();

        minList = new ArrayList<>();
        addData();

        adapter = new XiguaListAdapter(this,list);


        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapte, @NonNull View view, int position) {
                Intent intent = new Intent(XiguaListActivity.this,XiguaActivity.class);
                intent.putExtra("data",list.get(position));
                startActivity(intent);

            }
        });

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

        adapter.setLoadMoreListener(new OnXiguaListLoadmore() {
            @Override
            public void loadMore(int position) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addMinData();
                    }
                }, 100);
            }
        });

        adapter.setScreenListener(new ScreenListListener() {
            @Override
            public void gotoNormalScreen(View player, int mainPosition, int secPosition) {
                VideoModel model = minList.get(secPosition);
                list.remove(mainPosition);
                list.add(mainPosition,model);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void gotoFullscreen(View player, int position) {
                xiguaPlayer = (XiguaPlayer) player;
                minList.clear();
                minList.add(list.get(position));
            }
        });


        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                        //adapter.notifyItemInserted(adapter.getItemCount());
                        adapter.notifyDataSetChanged();
                        adapter.getLoadMoreModule().loadMoreComplete();
                    }
                },1000);

            }
        });
        adapter.getLoadMoreModule().setEnableLoadMore(true);

/*        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //adapter.notifyItemChanged(0);
                Log.e("qwer","执行刷新");
                adapter.notifyDataSetChanged();
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


    private void addMinData(){
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


        List<VideoModel> mList = new ArrayList<>();

        mList.add(model1);
        mList.add(model2);
        mList.add(model3);
        mList.add(model4);
        mList.add(model5);
        minList.addAll(mList);
        refreshAdapter(mList);
    }

    private void refreshAdapter(List<VideoModel> list){
        xiguaPlayer.addAll(list);
        xiguaPlayer.loadMoreComplete();
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
