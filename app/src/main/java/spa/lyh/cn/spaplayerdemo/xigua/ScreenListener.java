package spa.lyh.cn.spaplayerdemo.xigua;

import android.view.View;

public interface ScreenListener {
    void gotoNormalScreen(View player, int position);
    void gotoFullscreen(View player,int position);
}