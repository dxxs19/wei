package com.wei.applications.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wei.applications.R;

public class SecondActivity extends BaseActivity
{
    TextView showTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        RelativeLayout rlLayout = (RelativeLayout) findViewById(R.id.rl_global);
        // 设置过渡动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
        scaleAnimation.setDuration(2000);
        // 设置布局动画的显示属性
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(scaleAnimation, 0.5f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        // 为ViewGroup设置布局动画
        rlLayout.setLayoutAnimation(layoutAnimationController);
    }

    private void initView() {
        setContentView(R.layout.activity_second);
        showTxt = (TextView) findViewById(R.id.txt_show);
        Intent intent = getIntent();
        if (null != intent)
        {
            String txt = intent.getStringExtra("content");
            if (null != txt && !"".equals(txt))
            {
                showTxt.setText(txt);
            }
        }
    }

    public static void actionStart(Context context, String data)
    {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra("content", data);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.jump:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.addCategory("android.intent.category.MY_DEFAULT");
//                intent.setData(Uri.parse("tel:15992682875"));
                intent.setDataAndType(Uri.parse("tel:15992682875"), "image/png");
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        Log.e(getClass().getSimpleName(), "按下返回键");
        super.onBackPressed();
    }
}
