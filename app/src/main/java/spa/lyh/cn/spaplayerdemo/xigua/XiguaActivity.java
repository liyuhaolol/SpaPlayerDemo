package spa.lyh.cn.spaplayerdemo.xigua;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;
import spa.lyh.cn.spaplayerdemo.xigua.player.XiguaPlayer;

public class XiguaActivity extends AppCompatActivity {
    XiguaPlayer player;

    TextView tv_title;

    //List<VideoModel> list;

    VideoModel videoModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xigua);

        player = findViewById(R.id.xiguaplayer);

        //list = new ArrayList<>();

        videoModel = (VideoModel) getIntent().getSerializableExtra("data");

        player.setUp(videoModel);

        tv_title = findViewById(R.id.title);

        tv_title.setText(videoModel.title);

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

        player.setScreenListener(new ScreenPositionListener() {
            @Override
            public void gotoNormalScreen(View player,VideoModel model, int position) {
                videoModel = model;
                refreshView();
            }

            @Override
            public void gotoFullscreen(View player, int position) {

            }
        });
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

        List<VideoModel> mList = new ArrayList<>();
        mList.add(model1);
        mList.add(model2);
        mList.add(model3);
        mList.add(model4);
        mList.add(model5);
        player.addAll(mList);
    }

    private void refreshView(){

        tv_title.setText(videoModel.title);
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
