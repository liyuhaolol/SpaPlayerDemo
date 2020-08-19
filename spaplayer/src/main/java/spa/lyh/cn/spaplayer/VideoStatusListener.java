package spa.lyh.cn.spaplayer;

public interface VideoStatusListener {
    void onStateNormal();
    void onStatePreparing();
    void onStatePreparingPlaying();
    void onStatePreparingChangeUrl();
    void onStatePlaying();
    void onStatePause();
    void onStateError();
    void onComplete();
}
