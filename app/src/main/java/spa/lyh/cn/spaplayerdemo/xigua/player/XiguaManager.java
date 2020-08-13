package spa.lyh.cn.spaplayerdemo.xigua.player;


public class XiguaManager {
    private XiguaPlayer player;

    public static XiguaManager instance;

    public static XiguaManager getInstance(){
        if (instance == null){
            synchronized (XiguaManager.class){
                if (instance == null){
                    instance = new XiguaManager();
                }
            }
        }
        return instance;
    }

    public XiguaManager(){
        player = null;
    }

    public void setXiguaplayer(XiguaPlayer player){
        this.player = player;
    }

    public XiguaPlayer getXiguaPlayer(){
        return player;
    }

    public void clearPlayer(){
        this.player = null;
    }
}
