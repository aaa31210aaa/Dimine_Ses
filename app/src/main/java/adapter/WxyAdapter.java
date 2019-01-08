package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.WxyBean;
import utils.ListViewHolder;

public class WxyAdapter extends BaseAdapter {
    private Context context;
    private List<WxyBean> mlists;
    private String mycode;


    public WxyAdapter(Context context, List<WxyBean> mlists, String mycode) {
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
            convertView = View.inflate(context, R.layout.wxy_item, null);
        }
        TextView wxy_mainrisk = ListViewHolder.get(convertView, R.id.wxy_mainrisk);
        TextView wxy_maindanger = ListViewHolder.get(convertView, R.id.wxy_maindanger);
        TextView wxy_risktypename = ListViewHolder.get(convertView, R.id.wxy_risktypename);
        TextView wxy_qyname = ListViewHolder.get(convertView, R.id.wxy_qyname);
        WxyBean bean = mlists.get(position);

        wxy_mainrisk.setText("主要风险点：" + bean.getMainrisk());
        wxy_maindanger.setText("主要危险：" + bean.getMaindanger());
        wxy_risktypename.setText("风险类型：" + bean.getRisktypename());
        wxy_qyname.setText("企业名称："+bean.getQyname());

        if (mycode.equals("home")) {
            wxy_qyname.setVisibility(View.VISIBLE);
        } else {
            wxy_qyname.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void DataNotify(List<WxyBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }

}
