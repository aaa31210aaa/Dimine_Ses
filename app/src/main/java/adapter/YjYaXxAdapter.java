package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.YjYaXxBean;
import utils.ListViewHolder;

public class YjYaXxAdapter extends BaseAdapter {
    private Context context;
    private List<YjYaXxBean> mlists;
    private String mycode;

    public YjYaXxAdapter(Context context, List<YjYaXxBean> mlists, String mycode) {
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
            convertView = View.inflate(context, R.layout.yjyaxx_item, null);
        }

        TextView yjyaxx_item_ohaddres = ListViewHolder.get(convertView, R.id.yjyaxx_item_ohaddres);
        TextView yjyaxx_item_reservname = ListViewHolder.get(convertView, R.id.yjyaxx_item_reservname);
        TextView yjyaxx_item_reloaddate = ListViewHolder.get(convertView, R.id.yjyaxx_item_reloaddate);
        TextView zywsxx_item_qyname = ListViewHolder.get(convertView, R.id.yjyaxx_item_qyname);
        YjYaXxBean bean = mlists.get(position);

        if (bean.getIsexpire().equals("1")){
            yjyaxx_item_reloaddate.setTextColor(Color.RED);
        }else{
            yjyaxx_item_reloaddate.setTextColor(Color.BLACK);
        }

        yjyaxx_item_ohaddres.setText("预案类型：" + bean.getOhaddres());
        yjyaxx_item_reservname.setText("应急预案名称：" + bean.getReservname());
        yjyaxx_item_reloaddate.setText("修编时间：" + bean.getReloaddate());
        zywsxx_item_qyname.setText("企业名称：" + bean.getQyname());
        
        if (mycode.equals("home")) {
            zywsxx_item_qyname.setVisibility(View.VISIBLE);
        } else {
            zywsxx_item_qyname.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void DataNotify(List<YjYaXxBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
