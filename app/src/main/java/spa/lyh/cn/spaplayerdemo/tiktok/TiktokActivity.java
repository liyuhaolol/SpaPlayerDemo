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

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaSystem;
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

/*        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
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
        },5000);*/

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

            spaPlayer.disableBottomContraler();

            spaPlayer.disableTouchContraler();

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
