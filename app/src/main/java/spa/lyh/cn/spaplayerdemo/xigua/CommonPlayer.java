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
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if (listener != null){
            listener.onComplete();
        }
    }

    @Override
    public void gotoNormalScreen() {
        //super.gotoNormalScreen();
        if (screenListener != null){
            gobakFullscreenTime = System.currentTimeMillis();//退出全屏
            screenListener.gotoNormalScreen(0);
        }
    }

    @Override
    public void gotoFullscreen() {
        //super.gotoFullscreen();
        if (screenListener != null){
            gotoFullscreenTime = System.currentTimeMillis();
            screenListener.gotoFullscreen(0);
        }
    }

    public void setOnCompleteListener(VideoStatusListener listener){
        this.listener = listener;
    }

    public void setScreenListener(ScreenListener listener){
        this.screenListener = listener;
    }
}
