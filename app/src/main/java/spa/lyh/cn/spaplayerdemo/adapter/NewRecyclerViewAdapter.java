package spa.lyh.cn.spaplayerdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
        ConstraintLayout cc = holder.getView(R.id.cc);
        SpaPlayer currentPlayer = VideoManager.getInstance().getSpaPlayer();

        if (currentPlayer != null && currentPlayer.playPosition == holder.getLayoutPosition()){
            ViewGroup vg = (ViewGroup) currentPlayer.getParent();
            if (vg instanceof ConstraintLayout){
                //说明此时，当前播放的view还在某个ViewGroup里
                if (!spaPlayer.equals(currentPlayer)){
                    //当前2个player不是同一个东西
                    vg.removeView(currentPlayer);//移除掉当前播放器
                    cc.addView(currentPlayer);//添加到当前控制器
                }
            }else if (vg == null){
                //当前播放器已经被移除了，直接添加
                cc.addView(currentPlayer);//添加到当前控制器
            }
        }else {
            cc.removeView(currentPlayer);
        }

        ImageLoadUtil.displayImage(context, Global.pic,spaPlayer.posterImageView);
        spaPlayer.titleTextView.setText("聪明的小学神");

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
