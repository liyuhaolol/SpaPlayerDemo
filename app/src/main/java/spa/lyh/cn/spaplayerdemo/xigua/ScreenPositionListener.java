package spa.lyh.cn.spaplayerdemo.xigua;

import android.view.View;

import spa.lyh.cn.spaplayerdemo.tiktok.VideoModel;

public interface ScreenPositionListener {
    void gotoNormalScreen(View player,VideoModel model,int position);
    void gotoFullscreen(View player,int position);
}
