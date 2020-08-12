package spa.lyh.cn.spaplayerdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.OnStartButtonClickListener;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.listener.OnStartPositionClickListener;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;

public class NewNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<VideoModel> list;
    private OnStartPositionClickListener clickListener;

    public NewNewAdapter(Context context, List<VideoModel> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new videoViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_videoview, parent,
                false));;

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        videoViewHolder vHolder = (videoViewHolder) holder;

        ImageLoadUtil.displayImage(context, list.get(position).picUrl,vHolder.spaPlayer.posterImageView);
        vHolder.spaPlayer.titleTextView.setText("聪明的小学神");

        vHolder.spaPlayer.setOnStartButtonClickListener(new OnStartButtonClickListener() {
            @Override
            public void startButtonClicked(SpaPlayer player) {
                if (clickListener != null){
                    //范例就不判断头的问题了
                    clickListener.startButtonClicked(player,vHolder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class videoViewHolder extends RecyclerView.ViewHolder {
        SpaPlayer spaPlayer;

        public videoViewHolder(View itemView) {
            super(itemView);
            spaPlayer = itemView.findViewById(R.id.spaplayer);
        }
    }

    public void setVideoPlayClickListener(OnStartPositionClickListener listener){
        this.clickListener = listener;
    }
}
