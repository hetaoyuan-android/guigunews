package com.example.a18302.guigu_news.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 水平方向滑动的viewpager
 */
public class HorizontalScrollViewpager extends ViewPager {
    public HorizontalScrollViewpager(@NonNull Context context) {
        super(context);
    }

    public HorizontalScrollViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 起始坐标
     *
     * @param ev
     * @return
     */
    private float startX;
    private float startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父层视图不拦截，当前控件的事件
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                //记录起始坐标
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //新的坐标
                float endX = ev.getX();
                float endY = ev.getY();
                //计算偏移量
                float distanceX = endX - startX;
                float distanceY = endY - startY;

                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    //水平方向
                    //当我们滑动 到viepager的第0个页面，并且是从左到右滑动
                    if (getCurrentItem() == 0 && distanceX > 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        //当我们滑动到viewpager的最后一个页面，并且是从右到左
                    } else if ((getCurrentItem() == (getAdapter().getCount() - 1)) && distanceX < 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        //其他
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    //竖直方向
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
