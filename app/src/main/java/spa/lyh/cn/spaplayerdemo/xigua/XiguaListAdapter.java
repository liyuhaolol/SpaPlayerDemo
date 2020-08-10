package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.listener.VideoStartListener;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;

public class XiguaListAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> implements LoadMoreModule {
    private Context mContext;

    private VideoStartListener startListener;

    private ScreenListListener sListener;

    private OnXiguaListLoadmore loadmoreListener;



    public XiguaListAdapter(Context context, @Nullable List<VideoModel> data) {
        super(R.layout.item_xigua_list,data);
        mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, VideoModel viewModel) {
        XiguaPlayer player = holder.getView(R.id.player);
        player.setUp(
                viewModel.videoUrl,
                viewModel.picUrl,
                viewModel.title);

        int count = getHeaderLayoutCount();
        int pos;
        if (count > 0){
            pos = holder.getLayoutPosition()-1;
        }else {
            pos = holder.getLayoutPosition();
        }

        player.setOnStatusListener(new VideoStatusListener() {
            @Override
            public void onStateNormal() {

            }

            @Override
            public void onStatePreparing() {
                if (startListener != null){
                    int count = getHeaderLayoutCount();
                    if (count > 0){
                        startListener.onStart(holder.getLayoutPosition()-1);
                    } else {
                        startListener.onStart(holder.getLayoutPosition());
                    }
                }
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

            }
        });

        player.setLoadMoreListener(new OnXiguaLoadmore() {
            @Override
            public void loadMore() {
                if (loadmoreListener != null){
                    loadmoreListener.loadMore(pos);
                }
            }
        });

        player.setScreenListener(new ScreenListener() {
            @Override
            public void gotoNormalScreen(View player, int position) {

            }

            @Override
            public void gotoFullscreen(View player, int position) {

            }
        });
        player.setScreenListener(new ScreenListener() {
            @Override
            public void gotoNormalScreen(View player, int position) {
                if (sListener != null){
                    sListener.gotoNormalScreen(player,pos,position);
                }
            }

            @Override
            public void gotoFullscreen(View player, int position) {
                if (sListener != null){
                    sListener.gotoFullscreen(player,pos);
                }
            }
        });

        TextView title = holder.getView(R.id.text);
        title.setText(viewModel.title);
    }


    public void setVideoStartListener(VideoStartListener listener){
        this.startListener = listener;
    }

    public void setLoadMoreListener(OnXiguaListLoadmore listener){
        this.loadmoreListener = listener;
    }

    public void setScreenListener(ScreenListListener listener){
        this.sListener = listener;
    }

    public XiguaPlayer getVideoPlayer(int position){
        return (XiguaPlayer) getViewByPosition(position,R.id.player);
    }
}
