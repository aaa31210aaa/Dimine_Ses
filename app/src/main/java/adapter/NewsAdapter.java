package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;

import bean.NewsBean;
import utils.ListViewHolder;

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsBean> mDatas;

    public NewsAdapter(Context context, ArrayList<NewsBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.news_recyclerview_item, null);
        }

        NewsBean newsEntity = mDatas.get(position);
        ImageView news_item_img = ListViewHolder.get(convertView, R.id.news_item_img);
        TextView news_item_title = ListViewHolder.get(convertView, R.id.news_item_title);
        TextView news_item_date = ListViewHolder.get(convertView, R.id.news_item_date);
        TextView news_item_content = ListViewHolder.get(convertView, R.id.news_item_content);

        news_item_img.setImageResource(newsEntity.getImg());
        news_item_title.setText(newsEntity.getTitle());
        news_item_date.setText(newsEntity.getDate());
        news_item_content.setText(newsEntity.getContent());

        return convertView;
    }
}
