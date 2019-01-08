package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.BmGlBean;
import utils.ListViewHolder;

public class BmGlAdapter extends BaseAdapter {
    private Context context;
    private List<BmGlBean> mlists;

    public BmGlAdapter(Context context, List<BmGlBean> mlists) {
        this.context = context;
        this.mlists = mlists;
    }

    @Override
    public int getCount() {
        return mlists.size();
    }

    @Override
    public Object getItem(int position) {
        return mlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.bmgl_item, null);
        }
        TextView bmgl_item_parentdeptname = ListViewHolder.get(convertView, R.id.bmgl_item_parentdeptname);
        TextView bmgl_item_deptname = ListViewHolder.get(convertView, R.id.bmgl_item_deptname);
        TextView bmgl_item_deptcode = ListViewHolder.get(convertView, R.id.bmgl_item_deptcode);
        BmGlBean bean = mlists.get(position);

        bmgl_item_parentdeptname.setText("单位名称：" + bean.getParentdeptname());
        bmgl_item_deptname.setText("部门名称：" + bean.getDeptname());
        bmgl_item_deptcode.setText("部门编号：" + bean.getDeptcode());
        return convertView;
    }

    public void DataNotify(List<BmGlBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }

}
