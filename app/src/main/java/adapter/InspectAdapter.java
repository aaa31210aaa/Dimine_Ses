package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.EnterpriseBean;
import utils.ListViewHolder;

public class InspectAdapter extends BaseAdapter {
    private Context context;
    private List<EnterpriseBean> mDatas;

    public InspectAdapter(Context context, List<EnterpriseBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
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
            convertView = View.inflate(context, R.layout.inspect_item,null);
        }

        EnterpriseBean entity = mDatas.get(position);
        TextView inspect_item_investigationname = ListViewHolder.get(convertView,R.id.inspect_item_investigationname);
        TextView inspect_item_investigationtype = ListViewHolder.get(convertView,R.id.inspect_item_investigationtype);
        TextView inspect_item_investigators = ListViewHolder.get(convertView,R.id.inspect_item_investigators);

        inspect_item_investigationname.setText(entity.getInvestigationName());
        inspect_item_investigationtype.setText(entity.getInvestigationType());
        inspect_item_investigators.setText(entity.getInvestigators());
        return convertView;
    }

    public void DataNotify(List<EnterpriseBean> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
}
