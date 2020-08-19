package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.jzvd.Jzvd;
import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.OnReleaseVideoListener;
import spa.lyh.cn.spaplayer.OnStartButtonClickListener;
import spa.lyh.cn.spaplayer.ScreenListener;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.listener.OnStartPositionClickListener;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;
import spa.lyh.cn.spaplayerdemo.listener.VideoPositionCompleteListener;

public class XiguaAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder>{
    private Context mContext;
    private VideoPositionCompleteListener listener;

    private ScreenPositionListener screenPositionListener;

    private VideoStatusListener statusListener;

    private OnStartPositionClickListener clickListener;

    private OnReleaseVideoListener releaseVideoListener;

    public XiguaAdapter(Context context, @Nullable List<VideoModel> data) {
        super(R.layout.item_xigua_video,data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VideoModel viewModel) {
        int count = getHeaderLayoutCount();
        int pos;
        if (count > 0){
            pos = baseViewHolder.getLayoutPosition()-1;
        }else {
            pos = baseViewHolder.getLayoutPosition();
        }
        SpaPlayer spaPlayer = baseViewHolder.getView(R.id.player);
        //内部嵌套，所以这里不应该检查父类
        ImageLoadUtil.displayImage(mContext,viewModel.picUrl,spaPlayer.posterImageView);
        spaPlayer.titleTextView.setText(viewModel.title);

        spaPlayer.setOnStartButtonClickListener(new OnStartButtonClickListener() {
            @Override
            public void startButtonClicked(SpaPlayer player) {
                if (clickListener != null){
                    //范例就不判断头的问题了
                    clickListener.startButtonClicked(player,pos);
                }
            }
        });


        spaPlayer.setScreenListener(new ScreenListener() {
            @Override
            public void gotoNormalScreen(SpaPlayer player) {
                if (screenPositionListener != null){
                    screenPositionListener.gotoNormalScreen(player,pos);
                }
            }

            @Override
            public void gotoFullscreen(SpaPlayer player) {
                if (screenPositionListener != null){
                    screenPositionListener.gotoFullscreen(player,pos);
                }
            }
        });

        spaPlayer.setOnStatusListener(new VideoStatusListener() {
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
            public void onStatePreparingPlaying() {
                if (statusListener != null){
                    statusListener.onStatePreparingPlaying();
                }
            }

            @Override
            public void onStatePreparingChangeUrl() {
                if (statusListener != null){
                    statusListener.onStatePreparingChangeUrl();
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

        spaPlayer.setOnReleaseVideoListener(new OnReleaseVideoListener() {
            @Override
            public void onRelease() {
                if (releaseVideoListener != null){
                    releaseVideoListener.onRelease();
                }
            }
        });

    }

    public void setOnCompleteListener(VideoPositionCompleteListener listener){
        this.listener = listener;
    }

    public SpaPlayer getVideoPlayer(int position){
        return (SpaPlayer) getViewByPosition(position,R.id.player);
    }

    /**
     * 判断当前屏幕是否是横屏
     * @param activity
     * @return
     */
    private boolean isVerticalScreen(Context activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public void setScreenPositionListener(ScreenPositionListener listener){
        this.screenPositionListener = listener;
    }

    public void setOnStatusListener(VideoStatusListener listener){
        this.statusListener = listener;
    }

    public void setVideoPlayClickListener(OnStartPositionClickListener listener){
        this.clickListener = listener;
    }

    public void setOnReleaseVideoListener(OnReleaseVideoListener listener){
        this.releaseVideoListener = listener;
    }

}
