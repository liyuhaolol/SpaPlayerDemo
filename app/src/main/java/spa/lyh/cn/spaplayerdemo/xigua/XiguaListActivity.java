package spa.lyh.cn.spaplayerdemo.xigua;

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
                Toast.makeText(XiguaListActivity.this,list.get(position).title,Toast.LENGTH_SHORT).show();

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

        /*adapter.setLoadMoreListener(new OnXiguaListLoadmore() {
            @Override
            public void loadMore(int position) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addMinData();
                    }
                }, 100);
            }
        });*/

        adapter.setScreenListener(new ScreenListListener() {
            @Override
            public void gotoNormalScreen(View player, int mainPosition, int secPosition) {
                VideoModel model = minList.get(secPosition);
                list.remove(mainPosition);
                list.add(mainPosition,model);

                //adapter.notifyDataSetChanged();

                /*new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("qwer","执行刷新");
                        adapter.notifyDataSetChanged();
                    }
                },5000);*/
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
                },7000);

            }
        });
        adapter.getLoadMoreModule().setEnableLoadMore(true);

        /*new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //adapter.notifyItemChanged(0);
                Log.e("qwer","执行刷新");
                VideoModel model1 = new VideoModel();
                model1.title = "聪明的小糊涂";
                model1.videoUrl = "http://jzvd.nathen.cn/df6096e7878541cbbea3f7298683fbed/ef76450342914427beafe9368a4e0397-5287d2089db37e62345123a1be272f8b.mp4";
                model1.picUrl = "http://jzvd-pic.nathen.cn/jzvd-pic/ccd86ca1-66c7-4331-9450-a3b7f765424a.png";
                //list.remove(0);
                //list.add(0,model1);
                adapter.notifyDataSetChanged();
                //adapter.notifyItemRemoved(0);
                //adapter.notifyItemInserted(1);
                //loadMore();
            }
        },15000);*/
    }

    private void addData(){
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


    private void addMinData(){
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
