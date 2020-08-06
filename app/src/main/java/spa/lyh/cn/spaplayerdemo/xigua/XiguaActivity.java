package spa.lyh.cn.spaplayerdemo.xigua;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;

public class XiguaActivity extends AppCompatActivity {
    XiguaPlayer player;

    TextView tv_title;

    List<VideoModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xigua);

        player = findViewById(R.id.xiguaplayer);

        list = new ArrayList<>();

        VideoModel model1 = new VideoModel();
        model1.title = "聪明的小学神";
        model1.videoUrl = "http://jzvd.nathen.cn/df6096e7878541cbbea3f7298683fbed/ef76450342914427beafe9368a4e0397-5287d2089db37e62345123a1be272f8b.mp4";
        model1.picUrl = "http://jzvd-pic.nathen.cn/jzvd-pic/ccd86ca1-66c7-4331-9450-a3b7f765424a.png";

        list.add(model1);

        player.setUp(list.get(0).videoUrl,list.get(0).picUrl,list.get(0).title);

        tv_title = findViewById(R.id.title);

        tv_title.setText(list.get(0).title);

        player.setLoadMoreListener(new OnXiguaLoadmore() {
            @Override
            public void loadMore() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                        player.loadMoreComplete();
                    }
                }, 100);
            }
        });

        player.setScreenListener(new ScreenListener() {
            @Override
            public void gotoNormalScreen(int position) {
                VideoModel model = list.get(position);
                list.clear();
                list.add(model);
                player.setCanLoadMore(false);
                refreshView();
            }

            @Override
            public void gotoFullscreen(int position) {
                player.setCanLoadMore(true);
                player.processLoadMore();
            }
        });
    }

    private void addData(){
        /*VideoModel model1 = new VideoModel();
        model1.title = "聪明的小学神";
        model1.videoUrl = "http://jzvd.nathen.cn/df6096e7878541cbbea3f7298683fbed/ef76450342914427beafe9368a4e0397-5287d2089db37e62345123a1be272f8b.mp4";
        model1.picUrl = "http://jzvd-pic.nathen.cn/jzvd-pic/ccd86ca1-66c7-4331-9450-a3b7f765424a.png";*/
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
        //list.add(model1);
        mList.add(model2);
        mList.add(model3);
        mList.add(model4);
        mList.add(model5);
        list.addAll(mList);
        player.addAll(mList);
    }

    private void refreshView(){

        tv_title.setText(list.get(0).title);
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
