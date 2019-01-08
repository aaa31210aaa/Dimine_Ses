package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.RulesBean;
import utils.ListViewHolder;

public class RulesAdapter extends BaseAdapter {
    private Context context;
    private List<RulesBean> mDatas;
    private String mycode;


    public RulesAdapter(Context context, List<RulesBean> mDatas, String mycode) {
        this.context = context;
        this.mDatas = mDatas;
        this.mycode = mycode;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.rules_item, null);
        }
        TextView rules_detail_gzzdname = ListViewHolder.get(convertView, R.id.rules_detail_gzzdname);
        TextView rules_detail_efftime = ListViewHolder.get(convertView, R.id.rules_detail_efftime);
        TextView rules_detail_cbtypename = ListViewHolder.get(convertView, R.id.rules_detail_cbtypename);
        TextView rules_detail_qyname = ListViewHolder.get(convertView, R.id.rules_detail_qyname);

        RulesBean bean = mDatas.get(position);

        rules_detail_gzzdname.setText("规章制度名称：" + bean.getRulesName());
        rules_detail_efftime.setText("生效时间：" + bean.getRulesEffTime());
        rules_detail_cbtypename.setText("规章制度类型：" + bean.getRulesType());
        rules_detail_qyname.setText("企业名称："+bean.getQyname());

        if (mycode.equals("home")) {
            rules_detail_qyname.setVisibility(View.VISIBLE);
        } else {
            rules_detail_qyname.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void DataNotify(List<RulesBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }
}
