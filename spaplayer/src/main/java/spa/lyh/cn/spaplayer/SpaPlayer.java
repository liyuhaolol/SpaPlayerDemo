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
    private VideoCompleteListener listener;
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
        time = findViewById(R.id.time);
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpaPlayer.this.mediaInterface != null){
                    if (SpaPlayer.this.mediaInterface.jzvd.state == Jzvd.STATE_NORMAL){
                        Toast.makeText(context, "初始化",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "初始化",Toast.LENGTH_SHORT).show();
                }
                SpaPlayer.super.onClick(v);
            }
        });

        /*posterImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"进入详情",Toast.LENGTH_SHORT).show();
            }
        });*/
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
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if (listener != null){
            listener.onComplete();
        }
    }

    public void setOnCompleteListener(VideoCompleteListener listener){
        this.listener = listener;
    }

}
