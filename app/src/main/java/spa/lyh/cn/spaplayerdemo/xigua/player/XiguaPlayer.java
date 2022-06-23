package spa.lyh.cn.spaplayerdemo.xigua.player;

import static androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayer.OnReleaseVideoListener;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoManager;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.listener.OnStartPositionClickListener;
import spa.lyh.cn.spaplayerdemo.test.widget.ViewPager3;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;
import spa.lyh.cn.spaplayerdemo.xigua.OnXiguaLoadmore;
import spa.lyh.cn.spaplayerdemo.xigua.ScreenPositionListener;
import spa.lyh.cn.spaplayerdemo.xigua.XiguaAdapter;

public class XiguaPlayer extends RelativeLayout {

    private final static String TAG = "XiguaPlayer";

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

    private ScreenPositionListener sListener;

    private int currentPosition;

    public static LinkedList<ViewGroup> CONTAINER_LIST = new LinkedList<>();

    public ViewPager3.LinearLayoutManagerImpl manager;

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
        //viewPager.setUserInputEnabled(false);
        /*LinearLayoutManager manager = new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                if (list.size() > 1){
                    return true;
                }
                return false;
            }
        };*/
        //manager = (ViewPager3.LinearLayoutManagerImpl) recyInViewpager.getLayoutManager();
        //recyInViewpager.setLayoutManager(manager);

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
    }

    public void setUp(String url,String pic,String title){
        setUp(0,url,pic,title);

    }

    public void setUp(int position,String url,String pic,String title){
        //初始化列表或者清空列表

        this.currentPosition = position;
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
        checkScroll();

        if (adapter == null){
            adapter = new XiguaAdapter(context,list);
            viewPager.setAdapter(adapter);
            setData();
        }else {
            adapter.notifyDataSetChanged();
        }

    }

    public void showView(int position,String url,String pic,String title){
        this.currentPosition = position;
        if (list == null){
            list = new ArrayList<>();
            VideoModel model = new VideoModel();
            model.videoUrl = url;
            model.picUrl = pic;
            model.title = title;

            list.add(model);
            checkScroll();
            adapter = new XiguaAdapter(context,list);
            viewPager.setAdapter(adapter);
            setData();
        }
    }

    private void setData(){
        adapter.setScreenPositionListener(new ScreenPositionListener() {
            @Override
            public void gotoNormalScreen(View player, int position) {
                //Log.e("qwer","小屏");
                SpaPlayer spaPlayer = (SpaPlayer) player;
                ViewGroup vg = (ViewGroup) (JZUtils.scanForActivity(context)).getWindow().getDecorView();
                vg.removeView(XiguaPlayer.this);
                CONTAINER_LIST.getLast().addView(XiguaPlayer.this, blockIndex, blockLayoutParams);
                CONTAINER_LIST.pop();
                spaPlayer.CONTAINER_LIST.pop();
                spaPlayer.setScreenNormal();
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
                SpaPlayer spaPlayer = (SpaPlayer) player;
                ViewGroup vg = (ViewGroup) getParent();
                context = vg.getContext();
                blockLayoutParams = getLayoutParams();
                blockIndex = vg.indexOfChild(XiguaPlayer.this);
                vg.removeView(XiguaPlayer.this);
                CONTAINER_LIST.add(vg);
                ///测试代码
                ViewGroup inVg = (ViewGroup) spaPlayer.getParent();
                spaPlayer.CONTAINER_LIST.add(inVg);
                ///
                vg = (ViewGroup) (JZUtils.scanForActivity(context)).getWindow().getDecorView();
                ViewGroup.LayoutParams fullLayout = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                vg.addView(XiguaPlayer.this, fullLayout);

                spaPlayer.setScreenFullscreen();
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

        adapter.setVideoPlayClickListener(new OnStartPositionClickListener() {
            @Override
            public void startButtonClicked(SpaPlayer player, int position) {
                player.setUp(currentPosition, list.get(position).videoUrl,list.get(position).title);
                player.startVideo();
            }
        });

        adapter.setOnStatusListener(new VideoStatusListener() {
            @Override
            public void onStateNormal() {

            }

            @Override
            public void onStatePreparing() {
                XiguaManager.getInstance().setXiguaplayer(XiguaPlayer.this);
            }

            @Override
            public void onStatePreparingPlaying() {

            }

            @Override
            public void onStatePreparingChangeUrl() {

            }

            @Override
            public void onStatePlaying() {

            }

            @Override
            public void onStatePause() {

            }

            @Override
            public void onStateError() {

            }

            @Override
            public void onComplete() {

            }
        });
        adapter.setOnReleaseVideoListener(new OnReleaseVideoListener() {
            @Override
            public void onRelease() {
                XiguaManager.getInstance().clearPlayer();
            }
        });
    }

    private void playVideo(int position){
         SpaPlayer spaPlayer = adapter.getVideoPlayer(position);
        if (spaPlayer != null){

            spaPlayer.setUp(currentPosition,list.get(position).videoUrl,list.get(position).title,Jzvd.SCREEN_FULLSCREEN);

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
        checkScroll();
    }

    public void checkScroll(){
        /*if (list != null && manager != null){
            if (list.size() > 1){
                manager.setCanScroll(true);
            }else {
                manager.setCanScroll(false);
            }
        }*/
    }

    public void loadMoreComplete(){
        adapter.notifyDataSetChanged();
        canLoadMore = true;
    }

    public void loadMoreStop(){
        canLoadMore = false;
    }

    public void setScreenListener(ScreenPositionListener listener){
        this.sListener = listener;
    }


    public static XiguaPlayer getPlayer(Context context,ViewGroup inVg,int position){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        XiguaPlayer currentPlayer = XiguaManager.getInstance().getXiguaPlayer();
        XiguaPlayer xiguaPlayer = new XiguaPlayer(context);
        if (currentPlayer != null){
            if (currentPlayer.currentPosition == position){
                ViewGroup vg = (ViewGroup) currentPlayer.getParent();
                if (vg != null){
                    if (vg.getId() != -1){
                        //非全屏状态
                        vg.removeView(currentPlayer);
                        inVg.removeAllViews();
                        inVg.addView(currentPlayer,layoutParams);
                        return currentPlayer;
                    }else {
                        //应该是全屏状态
                        CONTAINER_LIST.pop();
                        CONTAINER_LIST.add(inVg);
                        return currentPlayer;
                    }
                }else {
                    inVg.removeAllViews();
                    inVg.addView(currentPlayer,layoutParams);
                    return currentPlayer;
                }
            }else {
                inVg.removeAllViews();
                inVg.addView(xiguaPlayer,layoutParams);
                return xiguaPlayer;
            }
        }else {
            inVg.removeAllViews();
            inVg.addView(xiguaPlayer,layoutParams);
            return xiguaPlayer;
        }
    }

}
