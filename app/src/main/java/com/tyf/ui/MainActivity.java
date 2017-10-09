package com.tyf.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tyf.ui.viewprinciple.AnalogClock;
import com.tyf.ui.viewprinciple.StaggerLayout;
import com.tyf.ui.viewprinciple.ViewPrincipleActivity;
import com.tyf.ui.viewwidget.ListViewFragment;
import com.tyf.ui.viewwidget.RecyclerFragment;
import com.tyf.ui.viewwidget.ViewWidgetActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Class[] view = {
                TestView.class,         //View的基本参数、初始化、绘制流程、事件体系
                AnalogClock.class,      //完全自定义View(重写onDraw)
                StaggerLayout.class,    //完全自定义ViewGroup(重写)
                //扩展已有View(如密码输入EditText)
                //扩展已有ViewGroup(inflate)

                TestAnim.class,          //动画

                ListViewFragment.class,
                RecyclerFragment.class,
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_view_widget:
                ViewWidgetActivity.actionStart(MainActivity.this);
                break;
            case R.id.btn_view_principle:
                ViewPrincipleActivity.actionStart(MainActivity.this);
                break;
        }
    }
}
