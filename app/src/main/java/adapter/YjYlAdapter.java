package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.YjYlBean;
import utils.ListViewHolder;

public class YjYlAdapter extends BaseAdapter {
    private Context context;
    private List<YjYlBean> mlists;

    public YjYlAdapter(Context context, List<YjYlBean> mlists) {
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
            convertView = View.inflate(context, R.layout.yjyl_item, null);
        }
        TextView yjyl_item_drilldate = ListViewHolder.get(convertView, R.id.yjyl_item_drilldate);
        TextView yjyl_item_drillcontent = ListViewHolder.get(convertView, R.id.yjyl_item_drillcontent);
        TextView yjyl_item_drillman = ListViewHolder.get(convertView, R.id.yjyl_item_drillman);
        TextView yjyl_item_reservname = ListViewHolder.get(convertView, R.id.yjyl_item_reservname);
        YjYlBean bean = mlists.get(position);

        yjyl_item_drilldate.setText("演练时间：" + bean.getDrilldate());
        yjyl_item_drillcontent.setText("演练内容：" + bean.getDrillcontent());
        yjyl_item_drillman.setText("参与人员：" + bean.getDrillman());
        yjyl_item_reservname.setText("演练预案：" + bean.getReservname());
        return convertView;
    }

    public void DataNotify(List<YjYlBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
