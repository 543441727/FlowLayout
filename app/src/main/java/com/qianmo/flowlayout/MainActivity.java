package com.qianmo.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        List<String> lable = new ArrayList<>();
        lable.add("经济");
        lable.add("娱乐");
        lable.add("八卦");
        lable.add("小道消息");
        lable.add("政治中心");
        lable.add("彩票");
        lable.add("情感");
        //设置标签
        flowLayout.setLables(lable, true);
    }
}
