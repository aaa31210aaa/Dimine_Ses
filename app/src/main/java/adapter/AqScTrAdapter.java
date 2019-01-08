package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.AqScTrBean;
import utils.ListViewHolder;

public class AqScTrAdapter extends BaseAdapter {
    private Context context;
    private List<AqScTrBean> mlists;
    private String mycode;

    public AqScTrAdapter(Context context, List<AqScTrBean> mlists, String mycode) {
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
            convertView = View.inflate(context, R.layout.aqsctr_item, null);
        }
        TextView aqsctr_item_monthvalue = ListViewHolder.get(convertView, R.id.aqsctr_item_monthvalue);
        TextView aqsctr_item_investypename = ListViewHolder.get(convertView, R.id.aqsctr_item_investypename);
        TextView aqsctr_item_investname = ListViewHolder.get(convertView, R.id.aqsctr_item_investname);
        TextView aqsctr_item_usemoney = ListViewHolder.get(convertView, R.id.aqsctr_item_usemoney);
        TextView aqsctr_item_qyname = ListViewHolder.get(convertView, R.id.aqsctr_item_qyname);
        AqScTrBean bean = mlists.get(position);

        aqsctr_item_monthvalue.setText("生产月份：" + bean.getMonthvalue());
        aqsctr_item_investypename.setText("费用类型：" + bean.getInvestypename());
        aqsctr_item_investname.setText("项目名称：" + bean.getInvestname());
        aqsctr_item_usemoney.setText("费用金额：" + bean.getUsemoney());
        if (mycode.equals("home")) {
            aqsctr_item_qyname.setText("企业名称：" + bean.getQyname());
            aqsctr_item_qyname.setVisibility(View.VISIBLE);
        } else {
            aqsctr_item_qyname.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void DataNotify(List<AqScTrBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
