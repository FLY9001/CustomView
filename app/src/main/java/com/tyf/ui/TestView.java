package com.tyf.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by 1 on 2017/6/30 0030.
 */

public class TestView extends LinearLayout {
    private void test() {
        /**
         * 坐标
         */
        int left = getLeft();   //相对父容器的四坐标
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();
        int width = right - left;
        int herght = bottom - top;

        float x = getX();       //相对父容器的当前左顶点坐标,及相对四坐标的偏移量，x=left+translationX
        float y = getY();
        float translationX = getTranslationX();
        float translationY = getTranslationY();

        /**
         * 滑动
         */
        scrollBy(10, 10);       //相对滑动，与绝对滑动（针对View中内容的滑动）
        scrollTo(getScrollX() + 10, getScrollY() + 10);
        ObjectAnimator.ofFloat(this, "translationX", 0, 10).setDuration(1000).start();//动画滑动
        ((ViewGroup.MarginLayoutParams) getLayoutParams()).leftMargin += 10;          //参数滑动
        requestLayout();

        /**
         * 动态获取View的宽高
         */
//        public void onWindowFocusChanged(boolean hasWindowFocus) {
//            if (hasWindowFocus) {
//                getWidth();
//            }
//        }
        this.post(new Runnable() {      //view中有post，不需要handler
            @Override
            public void run() {
                getWidth();
            }
        });
        final ViewTreeObserver observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                observer.removeOnGlobalLayoutListener(this);
                getWidth();
            }
        });

    }

//                                 View的初始化
//**************************************************************************************************

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.TestViewStyle);    //0表示不去寻找
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.AppTheme);   //本View默认的Style
    }

    /**
     * 属性优先度：theme直接定义 < defStyleRes < defStyleAttr < xml中style引用 < xml直接定义
     *
     * @param context
     * @param attrs        xml中指定的属性的值
     * @param defStyleAttr 当前主题指定的style
     * @param defStyleRes  View默认指定的style
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TestView, defStyleAttr, defStyleRes);
        int weight = typedArray.getInt(R.styleable.TestView_weight, 1);
        typedArray.recycle();

        //String str_weight = getWeightStatus(weight);
        //setWeight(str_weight);
    }

//                                 View的绘制流程
//**************************************************************************************************

    //performTraversals->
    //测量后，设置自己宽高，performMeasure->measure->onMeasure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//MeasureSpec由SpecMode和SpecSize组成
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),//设置宽高
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));          //此为系统默认得到测量宽高方法，可自定义

        getMeasuredWidth();         //测量宽高（不是最终）
        getMeasuredHeight();
    }

    //布局自己位置后，布局子View，ViewGroup要布局子V，performLayout->layout->onLayout
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //可得到四坐标
        getWidth();                 //最终宽高
        getHeight();
    }

    //绘制背景后，绘制内容，performDraw->draw->onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //在此，可停止线程及动画
    }

//                                 View的事件体系
//**************************************************************************************************

    //分发事件，Activity->Window->ViewGroup->View
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);//请求父容器不拦截事件，但对ACTION_DOWN无效（可内部拦截，解决滑动冲突）
        return super.dispatchTouchEvent(event);
    }

    //拦截事件，默认情况：返回true才会调用onTouchEvent，拦截以后，同一事件序列不会在调用  （可外部拦截，解决滑动冲突）
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    //设置OnTouchListener时，onTouchEvent前先判断onTouch
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        l = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        };
        super.setOnTouchListener(l);
    }

    //处理事件
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();         //事件相对父容器的坐标
        float y = e.getY();
        float rawX = e.getRawX();   //事件的真实坐标（相对手机屏幕）
        float rawY = e.getRawY();

        ViewConfiguration.get(getContext()).getScaledTouchSlop();//滑动的最小距离

        VelocityTracker vt = VelocityTracker.obtain();  //速度追踪
        vt.addMovement(e);
        vt.computeCurrentVelocity(1000);
        int xVelocity = (int) vt.getXVelocity();        //每1000ms滑动了xVelocity个像素
        int yVelocity = (int) vt.getYVelocity();
        vt.clear();                                     //重置并回收
        vt.recycle();

        GestureDetector gd = new GestureDetector(getContext(), null);//手势检测，要实现接口
        gd.setIsLongpressEnabled(false);                //解决长按屏幕后无法拖动
        boolean consume = gd.onTouchEvent(e);           //接管事件
        //return consume;                               //建议监听双击时，运用手势

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(e);
    }

    //若可点击，在ACTION_UP中执行onClick或onLongClick
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        l = new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        super.setOnClickListener(l);
    }

    Scroller mScroller = new Scroller(getContext());

    /**
     * 弹性滑动
     */
    public void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * “内存重启”时，数据的保存与恢复
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }
}
