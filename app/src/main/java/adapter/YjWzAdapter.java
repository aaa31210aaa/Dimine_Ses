package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.YjWzBean;
import utils.ListViewHolder;

public class YjWzAdapter extends BaseAdapter {
    private Context context;
    private List<YjWzBean> mlists;

    public YjWzAdapter(Context context, List<YjWzBean> mlists) {
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
            convertView = View.inflate(context, R.layout.yjwz_item, null);
        }
        TextView yjwz_item_materialsname = ListViewHolder.get(convertView, R.id.yjwz_item_materialsname);
        TextView yjwz_item_materialssum = ListViewHolder.get(convertView, R.id.yjwz_item_materialssum);
        TextView yjwz_item_ohaddres = ListViewHolder.get(convertView, R.id.yjwz_item_ohaddres);
        TextView yjwz_item_modifydate = ListViewHolder.get(convertView, R.id.yjwz_item_modifydate);
        YjWzBean bean = mlists.get(position);

        yjwz_item_materialsname.setText("应急物资名称：" + bean.getMaterialsname());
        yjwz_item_materialssum.setText("应急物资数量：" + bean.getMaterialssum());
        yjwz_item_ohaddres.setText("物资存放位置：" + bean.getOhaddres());
        yjwz_item_modifydate.setText("更新时间：" + bean.getModifydate());
        return convertView;
    }

    public void DataNotify(List<YjWzBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
