package com.wei.ipc.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.ipc.R;

/**
 * Created by WEI on 2016/7/25.
 */
public class BaseActivity extends FragmentActivity {
    private LinearLayout viewGroup;
    protected LinearLayout titleCustom;
    protected LinearLayout container;
    protected RelativeLayout titleContainer;
    protected LinearLayout headMid;
    protected View back;
    protected TextView title;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseView();
        mContext = this;
    }

    private void initBaseView() {
        viewGroup = (LinearLayout) View.inflate(this, R.layout.title_bar, null);
        titleCustom = (LinearLayout) viewGroup.findViewById(R.id.title_custom);
        titleContainer = (RelativeLayout) viewGroup.findViewById(R.id.title_container);
        container = (LinearLayout) viewGroup.findViewById(R.id.container);
        headMid = (LinearLayout) viewGroup.findViewById(R.id.ll_head);
        back = viewGroup.findViewById(R.id.title_back);
        back.setVisibility(View.VISIBLE);
        title = (TextView) viewGroup.findViewById(R.id.title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        View contentView = View.inflate(this, layoutResID, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1);
        contentView.setLayoutParams(layoutParams);
        container.addView(contentView);
        super.setContentView(viewGroup);
    }

    public void setCustomTitle(String text) {
        title.setText(text);
        title.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    protected void showMsg(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
