package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.AccidentBean;
import utils.ListViewHolder;

public class AccidentPresentationAdapter extends BaseAdapter {
    private Context context;
    private List<AccidentBean> lists;

    public AccidentPresentationAdapter(Context context, List<AccidentBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.accident_presentation_item, null);
        }
        TextView inspect_item_enterprise_name = ListViewHolder.get(convertView, R.id.accident_presentation_accidentname);
        TextView accident_presentation_publisher = ListViewHolder.get(convertView, R.id.accident_presentation_publisher);
        TextView accident_presentation_releasetime = ListViewHolder.get(convertView, R.id.accident_presentation_releasetime);
        AccidentBean entity = lists.get(position);
        inspect_item_enterprise_name.setText("快报名称：" + entity.getAccidentName());
        accident_presentation_publisher.setText("发布人：" + entity.getPublisher());
        accident_presentation_releasetime.setText("发布时间：" + entity.getReleaseTime());
        return convertView;
    }

    public void DataNotify(List<AccidentBean> mDatas) {
        this.lists = mDatas;
        notifyDataSetChanged();
    }

}
