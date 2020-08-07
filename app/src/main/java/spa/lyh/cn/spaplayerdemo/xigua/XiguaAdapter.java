package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.jzvd.Jzvd;
import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.listener.VideoStartListener;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;
import spa.lyh.cn.spaplayerdemo.listener.VideoPositionCompleteListener;

public class XiguaAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder>{
    private Context mContext;
    private VideoPositionCompleteListener listener;

    private ScreenListener screenListener;

    private VideoStatusListener statusListener;

    public XiguaAdapter(Context context, @Nullable List<VideoModel> data) {
        super(R.layout.item_xigua_video,data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VideoModel viewModel) {
        CommonPlayer commonPlayer = baseViewHolder.getView(R.id.player);
        if (isVerticalScreen(mContext)){
            //竖屏
            if (commonPlayer.jzDataSource !=null){
                //当前有对应播放数据
                if (!commonPlayer.jzDataSource.containsTheUrl(viewModel.videoUrl)){
                    //当前item的url不一样，初始化
                    commonPlayer.setUp(
                            viewModel.videoUrl,
                            viewModel.title,Jzvd.SCREEN_NORMAL);
                }
            }else {
                //当前没有对应播放数据，初始化
                commonPlayer.setUp(
                        viewModel.videoUrl,
                        viewModel.title,Jzvd.SCREEN_NORMAL);
            }
        }else {
            //横屏
            if (commonPlayer.jzDataSource !=null){
                //当前有对应播放数据
                if (!commonPlayer.jzDataSource.containsTheUrl(viewModel.videoUrl)){
                    //当前item的url不一样，初始化
                    commonPlayer.setUp(
                            viewModel.videoUrl,
                            viewModel.title, Jzvd.SCREEN_FULLSCREEN);
                }
            }else {
                //当前没有对应播放数据，初始化
                commonPlayer.setUp(
                        viewModel.videoUrl,
                        viewModel.title, Jzvd.SCREEN_FULLSCREEN);
            }
        }
        ImageLoadUtil.displayImage(mContext,viewModel.picUrl,commonPlayer.posterImageView);
        int count = getHeaderLayoutCount();
        int pos;
        if (count > 0){
            pos = baseViewHolder.getLayoutPosition()-1;
        }else {
            pos = baseViewHolder.getLayoutPosition();
        }
        commonPlayer.setScreenListener(new ScreenListener() {
            @Override
            public void gotoNormalScreen(View player, int position) {
                if (screenListener != null){
                    screenListener.gotoNormalScreen(player,pos);
                }
            }

            @Override
            public void gotoFullscreen(View player, int position) {
                if (screenListener != null){
                    screenListener.gotoFullscreen(player,pos);
                }
            }
        });

        commonPlayer.setOnStatusListener(new VideoStatusListener() {
            @Override
            public void onStateNormal() {
                if (statusListener != null){
                    statusListener.onStateNormal();
                }
            }

            @Override
            public void onStatePreparing() {
                if (statusListener != null){
                    statusListener.onStatePreparing();
                }

            }

            @Override
            public void onStatePlaying() {
                if (statusListener != null){
                    statusListener.onStatePlaying();
                }
            }

            @Override
            public void onStatePause() {
                if (statusListener != null){
                    statusListener.onStatePause();
                }
            }

            @Override
            public void onStateError() {
                if (statusListener != null){
                    statusListener.onStateError();
                }
            }

            @Override
            public void onComplete() {
                if (statusListener != null){
                    statusListener.onComplete();
                }
            }
        });

    }

    public void setOnCompleteListener(VideoPositionCompleteListener listener){
        this.listener = listener;
    }

    public CommonPlayer getVideoPlayer(int position){
        return (CommonPlayer) getViewByPosition(position,R.id.player);
    }

    /**
     * 判断当前屏幕是否是横屏
     * @param activity
     * @return
     */
    private boolean isVerticalScreen(Context activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public void setScreenListener(ScreenListener listener){
        this.screenListener = listener;
    }

    public void setOnStatusListener(VideoStatusListener listener){
        this.statusListener = listener;
    }

}
