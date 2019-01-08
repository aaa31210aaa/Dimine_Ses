package tab;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.dimine_sis.NewsDetail;
import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;

import adapter.NewsAdapter;
import bean.NewsBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    private View view;
    private ListView news_listview;
    private ArrayList<NewsBean> mDatas;


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_news, null);
            initView();
            initData();
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    private void initView() {
        news_listview = (ListView) view.findViewById(R.id.news_listview);

    }


    private void initData() {
        mDatas = new ArrayList<NewsBean>();
        for (int i = 0; i < 15; i++) {
            NewsBean newsEntity = new NewsBean();
            newsEntity.setImg(R.drawable.icon_chk);
            newsEntity.setTitle("安全检查");
            newsEntity.setDate("2016-12-1");
            newsEntity.setContent("测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" + i);
            mDatas.add(newsEntity);
        }

        NewsAdapter adapter = new NewsAdapter(getActivity(), mDatas);
        news_listview.setAdapter(adapter);

        news_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), NewsDetail.class);
                startActivity(intent);
            }
        });

    }


}
