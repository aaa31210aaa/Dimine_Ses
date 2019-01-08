package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.RyGlBean;
import utils.ListViewHolder;

public class RyGlAdapter extends BaseAdapter {
    private Context context;
    private List<RyGlBean> mlists;

    public RyGlAdapter(Context context, List<RyGlBean> mlists) {
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
            convertView = View.inflate(context, R.layout.rygl_item, null);
        }
        TextView rygl_item_sptypename = ListViewHolder.get(convertView, R.id.rygl_item_sptypename);
        TextView rygl_item_mainhead = ListViewHolder.get(convertView, R.id.rygl_item_mainhead);
        TextView rygl_item_deptname = ListViewHolder.get(convertView, R.id.rygl_item_deptname);
        TextView rygl_item_yxstardate = ListViewHolder.get(convertView, R.id.rygl_item_yxstardate);
        RyGlBean bean = mlists.get(position);

        if (bean.getIsexpire().equals("1")) {
            rygl_item_yxstardate.setTextColor(Color.RED);
        } else {
            rygl_item_yxstardate.setTextColor(Color.BLACK);
        }

        rygl_item_sptypename.setText("人员类型：" + bean.getSptypename());
        rygl_item_mainhead.setText("姓名：" + bean.getMainhead());
        rygl_item_deptname.setText("所在部门：" + bean.getDeptname());
        rygl_item_yxstardate.setText("有效时间：" + bean.getYxstardate());

        return convertView;
    }

    public void DataNotify(List<RyGlBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
