package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import utils.ListViewHolder;

public class CommonlyRiskListAdapter extends BaseAdapter {
    private Context context;
    private List<String> mDatas;

    public CommonlyRiskListAdapter(Context context, List<String> mDatas) {
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
            convertView = View.inflate(context, R.layout.risk_list_item, null);
        }
        TextView risk_list_item_tv = ListViewHolder.get(convertView, R.id.risk_list_item_tv);
        risk_list_item_tv.setText(mDatas.get(position));
        return convertView;
    }
}
