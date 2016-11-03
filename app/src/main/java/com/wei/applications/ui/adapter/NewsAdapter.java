package com.wei.applications.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wei.applications.R;
import com.wei.applications.model.News;

import java.util.List;

/**
 * Created by WEI on 2016/5/24.
 */
public class NewsAdapter extends ArrayAdapter<News>
{
    private int resourceId;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        News news = getItem(position);
        View view;
        if (convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }
        else
        {
            view = convertView;
        }
        TextView titleTxt = (TextView) view.findViewById(R.id.news_title);
        titleTxt.setText(news.getTitle());
        return view;
    }
}
