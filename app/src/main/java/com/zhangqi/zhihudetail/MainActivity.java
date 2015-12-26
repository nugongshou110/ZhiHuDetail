package com.zhangqi.zhihudetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyScrollView.OnScrollListener{
    //ToolBar
    private Toolbar mToolbar;
    //WebView
    private WebView mWebView;
    //自定义的ScrollView
    private MyScrollView mScrollView;
    //随着ScrollView滑走的View
    private RelativeLayout mUserDetail;
    //固定在顶部的View
    private RelativeLayout mTopUserDetail;
    //底部View
    private LinearLayout ll_bottom;
    private Animation showAnim;
    private Animation dismissAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mToolbar.setTitle("历史上有哪些打脸的故事？");
        setSupportActionBar(mToolbar);
        //设置WebView，加载一个网页
        mWebView.loadUrl("http://www.zhihu.com/question/28057213");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }


    private void initView() {
        mUserDetail = (RelativeLayout) findViewById(R.id.user_detail);
        mTopUserDetail = (RelativeLayout) findViewById(R.id.top_user_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mWebView = (WebView) findViewById(R.id.webview);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mScrollView = (MyScrollView) findViewById(R.id.myscrollview);
        mScrollView.setOnScrollListener(this);
        //当布局中所有的View都测量完后回回调的方法，我们在这个方法中可以拿到View的宽和高
        //在这个方法中调用onScroll是为什么？
        //因为我们要在onScroll中获得mUserDetail距顶部的高度
        //只有在所有的View都测量完后我们才能拿到这个高度值，否则我们拿到的是0
        //所以在onGlobalLayout中调用一下onScroll方法，我们一定可以拿到mUserDetail这个View
        //距离屏幕顶部的距离，从而设置给我们的mTopUserDetail这个View，实现两个View的重合
        findViewById(R.id.container).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(mScrollView.getScrollY());
            }
        });
        showAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_show);
        dismissAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_dismiss);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share){
            Toast.makeText(MainActivity.this,"分享",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_menu){
            Toast.makeText(MainActivity.this,"菜单",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScroll(int scrollY) {
        //在最开始mUserDetail距离屏幕顶部是有一段距离的，而最开始scrollY=0，
        //所以在最开始的时候我们取两者的最大值就可以使两个View重合起来
        //因为我们是在所有的View都测量完毕后调用过onScroll方法的，
        //所以mUserDetail.getTop()得到的值是正确的值
        int userDetailView2Top = Math.max(scrollY, mUserDetail.getTop());
        //调用mTopUserDetail的layout方法，设置其在屏幕上的位置
        mTopUserDetail.layout(0, userDetailView2Top, mTopUserDetail.getWidth(), userDetailView2Top + mTopUserDetail.getHeight());
    }

    @Override
    public void onScrollToTop() {
        if (!ll_bottom.isShown()) {
            ll_bottom.clearAnimation();
            ll_bottom.startAnimation(showAnim);
            ll_bottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onScrollToBottom() {
        if (ll_bottom.isShown()) {
            ll_bottom.clearAnimation();
            ll_bottom.startAnimation(dismissAnim);
            ll_bottom.setVisibility(View.GONE);
        }
    }

}
