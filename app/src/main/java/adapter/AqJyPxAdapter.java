package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.AqJyPxBean;
import utils.ListViewHolder;

public class AqJyPxAdapter extends BaseAdapter {
    private Context context;
    private List<AqJyPxBean> mlists;
    private String mycode;

    public AqJyPxAdapter(Context context, List<AqJyPxBean> mlists, String mycode) {
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
            convertView = View.inflate(context, R.layout.aqjypx_item, null);
        }
        TextView aqjypx_item_trainingname = ListViewHolder.get(convertView, R.id.aqjypx_item_trainingname);
        TextView aqjypx_item_trainingdate = ListViewHolder.get(convertView, R.id.aqjypx_item_trainingdate);
        TextView aqjypx_item_trainingadd = ListViewHolder.get(convertView, R.id.aqjypx_item_trainingadd);
        TextView aqjypx_item_qyname = ListViewHolder.get(convertView, R.id.aqjypx_item_qyname);
        AqJyPxBean bean = mlists.get(position);

        if (bean.getIsexpire().equals("1")){
            aqjypx_item_trainingdate.setTextColor(Color.RED);
        }else{
            aqjypx_item_trainingdate.setTextColor(Color.BLACK);
        }

        aqjypx_item_trainingname.setText("培训名称：" + bean.getTrainingname());
        aqjypx_item_trainingdate.setText("培训时间：" + bean.getTrainingdate());
        aqjypx_item_trainingadd.setText("培训地点：" + bean.getTrainingadd());
        aqjypx_item_qyname.setText("企业名称：" + bean.getQyname());
        if (mycode.equals("home")) {
            aqjypx_item_qyname.setVisibility(View.VISIBLE);
        } else {
            aqjypx_item_qyname.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void DataNotify(List<AqJyPxBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
