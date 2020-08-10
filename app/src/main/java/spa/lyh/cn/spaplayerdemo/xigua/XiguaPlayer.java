package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;

public class XiguaPlayer extends RelativeLayout {
    private Context context;

    private ViewPager2 viewPager;
    private RecyclerView recyInViewpager;

    private XiguaAdapter adapter;

    private List<VideoModel> list;

    private int blockIndex;

    private ViewGroup.LayoutParams blockLayoutParams;

    private boolean canLoadMore;

    private int SCREEN_ORIENTATION;

    private OnXiguaLoadmore listener;

    private ScreenListener sListener;

    public XiguaPlayer(Context context) {
        this(context,null);
    }

    public XiguaPlayer(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public XiguaPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.view_xiguaplayer,this);
        viewPager = findViewById(R.id.viewpager);
        recyInViewpager = (RecyclerView) viewPager.getChildAt(0);
        recyInViewpager.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE){
                    Jzvd.releaseAllVideos();
                    playVideo(position);
                }

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

        //this.setAlpha(0.2f);
    }

    public void setUp(String url,String pic,String title){
        //初始化列表或者清空列表
        if (list == null){
            list = new ArrayList<>();
        }else {
            list.clear();
        }
        VideoModel model = new VideoModel();
        model.videoUrl = url;
        model.picUrl = pic;
        model.title = title;

        list.add(model);
        //list.add(model);

        if (adapter == null){
            adapter = new XiguaAdapter(context,list);
            viewPager.setAdapter(adapter);
            setData();
        }else {
            //Jzvd.releaseAllVideos();
            adapter.notifyDataSetChanged();
        }

    }

    private void setData(){
        adapter.setScreenListener(new ScreenListener() {
            @Override
            public void gotoNormalScreen(View player, int position) {
                //Log.e("qwer","小屏");
                CommonPlayer commonPlayer = (CommonPlayer) player;
                ViewGroup vg = (ViewGroup) (JZUtils.scanForActivity(context)).getWindow().getDecorView();
                vg.removeView(XiguaPlayer.this);
                //player.CONTAINER_LIST.getLast().removeViewAt(blockIndex);//remove block
                /*TextView a = new TextView(context);
                a.setText("测试一下");*/
                commonPlayer.CONTAINER_LIST.getLast().addView(XiguaPlayer.this, blockIndex, blockLayoutParams);
                commonPlayer.CONTAINER_LIST.pop();

                commonPlayer.setScreenNormal();//这块可以放到jzvd中
                JZUtils.showStatusBar(context);
                JZUtils.setRequestedOrientation(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                JZUtils.showSystemUI(context);
                VideoModel model = list.get(position);
                list.clear();
                list.add(model);
                adapter.notifyDataSetChanged();
                canLoadMore = false;
                if (sListener != null){
                    sListener.gotoNormalScreen(XiguaPlayer.this,position);
                }
            }

            @Override
            public void gotoFullscreen(View player, int position) {
                //Log.e("qwer","全屏");
                CommonPlayer commonPlayer = (CommonPlayer) player;
                ViewGroup vg = (ViewGroup) getParent();
                context = vg.getContext();
                blockLayoutParams = getLayoutParams();
                blockIndex = vg.indexOfChild(XiguaPlayer.this);
                vg.removeView(XiguaPlayer.this);
                /*TextView textView = new TextView(context);
                textView.setText("123");
                vg.addView(textView);*/
                commonPlayer.CONTAINER_LIST.add(vg);
                vg = (ViewGroup) (JZUtils.scanForActivity(context)).getWindow().getDecorView();
                ViewGroup.LayoutParams fullLayout = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                vg.addView(XiguaPlayer.this, fullLayout);

                commonPlayer.setScreenFullscreen();
                JZUtils.hideStatusBar(context);
                JZUtils.setRequestedOrientation(context, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
                JZUtils.hideSystemUI(context);//华为手机和有虚拟键的手机全屏时可隐藏虚拟键 issue:1326
                canLoadMore = true;
                processLoadMore();
                if (sListener != null){
                    sListener.gotoFullscreen(XiguaPlayer.this,position);
                }
            }
        });
    }

    private void cloneView(ViewGroup vg){}

    private void playVideo(int position){
        CommonPlayer commonPlayer = adapter.getVideoPlayer(position);
        if (commonPlayer != null){

            commonPlayer.startVideo();
        }else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    playVideo(position);
                }
            },50);
        }
    }

    public void setLoadMoreListener(OnXiguaLoadmore listener){
        this.listener = listener;
    }

    private void loadMore(){
        if (listener != null){
            listener.loadMore();
        }
    }

    public void processLoadMore(){
        if (canLoadMore){
            canLoadMore = false;
            loadMore();
        }
    }

    public void addAll(List<VideoModel> mList){
        if (list != null){
            list.addAll(mList);
        }
    }

    public void loadMoreComplete(){
        adapter.notifyDataSetChanged();
        canLoadMore = true;
    }

    public void loadMoreStop(){
        canLoadMore = false;
    }

    public void setScreenListener(ScreenListener listener){
        this.sListener = listener;
    }

    public void setOnStatusListener(VideoStatusListener listener){
        adapter.setOnStatusListener(listener);
    }

    public Jzvd getVideoPlayer(){
        return (Jzvd) adapter.getViewByPosition(0,R.id.player);
    }
}
