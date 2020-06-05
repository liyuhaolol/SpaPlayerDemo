package spa.lyh.cn.spaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.jzvd.JzvdStd;

public class SpaPlayer extends JzvdStd {
    public SpaPlayer(Context context) {
        this(context,null);
    }

    public SpaPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        posterImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

}
