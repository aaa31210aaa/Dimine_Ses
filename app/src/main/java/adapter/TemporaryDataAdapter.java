package adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.TemporaryBean;
import utils.ListViewHolder;


public class TemporaryDataAdapter extends BaseAdapter {
    private Context context;
    private List<TemporaryBean> mDatas;


    public TemporaryDataAdapter(Context context, List<TemporaryBean> mDatas) {
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
            convertView = View.inflate(context, R.layout.temporarydata_item, null);
        }
        TextView temporary_name = ListViewHolder.get(convertView, R.id.temporary_name);
        TextView temporary_date = ListViewHolder.get(convertView, R.id.temporary_date);
        CheckBox temporarydata_item_choose = ListViewHolder.get(convertView, R.id.temporarydata_item_choose);


        TemporaryBean bean = mDatas.get(position);
        temporary_name.setText(bean.getCommonlyName());
        temporary_date.setText(bean.getSaveTime());


        if (bean.isShow()) {
            temporarydata_item_choose.setVisibility(View.VISIBLE);
        } else {
            temporarydata_item_choose.setVisibility(View.GONE);
        }

        if (bean.isCheck()){
            temporarydata_item_choose.setChecked(true);
        }else {
            temporarydata_item_choose.setChecked(false);
        }


        return convertView;
    }

    public void DataNotify(List<TemporaryBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }

}
