package spa.lyh.cn.spaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class SpaPlayer extends JzvdStd {
    TextView time;
    Context context;

    public boolean isStarted;

    private VideoStatusListener listener;

    public int playPosition;

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
        VideoManager.getInstance().clearPlayer();
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
}
