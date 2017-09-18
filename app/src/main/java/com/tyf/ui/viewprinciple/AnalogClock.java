package com.tyf.ui.viewprinciple;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import com.tyf.ui.R;

import java.util.TimeZone;

/**
 * Created by 1 on 2017/3/1 0001.
 */

public class AnalogClock extends View {
    private static final String TAG = "AnalogClock";
    private Time mCalendar;//用来记录当前时间

    //用来存放三张图片资源
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mDial;

    private int mDialWidth;
    private int mDialHeight;

    //在View attached到Window时注册监听器，在View从Window中剥离时，解除注册
    private boolean mAttached;

    private float mHour;
    private float mMinutes;
    private float mSeconds;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onTimeChanged();
            mhandler.sendEmptyMessageDelayed(0, 1000);
        }
    };


    /**
     * 用于JAVA代码中实例化
     *
     * @param context
     */
    public AnalogClock(Context context) {
        this(context, null);
    }

    /**
     * 用于xml中定义
     *
     * @param context
     * @param attrs   xml中定义的属性的值
     */
    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr 主题中的某个style属性名，如：R.attr.autoCompleteTextViewStyle
     */
    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * 属性优先度：theme直接定义 < defStyleRes < defStyleAttr < xml中style引用 < xml直接定义
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes  内置于View的style，如：R.style.xxx
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        /**
         * 自定义view的属性
         */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnalogClock, defStyleAttr, defStyleRes);
        int custom_color = typedArray.getColor(R.styleable.AnalogClock_custom_color, Color.BLACK);
        typedArray.recycle();

        if (mDial == null) {
            mDial = context.getDrawable(R.drawable.clock_dial);
        }
        if (mHourHand == null) {
            mHourHand = context.getDrawable(R.drawable.clock_hand_hour);
        }
        if (mMinuteHand == null) {
            mMinuteHand = context.getDrawable(R.drawable.clock_hand_minute);
        }
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            //这里确定我们要监听的三种系统广播
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            getContext().registerReceiver(mIntentReceiver, filter);
            mhandler.sendEmptyMessageDelayed(0, 1000);
        }

        mCalendar = new Time();
        onTimeChanged();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }
        float scale = Math.min(hScale, vScale);
        /**
         * 让自定义view支持wrap_content
         */
        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 让自定义view支持Padding
         */
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        //请注意，这里的availableWidth和availableHeight，
        int availableWidth = getRight() - getLeft();
        int availableHeight = getBottom() - getTop();

        //x和y就是View的中心点的坐标
        int x = availableWidth / 2;
        int y = availableHeight / 2;

        /**绘制表盘*/
        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        boolean scaled = false;
        //如果可用的宽高小于表盘图片的宽高， 就要进行缩放
        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w, (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }
        dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        dial.draw(canvas);

        /**绘制时针*/
        canvas.save();
        //以点(x,y)为中心旋转坐标系
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        w = hourHand.getIntrinsicWidth();
        h = hourHand.getIntrinsicHeight();
        hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        hourHand.draw(canvas);
        canvas.restore();

        /**绘制分针*/
        canvas.save();
        //根据分针旋转坐标系
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        final Drawable minuteHand = mMinuteHand;
        w = minuteHand.getIntrinsicWidth();
        h = minuteHand.getIntrinsicHeight();
        minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        minuteHand.draw(canvas);
        canvas.restore();

        /**绘制秒针*/
        canvas.save();
        //根据分针旋转坐标系
        canvas.rotate(mSeconds / 60.0f * 360.0f, x, y);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(x, y, x, h / 10, paint);
        canvas.restore();

        //最后，我们把缩放的坐标系复原。
        if (scaled) {
            canvas.restore();
        }
    }

    /**
     * 有线程或动画，需要及时停止
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onTimeChanged();
    }

    private void onTimeChanged() {
        mCalendar.setToNow();

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;

        //second和minute是可能超过60和24的
        mSeconds = second;
        mMinutes = minute + mSeconds / 60.0f;
        mHour = hour + mMinutes / 60.0f;

        invalidate();
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //在时区发生变化时，更新mCalendar的时区
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
            //进行时间的更新
            onTimeChanged();
        }
    };
}
