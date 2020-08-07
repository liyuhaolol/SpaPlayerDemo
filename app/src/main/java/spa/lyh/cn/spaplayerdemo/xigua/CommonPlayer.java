package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import cn.jzvd.JzvdStd;
import spa.lyh.cn.spaplayer.VideoStatusListener;

public class CommonPlayer extends JzvdStd {

    private VideoStatusListener listener;

    private ScreenListener screenListener;

    public CommonPlayer(Context context) {
        this(context,null);
    }

    public CommonPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void gotoNormalScreen() {
        //super.gotoNormalScreen();
        if (screenListener != null){
            gobakFullscreenTime = System.currentTimeMillis();//退出全屏
            screenListener.gotoNormalScreen(this,0);
        }
    }

    @Override
    public void gotoFullscreen() {
        //super.gotoFullscreen();
        if (screenListener != null){
            gotoFullscreenTime = System.currentTimeMillis();
            screenListener.gotoFullscreen(this,0);
        }
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        if (listener != null){
            listener.onStateNormal();
        }
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if (listener != null){
            listener.onStatePreparing();
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if (listener != null){
            listener.onStatePlaying();
        }
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        if (listener != null){
            listener.onStatePause();
        }
    }

    @Override
    public void onStateError() {
        super.onStateError();
        if (listener != null){
            listener.onStateError();
        }
    }


    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        textureViewContainer.removeAllViews();
        if (listener != null){
            listener.onComplete();
        }
    }



    public void setScreenListener(ScreenListener listener){
        this.screenListener = listener;
    }

    public void setOnStatusListener(VideoStatusListener listener){
        this.listener = listener;
    }
}
