package com.example.a18302.guigu_news;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a18302.guigu_news.activity.GuideActivity;
import com.example.a18302.guigu_news.utils.CacheUtils;

public class SplashActivity extends Activity {

    public static final String START_MAIN = "start_mian";

    private RelativeLayout rl_splashs_root;
    private static final long DURATION_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        rl_splashs_root = findViewById(R.id.rl_splashs_root);

        //动画
        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(DURATION_TIME);
        aa.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(DURATION_TIME);
        scaleAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(DURATION_TIME);
        rotateAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(scaleAnimation);
        set.addAnimation(rotateAnimation);

        rl_splashs_root.startAnimation(set);

        set.setAnimationListener(new MyAnimationListener());
    }

    /**
     * 动画的监听
     */
    class MyAnimationListener implements Animation.AnimationListener {

        //开始播放
        @Override
        public void onAnimationStart(Animation animation) {

        }
        //动画结束
        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent;
            //判断是否进入过主页面
            boolean isStartMain = CacheUtils.getBoolean(SplashActivity.this,START_MAIN);
            if (isStartMain) {
                //如果进入过，则直接进入主页面
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                //进入引导页
                intent = new Intent(SplashActivity.this, GuideActivity.class);
            }
            startActivity(intent);
            SplashActivity.this.finish();
//            Toast.makeText(SplashActivity.this,"动画播放完成",Toast.LENGTH_SHORT).show();
        }
        //动画重复播放
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
