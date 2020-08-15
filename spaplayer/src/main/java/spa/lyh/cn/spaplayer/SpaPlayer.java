package spa.lyh.cn.spaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class SpaPlayer extends JzvdStd {
    private final static String TAG = "SpaPlayer";


    TextView time;
    Context context;

    public boolean isStarted;

    private VideoStatusListener listener;

    private OnReleaseVideoListener releaseVideoListener;

    public int playPosition = -1;

    private boolean canQuit;

    private OnStartButtonClickListener startButtonClickListener;

    private ScreenListener screenListener;

    public SpaPlayer(Context context) {
        this(context,null);
    }

    public SpaPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.spalayout;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        this.context = context;

        time = findViewById(R.id.time);

        startButton.setOnClickListener(this);

        posterImageView.setOnClickListener(this);

    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int posterImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, posterImg, bottomPro, retryLayout);
        time.setVisibility(posterImg);
    }

    @Override
    public void gotoFullscreen() {
        if (screenListener != null){
            gotoFullscreenTime = System.currentTimeMillis();
            screenListener.gotoFullscreen(SpaPlayer.this);
        }else {
            super.gotoFullscreen();
        }
    }

    @Override
    public void gotoNormalScreen() {
        if (screenListener != null){
            gobakFullscreenTime = System.currentTimeMillis();
            screenListener.gotoNormalScreen(SpaPlayer.this);
        }else {
            super.gotoNormalScreen();
        }
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        //Log.e("qwer","onStateNormal");
        if (listener != null){
            listener.onStateNormal();
        }
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        //Log.e("qwer","onStatePreparing");
        isStarted = true;
        VideoManager.getInstance().setSpaplayer(this);
        if (listener != null){
            listener.onStatePreparing();
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        //Log.e("qwer","onStatePlaying");
        if (listener != null){
            listener.onStatePlaying();
        }
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        //Log.e("qwer","onStatePause");
        if (listener != null){
            listener.onStatePause();
        }
    }

    @Override
    public void onStateError() {
        super.onStateError();
        //Log.e("qwer","onStateError");
        if (listener != null){
            listener.onStateError();
        }
    }


    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        //Log.e("qwer","onStateAutoComplete");
        textureViewContainer.removeAllViews();
        if (listener != null){
            listener.onComplete();
        }
    }


    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void reset() {
        //Log.e("qwer","重置");
        super.reset();
        isStarted = false;
        playPosition = -1;
        VideoManager.getInstance().clearPlayer();
        if (releaseVideoListener != null){
            releaseVideoListener.onRelease();
        }
    }

    @Override
    public void onCompletion() {
        //由于框架自带播放结束返回普通屏幕，所以这里做出屏蔽
        if (screen == SCREEN_FULLSCREEN) {
            if (canQuit){
                super.onCompletion();
            }else {
                onStateAutoComplete();
            }
        } else {
            super.onCompletion();
        }
    }

    @Override
    public void setUp(String url, String title) {
        setUp(0,url, title);
    }

    public void setUp(int position,String url, String title) {
        this.playPosition = position;
        super.setUp(url, title);
    }

    @Override
    public void setUp(String url, String title, int screen) {
        setUp(0,url,title,screen);
    }


    public void setUp(int position,String url, String title, int screen) {
        this.playPosition = position;
        super.setUp(url,title,screen);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.poster) {
            if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                startButton.callOnClick();
            }else {
                if (SpaPlayer.this.mediaInterface != null){
                    if (SpaPlayer.this.mediaInterface.jzvd.state == Jzvd.STATE_NORMAL){
                        startButton.callOnClick();
                    }else {
                        super.onClick(v);
                    }
                }else {
                    startButton.callOnClick();
                }
            }
        }else if (i == R.id.start) {
            if (SpaPlayer.this.mediaInterface != null){
                if (SpaPlayer.this.mediaInterface.jzvd.state == Jzvd.STATE_NORMAL){

                    if (startButtonClickListener != null){
                        startButtonClickListener.startButtonClicked(SpaPlayer.this);
                    }else {
                        super.onClick(v);
                    }
                }else {
                    super.onClick(v);
                }
            }else {
                if (startButtonClickListener != null){
                    startButtonClickListener.startButtonClicked(SpaPlayer.this);
                }else {
                    super.onClick(v);
                }
            }
        }else {
            super.onClick(v);
        }
    }

    public void setOnStatusListener(VideoStatusListener listener){
        this.listener = listener;
    }

    public void setOnStartButtonClickListener(OnStartButtonClickListener listener){
        this.startButtonClickListener = listener;
    }

    public void canAutoQuitFullScreen(boolean canOrnot){
        this.canQuit = canOrnot;
    }

    public void setScreenListener(ScreenListener listener){
        this.screenListener = listener;
    }

    public void setOnReleaseVideoListener(OnReleaseVideoListener listener){
        this.releaseVideoListener = listener;
    }

    /**
     * 检查player是否错位
     */
    public static SpaPlayer checkPlayer(Context context,SpaPlayer spaPlayer,int position){
        Log.e("qwer",position+"");
        ViewGroup.LayoutParams blockLayoutParams = spaPlayer.getLayoutParams();
        int blockIndex;
        SpaPlayer currentPlayer = VideoManager.getInstance().getSpaPlayer();
        ViewGroup oldVg = (ViewGroup) spaPlayer.getParent();
        if (oldVg != null){
            if (oldVg.getId() != -1){
                blockIndex = oldVg.indexOfChild(spaPlayer);
                if (currentPlayer != null){
                    if (currentPlayer.playPosition == position){
                        //当前播放位置就是当前item
                        ViewGroup vg = (ViewGroup) currentPlayer.getParent();
                        if (vg != null){
                            //说明此时，当前播放的view还在某个ViewGroup里
                            if (!vg.equals(oldVg)){
                                //两个父类不是一个东西
                                if (oldVg.getId() != vg.getId()){
                                    //当前正在全屏播放，准备替换父类
                                    Log.e("qwer","替换了父类");
                                    currentPlayer.CONTAINER_LIST.pop();
                                    currentPlayer.CONTAINER_LIST.add(oldVg);
                                    VideoManager.getInstance().setViewGroup(oldVg);
                                    return currentPlayer;
                                }else {
                                    //当前id相同，替换播放器
                                    oldVg.removeView(spaPlayer);//这个应该不是当前的播放器，应该无用
                                    vg.removeView(currentPlayer);
                                    oldVg.addView(currentPlayer,blockIndex,blockLayoutParams);
                                    VideoManager.getInstance().setViewGroup(oldVg);
                                }
                            }
                        }
                    }else {
                        //当前item不是正在播放的位置
                        //进入这个分支说明什么：
                        //当前不是全屏播放
                        //oldVg肯定不是DecorView
                        //oldVg肯定包含player
                        //1,当前View并不是正在被播放的view
                        //2,当前View就是正在播放的容器
                        ViewGroup vg = (ViewGroup) currentPlayer.getParent();
                        if (vg != null){
                            //道理上，维护的播放器单例，父类不可能为空，这里用作判断
                            Log.e("qwer","vg:"+vg.toString());
                            Log.e("qwer","oldVg:"+oldVg.toString());
                            Log.e("qwer","inVg:"+VideoManager.getInstance().getViewGroup().toString());
                            if (vg.equals(oldVg)){
                                //两个容器是一个，说明这里已经出现错位问题
                                if (spaPlayer.equals(currentPlayer)){
                                    //严谨判断，两个播放器是否也是同一个东西
                                    if (oldVg.getChildCount() >= blockIndex){
                                        //保证可以得到view
                                        SpaPlayer replacePlayer = new SpaPlayer(context);
                                        replacePlayer.setId(spaPlayer.getId());
                                        View view = oldVg.getChildAt(blockIndex);
                                        if (view instanceof SpaPlayer){
                                            //得到的view是spaplayer时,需要先移除
                                            oldVg.removeView(view);
                                            VideoManager.getInstance().getViewGroup().addView(view,blockIndex,blockLayoutParams);
                                        }
                                        oldVg.addView(replacePlayer,blockIndex,blockLayoutParams);
                                        return replacePlayer;
                                    }else {
                                        Log.e(TAG,"VG的子类数量不对");
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
