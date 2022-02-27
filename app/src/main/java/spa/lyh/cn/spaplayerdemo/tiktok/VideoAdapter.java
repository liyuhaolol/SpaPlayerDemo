package spa.lyh.cn.spaplayerdemo.tiktok;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

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
    protected void convert(BaseViewHolder baseViewHolder, VideoModel viewModel) {
        int count = getHeaderLayoutCount();
        int postion;
        if (count > 0){
            postion = baseViewHolder.getLayoutPosition()-1;
        }else {
            postion = baseViewHolder.getLayoutPosition();
        }
        SpaPlayer spaPlayer = baseViewHolder.getView(R.id.spaplayer);

        ImageLoadUtil.displayImage(mContext,viewModel.picUrl,spaPlayer.posterImageView);
        spaPlayer.titleTextView.setText(viewModel.title);

        spaPlayer.setOnStatusListener(new VideoStatusListener() {
            @Override
            public void onStateNormal() {

            }

            @Override
            public void onStatePreparing() {

            }

            @Override
            public void onStatePreparingPlaying() {

            }

            @Override
            public void onStatePreparingChangeUrl() {

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
