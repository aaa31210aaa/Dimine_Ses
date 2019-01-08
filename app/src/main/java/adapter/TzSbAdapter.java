package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.TzSbBean;
import utils.ListViewHolder;

public class TzSbAdapter extends BaseAdapter{
    private Context context;
    private List<TzSbBean> mlists;

    public TzSbAdapter(Context context, List<TzSbBean> mlists) {
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
            convertView = View.inflate(context, R.layout.tzsb_item, null);
        }
        TextView tzsb_item_facilitiesname = ListViewHolder.get(convertView, R.id.tzsb_item_facilitiesname);
        TextView tzsb_item_facilitiesno = ListViewHolder.get(convertView, R.id.tzsb_item_facilitiesno);
        TextView tzsb_item_manufacturer = ListViewHolder.get(convertView, R.id.tzsb_item_manufacturer);
        TextView tzsb_item_installsite = ListViewHolder.get(convertView, R.id.tzsb_item_installsite);
        TextView tzsb_item_checkdate = ListViewHolder.get(convertView,R.id.tzsb_item_checkdate);
        TzSbBean bean = mlists.get(position);

        if (bean.getIsexpire().equals("1")){
            tzsb_item_checkdate.setTextColor(Color.RED);
        }else{
            tzsb_item_checkdate.setTextColor(Color.BLACK);
        }

        tzsb_item_facilitiesname.setText("设备名称：" + bean.getFacilitiesname());
        tzsb_item_facilitiesno.setText("规格型号：" + bean.getFacilitiesno());
        tzsb_item_manufacturer.setText("制造厂商：" + bean.getManufacturer());
        tzsb_item_installsite.setText("安装位置：" + bean.getInstallsite());
        tzsb_item_checkdate.setText("检查时间："+bean.getCheckdate());
        return convertView;
    }

    public void DataNotify(List<TzSbBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
