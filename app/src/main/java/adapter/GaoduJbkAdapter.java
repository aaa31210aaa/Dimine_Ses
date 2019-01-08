package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.GaoduBean;
import utils.ListViewHolder;

/**
 * Created by Administrator on 2017-03-28.
 */
public class GaoduJbkAdapter extends BaseAdapter {
    private Context context;
    private List<GaoduBean> mlist;

    public GaoduJbkAdapter(Context context, List<GaoduBean> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gaodu_jbk_item, null);
        }
        GaoduBean bean = mlist.get(position);
        TextView gaodu_jbk_item_tv = ListViewHolder.get(convertView, R.id.gaodu_jbk_item_tv);
        TextView gaodu_jbk_item_bt = ListViewHolder.get(convertView,R.id.gaodu_jbk_item_bt);
        gaodu_jbk_item_tv.setText(bean.getZwmz());
        gaodu_jbk_item_bt.setText(bean.getYwmz());

        return convertView;
    }
}
