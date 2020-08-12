package spa.lyh.cn.spaplayerdemo.xigua;

import android.view.View;

public interface ScreenPositionListener {
    void gotoNormalScreen(View player, int position);
    void gotoFullscreen(View player,int position);
}
