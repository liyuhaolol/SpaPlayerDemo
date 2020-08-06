package spa.lyh.cn.spaplayerdemo.tiktok;

import android.content.Context;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.listener.VideoPositionCompleteListener;

public class VideoAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder>{
    private Context mContext;
    private VideoPositionCompleteListener listener;

    public VideoAdapter(Context context,@Nullable List<VideoModel> data) {
        super(R.layout.item_tiktok_video,data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VideoModel viewModel) {
        SpaPlayer spaPlayer = baseViewHolder.getView(R.id.spaplayer);
        if (spaPlayer.jzDataSource !=null){
            //当前有对应播放数据
            if (!spaPlayer.jzDataSource.containsTheUrl(viewModel.videoUrl)){
                //当前item的url不一样，初始化
                spaPlayer.setUp(
                        viewModel.videoUrl,
                        viewModel.title);
                ImageLoadUtil.displayImage(mContext,viewModel.picUrl,spaPlayer.posterImageView);
            }
        }else {
            //当前没有对应播放数据，初始化
            spaPlayer.setUp(
                    viewModel.videoUrl,
                    viewModel.title);
            ImageLoadUtil.displayImage(mContext,viewModel.picUrl,spaPlayer.posterImageView);
        }

        spaPlayer.titleTextView.setText(viewModel.title);
        int count = getHeaderLayoutCount();
        int postion;
        if (count > 0){
            postion = baseViewHolder.getLayoutPosition()-1;
        }else {
            postion = baseViewHolder.getLayoutPosition();
        }

        spaPlayer.setOnStatusListener(new VideoStatusListener() {
            @Override
            public void onStateNormal() {

            }

            @Override
            public void onStatePreparing() {
            }

            @Override
            public void onStatePlaying() {

            }

            @Override
            public void onStatePause() {

            }

            @Override
            public void onStateError() {

            }

            @Override
            public void onComplete() {
                if (listener != null){
                    listener.onComplete(postion);
                }
            }
        });

    }


    public void setOnCompleteListener(VideoPositionCompleteListener listener){
        this.listener = listener;
    }

    public SpaPlayer getVideoPlayer(int position){
        return (SpaPlayer) getViewByPosition(position,R.id.spaplayer);
    }
}
