package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.QyZzBean;
import utils.ListViewHolder;

public class QyZzAdapter extends BaseAdapter {
    private Context context;
    private List<QyZzBean> mlists;

    public QyZzAdapter(Context context, List<QyZzBean> mlists) {
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
            convertView = View.inflate(context, R.layout.qyzz_item, null);
        }
        TextView qyzz_item_cftypename = ListViewHolder.get(convertView,R.id.qyzz_item_cftypename);
        TextView qyzz_item_certificate = ListViewHolder.get(convertView,R.id.qyzz_item_certificate);
        TextView qyzz_item_cardname = ListViewHolder.get(convertView,R.id.qyzz_item_cardname);
        TextView qyzz_item_administrator = ListViewHolder.get(convertView,R.id.qyzz_item_administrator);
        TextView qyzz_item_carddate = ListViewHolder.get(convertView,R.id.qyzz_item_carddate);

        QyZzBean bean = mlists.get(position);
        if (bean.getIsexpire().equals("1")){
            qyzz_item_carddate.setTextColor(Color.RED);
        }else{
            qyzz_item_carddate.setTextColor(Color.BLACK);
        }

        qyzz_item_cftypename.setText("证照类型："+bean.getCftypename());
        qyzz_item_certificate.setText("证件号："+bean.getCertificate());
        qyzz_item_cardname.setText("证件名称："+bean.getCardname());
        qyzz_item_administrator.setText("负责人："+bean.getAdministrator());
        qyzz_item_carddate.setText("颁布时间："+bean.getCarddate());

        return convertView;
    }

    public void DataNotify(List<QyZzBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
