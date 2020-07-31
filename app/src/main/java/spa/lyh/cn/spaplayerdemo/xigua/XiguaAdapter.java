package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.content.res.Configuration;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.jzvd.Jzvd;
import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;
import spa.lyh.cn.spaplayerdemo.listener.VideoPositionCompleteListener;

public class XiguaAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder>{
    private Context mContext;
    private VideoPositionCompleteListener listener;

    public XiguaAdapter(Context context, @Nullable List<VideoModel> data) {
        super(R.layout.item_xigua_video,data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VideoModel viewModel) {
        CommonPlayer commonPlayer = baseViewHolder.getView(R.id.player);
        if (isVerticalScreen(mContext)){
            //竖屏
            commonPlayer.setUp(
                    viewModel.videoUrl,
                    viewModel.title,Jzvd.SCREEN_NORMAL);
        }else {
            //横屏
            commonPlayer.setUp(
                    viewModel.videoUrl,
                    viewModel.title, Jzvd.SCREEN_FULLSCREEN);
        }
        ImageLoadUtil.displayImage(mContext,viewModel.picUrl,commonPlayer.posterImageView);
        int count = getHeaderLayoutCount();
        int postion;
        if (count > 0){
            postion = baseViewHolder.getLayoutPosition()-1;
        }else {
            postion = baseViewHolder.getLayoutPosition();
        }
/*        commonPlayer.setOnCompleteListener(new VideoStatusListener() {
            @Override
            public void onComplete() {
                if (listener != null){
                    listener.onComplete(postion);
                }
            }
        });*/

    }

    public void setOnCompleteListener(VideoPositionCompleteListener listener){
        this.listener = listener;
    }

    public CommonPlayer getVideoPlayer(int position){
        return (CommonPlayer) getViewByPosition(position,R.id.spaplayer);
    }

    /**
     * 判断当前屏幕是否是横屏
     * @param activity
     * @return
     */
    public static boolean isVerticalScreen(Context activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}
