package com.tyf.ui.viewprinciple;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tyf.ui.R;

import java.util.Random;

public class ViewPrincipleActivity extends AppCompatActivity {
    StaggerLayout staggerLayout;

    //启动该Activity
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ViewPrincipleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_principle);

        staggerLayout = (StaggerLayout) findViewById(R.id.staggerlayout);

        String tag;

        for (int i = 0; i < 15; i++) {
            tag = "我是标签：0" + i;
            TextView textView = new TextView(this);
            int color = i % 2 == 0 ? Color.CYAN : Color.YELLOW;
            Random random = new Random();
            int fontSize = random.nextInt(30) + 10;
            textView.setTextSize(fontSize);
            textView.setBackgroundColor(color);
            textView.setText(tag);
            staggerLayout.addView(textView);
        }
    }
}
