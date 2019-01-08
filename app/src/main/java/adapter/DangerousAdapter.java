package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.DangerousBean;
import utils.ListViewHolder;

/**
 * Created by Administrator on 2017-03-28.
 */
public class DangerousAdapter extends BaseAdapter {
    private Context context;
    private List<DangerousBean> mlist;

    public DangerousAdapter(Context context, List<DangerousBean> mlist) {
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
            convertView = View.inflate(context, R.layout.dangerous_item, null);
        }
        TextView dangerous_item_chname = ListViewHolder.get(convertView, R.id.dangerous_item_chname);
        TextView dangerous_item_ccode = ListViewHolder.get(convertView, R.id.dangerous_item_ccode);
        TextView dangerous_item_enname = ListViewHolder.get(convertView, R.id.dangerous_item_enname);

        DangerousBean bean = mlist.get(position);
        dangerous_item_chname.setText(bean.getChname());
        dangerous_item_ccode.setText(bean.getCcode());
        dangerous_item_enname.setText(bean.getEnname());

        return convertView;
    }

    public void DataNotify(List<DangerousBean> lists) {
        this.mlist = lists;
        notifyDataSetChanged();
    }
}
