package spa.lyh.cn.spaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class SpaPlayer extends JzvdStd {
    TextView time;
    Context context;

    public boolean isStarted;

    private VideoStatusListener listener;

    private OnStartButtonClickListener startButtonClickListener;

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
        super.gotoFullscreen();
        //Toast.makeText(getContext(), "全屏",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        isStarted = false;
        if (listener != null){
            listener.onStateNormal();
        }
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        isStarted = true;
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

    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
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
                    Toast.makeText(context, "初始化",Toast.LENGTH_SHORT).show();

                    if (startButtonClickListener != null){
                        startButtonClickListener.startButtonClicked(SpaPlayer.this);
                    }
                }else {
                    super.onClick(v);
                }
            }else {
                Toast.makeText(context, "初始化",Toast.LENGTH_SHORT).show();

                if (startButtonClickListener != null){
                    startButtonClickListener.startButtonClicked(SpaPlayer.this);
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

}
