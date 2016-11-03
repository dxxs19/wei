package com.wei.applications.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wei.applications.R;
import com.wei.applications.model.News;
import com.wei.applications.ui.activity.NewsContentActivity;
import com.wei.applications.ui.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsTitleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsTitleFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView newsTitleListView;
    private List<News> newsList;
    private NewsAdapter adapter;
    private boolean isTwoPane;


    public NewsTitleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsTitleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsTitleFragment newInstance(String param1, String param2) {
        NewsTitleFragment fragment = new NewsTitleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newsList = getNews();
        adapter = new NewsAdapter(context, R.layout.news_item, newsList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_title, container, false);
        newsTitleListView = (ListView) view.findViewById(R.id.news_title_list_view);
        newsTitleListView.setAdapter(adapter);
        newsTitleListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content_fragment) != null)
        {
            isTwoPane = true;
        }
        else
        {
            isTwoPane = false;
        }
    }

    private List<News> getNews() {
        List<News> newsList = new ArrayList<News>();
        News news1, news2;
        for(int i = 0; i < 20; i ++)
        {
            if (i % 2 == 0)
            {
                news1 = new News();
                news1.setTitle("Succeed in College as a Learning Disabled Student");
                news1.setContent("College freshmen will soon learn to live with a roommate, adjust to a new social scene and survive less-than-stellar " +
                        "dining hall food. Students with learning disabilities will face these transitions while also grappling with a few more hurdles.");
                newsList.add(news1);
            }
            else if (i % 2 == 1)
            {
                news2 = new News();
                news2.setTitle("Google Android exec poached by China's Xiaomi");
                news2.setContent("China's Xiaomi has poached a key Google executive involved in the tech giant's Android phones, in a move seen as a coup" +
                        " for the rapidly growing Chinese smartphone maker.");
                newsList.add(news2);
            }
        }
        return newsList;
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        News news = newsList.get(position);
        if (isTwoPane)
        {
            NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
            newsContentFragment.refresh(news.getTitle(), news.getContent());
        }
        else
        {
            NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
        }
    }
}
