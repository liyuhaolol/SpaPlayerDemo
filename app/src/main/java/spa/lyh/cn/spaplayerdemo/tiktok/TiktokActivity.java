package spa.lyh.cn.spaplayerdemo.tiktok;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.listener.VideoPositionCompleteListener;

public class TiktokActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private RecyclerView recyInViewpager;

    private VideoAdapter adapter;

    private List<VideoModel> list;

    private boolean canLoadMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiktok);

        list = new ArrayList<>();

        addData();

        viewPager = findViewById(R.id.viewpager2);
        recyInViewpager = (RecyclerView) viewPager.getChildAt(0);
        recyInViewpager.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Jzvd.releaseAllVideos();


                playVideo(position);

                if (canLoadMore){
                    //可以加载
                    if (position >= list.size()-2){
                        //大于列表倒数第二位
                        canLoadMore = false;
                        loadMore();
                    }
                }

            }
        });


        adapter = new VideoAdapter(this,list);

        viewPager.setAdapter(adapter);

        adapter.setOnCompleteListener(new VideoPositionCompleteListener() {
            @Override
            public void onComplete(int position) {
                if (position < list.size()-1){
                    //列表还没到底
                    viewPager.setCurrentItem(position+1);
                }
            }
        });

        canLoadMore = true;

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



    private void loadMore(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                addData();
                adapter.notifyDataSetChanged();
                //adapter.notifyItemInserted(adapter.getItemCount());
                loadMoreComplete();
            }
        }, 100);
    }


    private void loadMoreComplete(){
        canLoadMore = true;
    }

    private void loadMoreStop(){
        canLoadMore = false;
    }

    private void playVideo(int position){
        SpaPlayer spaPlayer = adapter.getVideoPlayer(position);
        if (spaPlayer != null){

            spaPlayer.setUp(position,list.get(position).videoUrl,list.get(position).title);

            spaPlayer.startVideo();
        }else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    playVideo(position);
                }
            },50);
        }
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
