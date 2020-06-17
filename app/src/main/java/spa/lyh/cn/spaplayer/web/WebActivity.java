package spa.lyh.cn.spaplayer.web;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import spa.lyh.cn.lib_utils.PixelUtils;
import spa.lyh.cn.spaplayer.Global;
import spa.lyh.cn.spaplayer.R;

public class WebActivity extends AppCompatActivity {
    WebView web;
    JzvdStd jzvdStd;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initWeb();
    }

    private void initWeb(){
        web = findViewById(R.id.web);
        jzvdStd = new JzvdStd(WebActivity.this);
        linearLayout = new LinearLayout(WebActivity.this);
        linearLayout.addView(jzvdStd);
        WebSettings webSettings = web.getSettings();
        webSettings.setBlockNetworkImage(true);
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

        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                web.getSettings().setBlockNetworkImage(false);
                if (!web.getSettings().getLoadsImagesAutomatically()) {

                    web.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        });
        //web.loadUrl("file:///android_asset/jzvd.html");
        web.loadUrl("file:///android_asset/test.html?closeAD=true");
    }

    public class JZCallBack {

        @JavascriptInterface
        public void adViewJiaoZiVideoPlayer(final int width, final int height, final int top, final int left, final int index,String videoUrl,String picUrl) {
            Log.e("qwer","调用");
            runOnUiThread(() -> {
                if (linearLayout.getParent() == null){
                    if (index == 0) {
                        Jzvd.releaseAllVideos();
                        jzvdStd.setUp(videoUrl, "饺子骑大马");
                        Glide.with(WebActivity.this)
                                .load(picUrl)
                                .into(jzvdStd.posterImageView);
                        ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                        //Log.e("qwer","top:" + top);
                        layoutParams.y = PixelUtils.dip2px(WebActivity.this, top);
                        //Log.e("qwer","left:" + left);
                        layoutParams.x = PixelUtils.dip2px(WebActivity.this, left);
                        //Log.e("qwer","原始高度:" + height+"转换后的高度:" + PixelUtils.dip2px(WebActivity.this, height));
                        layoutParams.height = PixelUtils.dip2px(WebActivity.this, height);
                        //Log.e("qwer","原始宽度:" + width+"转换后的宽度:" + PixelUtils.dip2px(WebActivity.this, width));
                        layoutParams.width = PixelUtils.dip2px(WebActivity.this, width);

                        web.addView(linearLayout, layoutParams);
                        jzvdStd.startVideo();
                    }
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
