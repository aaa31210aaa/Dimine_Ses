package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.HiddenReviewBean;
import utils.ListViewHolder;

public class ReViewAdapter extends BaseAdapter {
    private Context context;
    private List<HiddenReviewBean> mDatas;

    public ReViewAdapter(Context context, List<HiddenReviewBean> mDatas) {
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
            convertView = View.inflate(context, R.layout.review_item, null);
        }
        HiddenReviewBean bean = mDatas.get(position);
        TextView review_item_name = ListViewHolder.get(convertView, R.id.review_item_name);
        TextView review_item_num = ListViewHolder.get(convertView, R.id.review_item_num);
        TextView review_item_zgman = ListViewHolder.get(convertView, R.id.review_item_zgman);
        review_item_name.setText(bean.getHiddenName());
        review_item_num.setText(bean.getHiddenNumber());
        review_item_zgman.setText(bean.getRectificationPersonLiable());
        return convertView;
    }

    public void DataNotify(List<HiddenReviewBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }
}
