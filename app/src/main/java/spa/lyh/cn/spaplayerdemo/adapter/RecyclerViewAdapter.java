package spa.lyh.cn.spaplayerdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayerdemo.listener.VideoStartListener;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "AdapterRecyclerView";
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    private Context context;

    private VideoStartListener listener;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == 0){
            holder = new textViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.item_text, parent,
                    false));
        }else {
            holder = new videoViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.item_videoview, parent,
                    false));
        }

        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof videoViewHolder){
            videoViewHolder vHolder = (videoViewHolder) holder;
            Log.i(TAG, "onBindViewHolder [" + vHolder.spaPlayer.hashCode() + "] position=" + position);

/*            vHolder.spaPlayer.setUp(
                    Global.url,
                    "聪明的小学神");
            ImageLoadUtil.displayImage(context,Global.pic,vHolder.spaPlayer.posterImageView);*/
            if (vHolder.spaPlayer.jzDataSource !=null){
                //当前有对应播放数据
                if (!vHolder.spaPlayer.jzDataSource.containsTheUrl(Global.url)){
                    //当前item的url不一样，初始化
                    vHolder.spaPlayer.setUp(
                            Global.url,
                            "聪明的小学神");
                    ImageLoadUtil.displayImage(context,Global.pic,vHolder.spaPlayer.posterImageView);
                }else {
                    //当前item的url一样，
                }
            }else {
                //当前没有对应播放数据，初始化
                vHolder.spaPlayer.setUp(
                        Global.url,
                        "聪明的小学神");
                ImageLoadUtil.displayImage(context,Global.pic,vHolder.spaPlayer.posterImageView);
            }

            vHolder.spaPlayer.setOnStatusListener(new VideoStatusListener() {
                @Override
                public void onStateNormal() {

                }

                @Override
                public void onStatePreparing() {
                    if (listener != null){
                        listener.onStart(holder.getLayoutPosition());
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

        }else {
            textViewHolder tHolder = (textViewHolder) holder;
            tHolder.item.setText("item"+position);
        }
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class videoViewHolder extends RecyclerView.ViewHolder {
        SpaPlayer spaPlayer;

        public videoViewHolder(View itemView) {
            super(itemView);
            spaPlayer = itemView.findViewById(R.id.spaplayer);
        }
    }

    class textViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        public textViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    public void setVideoStartListener(VideoStartListener listener){
        this.listener = listener;
    }
}
