package spa.lyh.cn.spaplayer;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

public class VideoManager {
    private SpaPlayer player;
    private ViewGroup vg;

    @SuppressLint("StaticFieldLeak")
    public static VideoManager instance;

    public static VideoManager getInstance(){
        if (instance == null){
            synchronized (VideoManager.class){
                if (instance == null){
                    instance = new VideoManager();
                }
            }
        }
        return instance;
    }

    public VideoManager(){
        player = null;
        vg = null;
    }

    public void setSpaplayer(SpaPlayer player){
        this.player = player;
        this.vg = (ViewGroup) player.getParent();
    }

    public SpaPlayer getSpaPlayer(){
        return player;
    }

    public void clearPlayer(){
        this.player = null;
        this.vg = null;
    }

    public void setViewGroup(ViewGroup vg){
        this.vg = vg;
    }

    public ViewGroup getViewGroup(){
        return vg;
    }
}
