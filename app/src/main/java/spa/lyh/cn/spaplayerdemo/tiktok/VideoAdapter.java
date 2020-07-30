package spa.lyh.cn.spaplayerdemo.tiktok;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoCompleteListener;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;

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
            }else {
                //当前item的url一样，
                /*if (spaPlayer.mediaInterface != null){
                    Log.e("qwer","不为空");
                    if (spaPlayer.state == 7){
                        Log.e("qwer","释放");
                        *//*JZMediaSystem system = (JZMediaSystem) spaPlayer.mediaInterface;
                        system.release();*//*
                        spaPlayer.reset();
                    }
                }else {
                    Log.e("qwer","为空");
                }*/
                /*if (spaPlayer.mediaInterface != null){
                    //媒体入口不为空
                    JZMediaSystem system = (JZMediaSystem) spaPlayer.mediaInterface;
                    if (system.mediaPlayer != null){
                        //播放器不为空
                        if (!system.isPlaying()){
                            //当前没有在播放，初始化
                            spaPlayer.setUp(
                                    viewModel.videoUrl,
                                    viewModel.title);
                            ImageLoadUtil.displayImage(mContext,viewModel.picUrl,spaPlayer.posterImageView);
                        }
                    }else {
                        //播放器为空,初始化
                        spaPlayer.setUp(
                                viewModel.videoUrl,
                                viewModel.title);
                        ImageLoadUtil.displayImage(mContext,viewModel.picUrl,spaPlayer.posterImageView);
                    }
                }else {
                    //媒体入口为空，初始化
                    spaPlayer.setUp(
                            viewModel.videoUrl,
                            viewModel.title);
                    ImageLoadUtil.displayImage(mContext,viewModel.picUrl,spaPlayer.posterImageView);
                }*/
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
        //Log.e("qwer",postion+"");
        spaPlayer.setOnCompleteListener(new VideoCompleteListener() {
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
