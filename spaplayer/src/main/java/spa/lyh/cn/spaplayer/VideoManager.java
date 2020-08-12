package spa.lyh.cn.spaplayer;

public class VideoManager {
    private SpaPlayer player;

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
    }

    public void setSpaplayer(SpaPlayer player){
        this.player = player;
    }

    public SpaPlayer getSpaPlayer(){
        return player;
    }

    public void clearPlayer(){
        this.player = null;
    }
}
