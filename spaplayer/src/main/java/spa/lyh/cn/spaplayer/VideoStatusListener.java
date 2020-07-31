package spa.lyh.cn.spaplayer;

public interface VideoStatusListener {
    void onStateNormal();
    void onStatePreparing();
    void onStatePlaying();
    void onStatePause();
    void onStateError();
    void onComplete();
}
