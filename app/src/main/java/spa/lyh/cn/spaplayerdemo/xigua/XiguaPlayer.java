package spa.lyh.cn.spaplayerdemo.xigua;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import spa.lyh.cn.spaplayerdemo.R;
import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;

public class XiguaPlayer extends RelativeLayout {
    private Context context;

    private ViewPager2 viewPager;
    private RecyclerView recyInViewpager;

    private XiguaAdapter adapter;

    private List<VideoModel> list;

    public XiguaPlayer(Context context) {
        this(context,null);
    }

    public XiguaPlayer(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public XiguaPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.view_xiguaplayer,this);
        viewPager = findViewById(R.id.viewpager);
        recyInViewpager = (RecyclerView) viewPager.getChildAt(0);
        recyInViewpager.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }

    public void setUp(String url,String pic,String title){
        //初始化列表或者清空列表
        if (list == null){
            list = new ArrayList<>();
        }else {
            list.clear();
        }
        VideoModel model = new VideoModel();
        model.videoUrl = url;
        model.picUrl = pic;
        model.title = title;

        list.add(model);

        if (adapter == null){
            Log.e("qwer","初始化");
            adapter = new XiguaAdapter(context,list);
            viewPager.setAdapter(adapter);
        }else {
            Log.e("qwer","更新数据");
            Jzvd.releaseAllVideos();
            adapter.notifyDataSetChanged();
        }

    }

}
