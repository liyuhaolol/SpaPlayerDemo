package spa.lyh.cn.spaplayerdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.OnStartButtonClickListener;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayer.VideoManager;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.listener.OnStartPositionClickListener;
import spa.lyh.cn.spaplayerdemo.listener.VideoStartListener;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;

public class NewRecyclerViewAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> implements LoadMoreModule {

    private Context context;

    private OnStartPositionClickListener clickListener;


    public NewRecyclerViewAdapter(Context context, @Nullable List<VideoModel> data) {
        super(R.layout.item_videoview,data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, VideoModel videoModel) {
        SpaPlayer spaPlayer = holder.getView(R.id.spaplayer);
        //检查当前player是否存在错位
        SpaPlayer resultPlayer = SpaPlayer.checkPlayer(context,spaPlayer,holder.getLayoutPosition());
        if (resultPlayer != null){
            spaPlayer = resultPlayer;
        }

        ImageLoadUtil.displayImage(context, videoModel.picUrl,spaPlayer.posterImageView);
        spaPlayer.titleTextView.setText(videoModel.title);

        spaPlayer.setOnStartButtonClickListener(new OnStartButtonClickListener() {
            @Override
            public void startButtonClicked(SpaPlayer player) {
                if (clickListener != null){
                    //范例就不判断头的问题了
                    clickListener.startButtonClicked(player,holder.getLayoutPosition());
                }
            }
        });

            /*vHolder.spaPlayer.posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "进详情",Toast.LENGTH_SHORT).show();
                }
            });*/
    }

    public void setVideoPlayClickListener(OnStartPositionClickListener listener){
        this.clickListener = listener;
    }
}
