package com.qianmo.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjitao on 2017/3/27 0027.
 * E-Mail：543441727@qq.com
 * 打造实现流式标签
 */

public class FlowLayout extends ViewGroup {
    private static String TAG = "FlowLayout";

    //自定义属性
    private int LINE_SPACE;
    private int ROW_SPACE;

    //放置标签的集合
    private List<String> lables;
    private List<String> lableSelects;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        LINE_SPACE = a.getDimensionPixelSize(R.styleable.FlowLayout_lineSpace, 10);
        ROW_SPACE = a.getDimensionPixelSize(R.styleable.FlowLayout_rowSpace, 10);
        a.recycle();

    }

    /**
     * 添加margin 属性
     * @param attrs
     * @return
     */
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 添加标签
     *
     * @param lables 标签集合
     * @param isAdd  是否添加
     */
    public void setLables(List<String> lables, boolean isAdd) {
        if (this.lables == null) {
            this.lables = new ArrayList<>();
        }
        if (this.lableSelects == null) {
            this.lableSelects = new ArrayList<>();
        }
        if (isAdd) {
            this.lables.addAll(lables);
        } else {
            this.lables.clear();
            this.lables = lables;
        }
        if (lables != null && lables.size() > 0) {
            for (final String lable : lables) {
                final TextView tv = new TextView(getContext());
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
                tv.setText(lable);
                tv.setTextSize(20);
                tv.setBackgroundResource(R.drawable.shape_item_lable_bg);
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(12, 5, 12, 5);

                //判断是否选中
                if (lableSelects.contains(lable)) {
                    tv.setSelected(true);
                    tv.setTextColor(getResources().getColor(R.color.tv_blue));
                } else {
                    tv.setSelected(false);
                    tv.setTextColor(getResources().getColor(R.color.tv_gray));
                }

                //点击之后选中标签
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv.setSelected(tv.isSelected() ? false : true);
                        if (tv.isSelected()) {
                            tv.setTextColor(getResources().getColor(R.color.tv_blue));
                            lableSelects.add(lable);
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.tv_gray));
                            lableSelects.remove(lable);
                        }
                    }
                });

                //添加到容器中
                addView(tv);
            }
        }
    }

    /**
     * 通过测量子控件高度，来设置自身控件的高度
     * 主要是计算
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量所有子view的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //获取view的宽高测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //这里的宽度建议使用match_parent或者具体值，当然当使用wrap_content的时候没有重写的话也是match_parent所以这里的宽度就直接使用测量的宽度
        int width = widthSize;

        int height;
        //判断宽度
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int row = 1;
            int widthSpace = width; //宽度剩余空间
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                //获取标签宽度
                int childW = view.getMeasuredWidth();
                //判断剩余宽度是否大于此标签宽度
                if (widthSpace >= childW) {
                    widthSpace -= childW;
                } else {
                    row++;
                    widthSpace = width - childW;
                }
                //减去两边间距
                widthSpace -= LINE_SPACE;
            }
            //获取子控件的高度
            int childH = getChildAt(0).getMeasuredHeight();
            //测算最终所需要的高度
            height = (childH * row) + (row - 1) * ROW_SPACE;
        }

        //保存测量高度
        setMeasuredDimension(width, height);
    }

    /**
     * 摆放子view
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View chileView = getChildAt(i);
            int childW = chileView.getMeasuredWidth();
            int childH = chileView.getMeasuredHeight();
            right += childW;
            bottom = (childH + ROW_SPACE) * row + childH;
            if (right > (r - LINE_SPACE)) {
                row++;
                right = childW;
                bottom = (childH + ROW_SPACE) * row + childH;
            }
            chileView.layout(right - childW, bottom - childH, right, bottom);
            right += LINE_SPACE;
        }
    }
}
