package com.example.a18302.guigu_news.activity;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureUtils;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.a18302.guigu_news.MainActivity;
import com.example.a18302.guigu_news.R;
import com.example.a18302.guigu_news.SplashActivity;
import com.example.a18302.guigu_news.utils.CacheUtils;
import com.example.a18302.guigu_news.utils.DensityUtil;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private ViewPager viewpage_guide;
    private Button btn_start_main;
    private LinearLayout ll_point_grp;
    private ArrayList<ImageView> imageViews;
    private ImageView iv_red_point;
    private int leftmax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        viewpage_guide = findViewById(R.id.viewpage_guide);
        btn_start_main = findViewById(R.id.btn_start_main);
        ll_point_grp = findViewById(R.id.ll_point_grp);
        iv_red_point = findViewById(R.id.iv_red_point);

        //准备数据
        int[] ids = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };

        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);
            //创建点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            //点的单位是像素
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(this, 10);
            }
            point.setLayoutParams(params);

            ll_point_grp.addView(point);
        }
        //设置适配器
        viewpage_guide.setAdapter(new MyPagerAdapter());

        //
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobaLayoutListener());

        //viewpager监听页面的改变
        viewpage_guide.addOnPageChangeListener(new MyPageChangeListener());

        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保持曾经进入过的主页面
                CacheUtils.putBoolean(GuideActivity.this,SplashActivity.START_MAIN,true);
                //跳转到主页面
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                //关闭引导页面
                GuideActivity.this.finish();
            }
        });
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * @param i  当前屏幕的位置
         * @param v  页面活动的百分之比
         * @param i1 被选中页面对应的位置
         */
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            int leftrgin = (int) (i * leftmax + (v * leftmax));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftrgin;
            iv_red_point.setLayoutParams(params);
        }

        /**
         * 页面被选中，调用
         *
         * @param i 选中对应的位置
         */

        @Override
        public void onPageSelected(int i) {
            if (i == imageViews.size() - 1) {
                //最后一个页面
                btn_start_main.setVisibility(View.VISIBLE);
            } else {
                btn_start_main.setVisibility(View.GONE);
            }
        }

        /**
         * 滑动状态改变的回调
         *
         * @param i
         */
        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class MyOnGlobaLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            //
            iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            leftmax = ll_point_grp.getChildAt(1).getLeft() - ll_point_grp.getChildAt(0).getLeft();

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

            return view == o;
        }

        //当前创建的视图
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
