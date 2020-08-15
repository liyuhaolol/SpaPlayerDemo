package spa.lyh.cn.spaplayerdemo.xigua.player;

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
                /*canLoadMore = true;
                processLoadMore();*/
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
    }

    public VideoModel getFirstItem(){
        if (list == null){
            return null;
        }
        try {
            return list.get(0);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,"唯一能想到的可能性就是数组越界，但是如果真的发生了，不太符合逻辑。需要进一步排查发生原因，我自己靠脑子想不出来这种场景。");
            return null;
        }
    }

    public void refreshFirstData(VideoModel model){
        if (list != null){
            list.remove(0);
            list.add(0,model);
            adapter.notifyDataSetChanged();
        }
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

    /**
     * 检查player是否错位
     */
    public static XiguaPlayer checkPlayer(Context context,XiguaPlayer xiguaPlayer,int position){
        ViewGroup.LayoutParams blockLayoutParams = xiguaPlayer.getLayoutParams();
        int blockIndex;
        XiguaPlayer currentPlayer = XiguaManager.getInstance().getXiguaPlayer();
        ViewGroup oldVg = (ViewGroup) xiguaPlayer.getParent();
        if (oldVg != null){
            if (oldVg.getId() != -1){
                blockIndex = oldVg.indexOfChild(xiguaPlayer);
                if (currentPlayer != null && currentPlayer.currentPosition == position){
                    ViewGroup vg = (ViewGroup) currentPlayer.getParent();
                    if (vg != null){
                        //说明此时，当前播放的view还在某个ViewGroup里
                        if (oldVg.getId() == vg.getId()){
                            //显示播放器父布局，跟实际运行的播放器父布局是一个
                            if (!xiguaPlayer.equals(currentPlayer)){
                                //当前2个player不是同一个东西
                                oldVg.removeView(xiguaPlayer);
                                vg.removeView(currentPlayer);//移除掉当前播放器
                                oldVg.addView(currentPlayer,blockIndex,blockLayoutParams);//添加到当前控制器
                                return currentPlayer;
                            }
                        }else {
                            //当前正在全屏播放，准备替换父类
                            currentPlayer.CONTAINER_LIST.pop();
                            currentPlayer.CONTAINER_LIST.add(oldVg);
                            return currentPlayer;
                        }
                    }else {
                        //当前播放器没有父布局了直接添加
                        oldVg.removeView(xiguaPlayer);
                        oldVg.addView(currentPlayer,blockIndex,blockLayoutParams);//添加到当前控制器
                        return currentPlayer;
                    }
                }else {
                    //当前item不是正在播放的位置
                    if (currentPlayer != null){
                        //当前存在正在播放的播放器
                        ViewGroup vg = (ViewGroup) currentPlayer.getParent();
                        if (vg != null){
                            //说明此时，当前播放的view还在某个ViewGroup里
                            if (oldVg.getId() == vg.getId()){
                                //显示播放器父布局，跟实际运行的播放器父布局是一个
                                if (xiguaPlayer.equals(currentPlayer)){
                                    //当前2个player不是同一个东西
                                    vg.removeView(currentPlayer);//移除掉当前播放器
                                    XiguaPlayer replacePlayer = new XiguaPlayer(context);
                                    oldVg.addView(replacePlayer,blockIndex,blockLayoutParams);//添加到当前控制器
                                    return replacePlayer;
                                }
                            }
                        }
                    }
                    //oldVg.removeView(currentPlayer);
                }
            }else {
                if (!oldVg.toString().contains("DecorView")){
                    Log.e(TAG,"item中作为显示的SpaPlayer的父布局ViewGroup不存在ResId，逻辑逻辑判断无法继续进行，请去xml中对父布局设置id。");
                }
            }
        }
        return null;
    }
}
