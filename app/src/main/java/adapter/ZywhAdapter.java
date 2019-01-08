package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.ZyWhBean;
import utils.ListViewHolder;

public class ZywhAdapter extends BaseAdapter {
    private Context context;
    private List<ZyWhBean> mlist;

    public ZywhAdapter(Context context, List<ZyWhBean> mlist) {
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
            convertView = View.inflate(context, R.layout.zywh_item, null);
        }
        TextView zywh_item_dangerno = ListViewHolder.get(convertView, R.id.zywh_item_dangerno);
        TextView zywh_item_clname = ListViewHolder.get(convertView, R.id.zywh_item_clname);
        TextView zywh_item_enname = ListViewHolder.get(convertView, R.id.zywh_item_enname);
        ZyWhBean bean = mlist.get(position);
        zywh_item_dangerno.setText("危害编号：" + bean.getDangerno());
        zywh_item_clname.setText("中文名称：" + bean.getClname());
        zywh_item_enname.setText("英文名称：" + bean.getEnname());

        return convertView;
    }

    public void DataNotify(List<ZyWhBean> lists) {
        this.mlist = lists;
        notifyDataSetChanged();
    }

}
