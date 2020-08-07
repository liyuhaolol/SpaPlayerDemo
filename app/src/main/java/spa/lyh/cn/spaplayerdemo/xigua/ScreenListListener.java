package spa.lyh.cn.spaplayerdemo.xigua;

import android.view.View;

public interface ScreenListListener {
    void gotoNormalScreen(View player,int mainPosition, int secPosition);
    void gotoFullscreen(View player,int position);
}
