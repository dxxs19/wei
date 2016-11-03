package com.wei.applications.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.wei.applications.R;
import com.wei.applications.ui.fragment.NewsContentFragment;

public class NewsContentActivity extends BaseActivity {
    public static void actionStart(Context context, String newsTitle, String newsContent) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("news_title", newsTitle);
        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_content);
        // 获取传入的新闻标题
        String newsTitle = getIntent().getStringExtra("news_title");
        // 获取传入的新闻内容
        String newsContent = getIntent().getStringExtra("news_content");
        NewsContentFragment newsContentFragment = (NewsContentFragment)getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);
        // 刷新NewsContentFragment界面
        newsContentFragment.refresh(newsTitle, newsContent);
    }
}
