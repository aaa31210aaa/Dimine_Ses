package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.QyYhInformationBean;
import utils.ListViewHolder;

public class RiskListAdapter extends BaseAdapter {
    private Context context;
    private List<QyYhInformationBean> mlists;

    public RiskListAdapter(Context context, List<QyYhInformationBean> mlists) {
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
            convertView = View.inflate(context, R.layout.risklist_item, null);
        }
        TextView risklist_item_crname = ListViewHolder.get(convertView, R.id.risklist_item_crname);
        TextView risklist_item_crtypename = ListViewHolder.get(convertView, R.id.risklist_item_crtypename);
        TextView risklist_item_pcdate = ListViewHolder.get(convertView, R.id.risklist_item_pcdate);
        QyYhInformationBean bean = mlists.get(position);
        risklist_item_crname.setText("隐患名称：" + bean.getCrname());
        risklist_item_crtypename.setText("隐患类型：" + bean.getCrtypename());
        risklist_item_pcdate.setText("排查日期：" + bean.getPcdate());

        return convertView;
    }

    public void DataNotify(List<QyYhInformationBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
