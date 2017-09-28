package com.tyf.ui;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;

/**
 * Created by 1 on 2017/7/2 0002.
 */

public class TestAnim extends Activity {
    private void test() {
        View view = new View(this);
//---------------------------------------------------------------------
        //帧动画
        view.setBackgroundResource(R.drawable.frame_animation);
        AnimationDrawable frame_animation = (AnimationDrawable) view.getBackground();
        frame_animation.start();
//---------------------------------------------------------------------
        //view（补间）动画
        Animation tween_animation = AnimationUtils.loadAnimation(this, R.anim.tween_animation);
        view.startAnimation(tween_animation);

        //创建动画，参数表示他的子动画是否共用一个插值器
        AnimationSet animationSet = new AnimationSet(true);
        //添加动画
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
        //设置插值器
        animationSet.setInterpolator(new LinearInterpolator());
        //设置动画持续时长
        animationSet.setDuration(3000);
        //设置动画结束之后是否保持动画的目标状态
        animationSet.setFillAfter(true);
        //设置动画结束之后是否保持动画开始时的状态
        animationSet.setFillBefore(false);
        //设置重复模式
        animationSet.setRepeatMode(AnimationSet.REVERSE);
        //设置重复次数
        animationSet.setRepeatCount(AnimationSet.INFINITE);
        //设置动画延时时间
        animationSet.setStartOffset(2000);
        //取消动画
        animationSet.cancel();
        //释放资源
        animationSet.reset();
        //开始动画
        view.startAnimation(animationSet);
//---------------------------------------------------------------------
        //属性动画，分为ObjectAnimator、ValueAnimator
        ObjectAnimator tAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f, 100f);//对某个对象进行操作
        tAnimator.setInterpolator(new AccelerateDecelerateInterpolator());  //插值器，可自定义
        tAnimator.addListener(null);//监听
        tAnimator.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(tAnimator, tAnimator);//……
        animatorSet.setDuration(1000).start();

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.property_animator);
        set.setTarget(view);
        set.start();

        final ValueAnimator animator = ValueAnimator.ofInt(0, 100);//对数值变化进行操作
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                Integer value = (Integer) animation.getAnimatedValue();
                //btn_click.setText(value + "");
            }
        });
        animator.start();
//---------------------------------------------------------------------

        //Layout动画，ViewGroup中子View的出场动画
        Animation layout_animation = AnimationUtils.loadAnimation(this, R.anim.layout_animation);
        LayoutAnimationController controller = new LayoutAnimationController(layout_animation);
        controller.setDelay(0.5f);
        new ListView(this).setLayoutAnimation(controller);
//---------------------------------------------------------------------

        //activity切换效果，在startActivity()或finish()之后调用
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
    }
}
