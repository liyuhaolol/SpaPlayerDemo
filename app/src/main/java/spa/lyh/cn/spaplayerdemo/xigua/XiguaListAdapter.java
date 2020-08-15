package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;
import spa.lyh.cn.spaplayerdemo.xigua.player.XiguaPlayer;

public class XiguaListAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> implements LoadMoreModule {
    private Context mContext;

    private ScreenListListener sListener;

    private OnXiguaListLoadmore loadmoreListener;



    public XiguaListAdapter(Context context, @Nullable List<VideoModel> data) {
        super(R.layout.item_xigua_list,data);
        mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, VideoModel viewModel) {
        int count = getHeaderLayoutCount();
        int pos;
        if (count > 0){
            pos = holder.getLayoutPosition()-1;
        }else {
            pos = holder.getLayoutPosition();
        }
        Log.e("qwer",pos+"");

        XiguaPlayer player = holder.getView(R.id.player);

        //检查当前player是否存在错位
        XiguaPlayer resultPlayer = XiguaPlayer.checkPlayer(mContext,player,pos);
        if (resultPlayer != null){
            player = resultPlayer;
        }
        player.showView(
                pos,
                viewModel.videoUrl,
                viewModel.picUrl,
                viewModel.title);
        if (!viewModel.equals(player.getFirstItem())){
            player.refreshFirstData(viewModel);
        }

        player.setLoadMoreListener(new OnXiguaLoadmore() {
            @Override
            public void loadMore() {
                if (loadmoreListener != null){
                    loadmoreListener.loadMore(pos);
                }
            }
        });
        player.setScreenListener(new ScreenPositionListener() {
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


    public void setLoadMoreListener(OnXiguaListLoadmore listener){
        this.loadmoreListener = listener;
    }

    public void setScreenListener(ScreenListListener listener){
        this.sListener = listener;
    }

}
