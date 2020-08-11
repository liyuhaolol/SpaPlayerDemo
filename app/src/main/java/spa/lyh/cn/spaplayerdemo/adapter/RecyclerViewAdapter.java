package spa.lyh.cn.spaplayerdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import spa.lyh.cn.lib_image.app.ImageLoadUtil;
import spa.lyh.cn.spaplayer.OnStartButtonClickListener;
import spa.lyh.cn.spaplayer.VideoStatusListener;
import spa.lyh.cn.spaplayerdemo.Global;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayer.SpaPlayer;
import spa.lyh.cn.spaplayerdemo.listener.OnItemClickListener;
import spa.lyh.cn.spaplayerdemo.listener.OnStartPositionClickListener;
import spa.lyh.cn.spaplayerdemo.listener.VideoStartListener;

import static cn.jzvd.Jzvd.STATE_IDLE;
import static cn.jzvd.Jzvd.STATE_NORMAL;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "AdapterRecyclerView";
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    private Context context;

    private VideoStartListener listener;

    private OnStartPositionClickListener clickListener;

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

            ImageLoadUtil.displayImage(context,Global.pic,vHolder.spaPlayer.posterImageView);
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

            /*vHolder.spaPlayer.posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "进详情",Toast.LENGTH_SHORT).show();
                }
            });*/

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

    public void setVideoPlayClickListener(OnStartPositionClickListener listener){
        this.clickListener = listener;
    }
}
