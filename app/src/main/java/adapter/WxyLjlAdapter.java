package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.WxyLjlBean;
import utils.ListViewHolder;

/**
 * Created by Administrator on 2017-03-28.
 */
public class WxyLjlAdapter extends BaseAdapter {
    private Context context;
    private List<WxyLjlBean> mlist;

    public WxyLjlAdapter(Context context, List<WxyLjlBean> mlist) {
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
            convertView = View.inflate(context, R.layout.wxy_ljl_item, null);
        }
        TextView wxy_ljl_item_wzmc = ListViewHolder.get(convertView, R.id.wxy_ljl_item_wzmc);
        TextView wxy_ljl_item_lbcode = ListViewHolder.get(convertView, R.id.wxy_ljl_item_lbcode);
        TextView wxy_ljl_item_typename = ListViewHolder.get(convertView, R.id.wxy_ljl_item_typename);

        WxyLjlBean bean = mlist.get(position);
        wxy_ljl_item_wzmc.setText(bean.getWzmc());
        wxy_ljl_item_lbcode.setText(bean.getLbcode());
        wxy_ljl_item_typename.setText(bean.getTypename());

        return convertView;
    }

    public void DataNotify(List<WxyLjlBean> lists) {
        this.mlist = lists;
        notifyDataSetChanged();
    }
}
