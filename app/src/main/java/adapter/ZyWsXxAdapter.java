package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.ZyWsXxBean;
import utils.ListViewHolder;

public class ZyWsXxAdapter extends BaseAdapter {
    private Context context;
    private List<ZyWsXxBean> mlists;
    private String mycode;

    public ZyWsXxAdapter(Context context, List<ZyWsXxBean> mlists, String mycode) {
        this.context = context;
        this.mlists = mlists;
        this.mycode = mycode;
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
            convertView = View.inflate(context, R.layout.zywsxx_item, null);
        }
        TextView zywsxx_item_ohaddres = ListViewHolder.get(convertView, R.id.zywsxx_item_ohaddres);
        TextView zywsxx_item_sites = ListViewHolder.get(convertView, R.id.zywsxx_item_sites);
        TextView zywsxx_item_ohname = ListViewHolder.get(convertView, R.id.zywsxx_item_ohname);
        TextView zywsxx_item_createdate = ListViewHolder.get(convertView, R.id.zywsxx_item_createdate);
        TextView zywsxx_item_qyname = ListViewHolder.get(convertView, R.id.zywsxx_item_qyname);
        TextView zywsxx_item_dqdate = ListViewHolder.get(convertView,R.id.zywsxx_item_dqdate);
        ZyWsXxBean bean = mlists.get(position);

        if (bean.getIsexpire().equals("1")){
            zywsxx_item_dqdate.setTextColor(Color.RED);
        }else{
            zywsxx_item_dqdate.setTextColor(Color.BLACK);
        }

        zywsxx_item_ohaddres.setText("作业场所：" + bean.getOhaddres());
        zywsxx_item_sites.setText("岗位：" + bean.getSites());
        zywsxx_item_ohname.setText("职业危害因素名称：" + bean.getOhname());
        zywsxx_item_createdate.setText("创建日期：" + bean.getCreatedate());
        zywsxx_item_qyname.setText("企业名称：" + bean.getQyname());
        zywsxx_item_dqdate.setText("到期日期："+bean.getDqdate());
        if (mycode.equals("home")) {
            zywsxx_item_qyname.setVisibility(View.VISIBLE);
        } else {
            zywsxx_item_qyname.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void DataNotify(List<ZyWsXxBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
