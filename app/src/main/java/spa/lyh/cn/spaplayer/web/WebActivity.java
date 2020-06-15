package spa.lyh.cn.spaplayer.web;

import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import spa.lyh.cn.spaplayer.Global;
import spa.lyh.cn.spaplayer.R;

public class WebActivity extends AppCompatActivity {
    WebView web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initWeb();
    }

    private void initWeb(){
        web = findViewById(R.id.web);
        WebSettings webSettings = web.getSettings();
        //webSettings.setBlockNetworkImage(true);
        // 不支持缩放
        webSettings.setSupportZoom(false);

        // 自适应屏幕大小
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //使用缓存
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);

        //DOM Storage
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //启动Autoplay
        //webSettings.setMediaPlaybackRequiresUserGesture(false);
        //对图片大小适配
        webSettings.setUseWideViewPort(true);
        //对文字大小适配
        webSettings.setLoadWithOverviewMode(true);
        // 判断系统版本是不是5.0或之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //让系统不屏蔽混合内容和第三方Cookie
            CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
            webSettings.setMixedContentMode(0);//永远允许
        }
        web.setFocusableInTouchMode(true);

        //启动对js的支持
        webSettings.setJavaScriptEnabled(true);
        web.addJavascriptInterface(new JZCallBack(), "jzvd");
        web.loadUrl("file:///android_asset/jzvd.html");
    }

    public class JZCallBack {

        @JavascriptInterface
        public void adViewJiaoZiVideoPlayer(final int width, final int height, final int top, final int left, final int index) {
            runOnUiThread(() -> {
                if (index == 0) {
                    JzvdStd jzvdStd = new JzvdStd(WebActivity.this);
                    jzvdStd.setUp(Global.url, "饺子骑大马");
                    Glide.with(WebActivity.this)
                            .load(Global.pic)
                            .into(jzvdStd.posterImageView);
                    ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                    layoutParams.y = JZUtils.dip2px(WebActivity.this, top);
                    layoutParams.x = JZUtils.dip2px(WebActivity.this, left);
                    layoutParams.height = JZUtils.dip2px(WebActivity.this, height);
                    layoutParams.width = JZUtils.dip2px(WebActivity.this, width);

                    LinearLayout linearLayout = new LinearLayout(WebActivity.this);
                    linearLayout.addView(jzvdStd);
                    web.addView(linearLayout, layoutParams);
                } else {
                    JzvdStd jzvdStd = new JzvdStd(WebActivity.this);
                    jzvdStd.setUp(Global.url, "饺子失态了");
                    Glide.with(WebActivity.this)
                            .load(Global.pic)
                            .into(jzvdStd.posterImageView);
                    ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                    layoutParams.y = JZUtils.dip2px(WebActivity.this, top);
                    layoutParams.x = JZUtils.dip2px(WebActivity.this, left);
                    layoutParams.height = JZUtils.dip2px(WebActivity.this, height);
                    layoutParams.width = JZUtils.dip2px(WebActivity.this, width);

                    LinearLayout linearLayout = new LinearLayout(WebActivity.this);
                    linearLayout.addView(jzvdStd);
                    web.addView(linearLayout, layoutParams);
                }

            });

        }
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.goOnPlayOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
    }
}
