package spa.lyh.cn.spaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    private LinearLayout start_layout;

    private boolean canShowBottom = true;

    private boolean canTouchControl = true;

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

        start_layout = findViewById(R.id.start_layout);

    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int posterImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, posterImg, bottomPro, retryLayout);
        time.setVisibility(posterImg);
    }

    @Override
    public void dissmissControlView() {
        super.dissmissControlView();
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
        Log.e("qwer","onStateNormal");
        if (listener != null){
            listener.onStateNormal();
        }
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        Log.e("qwer","onStatePreparing");
        isStarted = true;
        VideoManager.getInstance().setSpaplayer(this);
        if (listener != null){
            listener.onStatePreparing();
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        Log.e("qwer","onStatePlaying");
        if (listener != null){
            listener.onStatePlaying();
        }
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        Log.e("qwer","onStatePause");
        if (listener != null){
            listener.onStatePause();
        }
    }

    @Override
    public void onStateError() {
        super.onStateError();
        Log.e("qwer","onStateError");
        if (listener != null){
            listener.onStateError();
        }
    }


    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        Log.e("qwer","onStateAutoComplete");
        textureViewContainer.removeAllViews();
        if (listener != null){
            listener.onComplete();
        }
    }

    @Override
    public void onStatePreparingPlaying() {
        super.onStatePreparingPlaying();
        Log.e("qwer","onStatePreparingPlaying");
        if (listener != null){
            listener.onStatePreparingPlaying();
        }
    }

    @Override
    public void onStatePreparingChangeUrl() {
        super.onStatePreparingChangeUrl();
        Log.e("qwer","onStatePreparingChangeUrl");
        if (listener != null){
            listener.onStatePreparingChangeUrl();
        }
    }

    @Override
    public void changeStartButtonSize(int size) {
        super.changeStartButtonSize(size);
        ViewGroup.LayoutParams lp = start_layout.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }

    @Override
    public void onClickUiToggle() {
        if (canShowBottom){
            super.onClickUiToggle();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (canTouchControl){
            return super.onTouch(v, event);
        }else {
            return true;
        }
    }

    @Override
    public void changeUIToPreparingPlaying() {
        if (canShowBottom){
            super.changeUIToPreparingPlaying();
        }else {
            switch (screen) {
                case SCREEN_NORMAL:
                case SCREEN_FULLSCREEN:
                    setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                            View.VISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                    updateStartImage();
                    break;
                case SCREEN_TINY:
                    break;
            }
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

    public void disableBottomContraler(){
        this.canShowBottom = false;
    }

    public void disableTouchContraler(){
        this.canTouchControl = false;
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

    public static SpaPlayer getPlayer(Context context,ViewGroup inVg,int position){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        SpaPlayer currentPlayer = VideoManager.getInstance().getSpaPlayer();
        SpaPlayer spaPlayer = new SpaPlayer(context);
        if (currentPlayer != null){
            if (currentPlayer.playPosition == position){
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
                        currentPlayer.CONTAINER_LIST.pop();
                        currentPlayer.CONTAINER_LIST.add(inVg);
                        return currentPlayer;
                    }
                }else {
                    inVg.removeAllViews();
                    inVg.addView(currentPlayer,layoutParams);
                    return currentPlayer;
                }
            }else {
                inVg.removeAllViews();
                inVg.addView(spaPlayer,layoutParams);
                return spaPlayer;
            }
        }else {
            inVg.removeAllViews();
            inVg.addView(spaPlayer,layoutParams);
            return spaPlayer;
        }
    }
}
