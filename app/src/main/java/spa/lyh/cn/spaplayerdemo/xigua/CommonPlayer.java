package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JzvdStd;
import spa.lyh.cn.spaplayer.VideoCompleteListener;

public class CommonPlayer extends JzvdStd {

    private VideoCompleteListener listener;


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

    public void setOnCompleteListener(VideoCompleteListener listener){
        this.listener = listener;
    }
}
