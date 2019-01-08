package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;

import bean.SafetyTrainingBean;
import utils.ListViewHolder;

public class SafetyTrainingListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SafetyTrainingBean> mDatas;

    public SafetyTrainingListViewAdapter(Context context, ArrayList<SafetyTrainingBean> mDatas) {
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
            convertView = View.inflate(context, R.layout.safety_training_listview_item, null);
        }

        SafetyTrainingBean safetyTrainingEntity = mDatas.get(position);

        ImageView safety_training_listview_item_img = ListViewHolder.get(convertView, R.id.safety_training_listview_item_img);
        TextView safety_training_listview_item_content = ListViewHolder.get(convertView, R.id.safety_training_listview_item_content);
        TextView safety_training_listview_item_date = ListViewHolder.get(convertView, R.id.safety_training_listview_item_date);
        ImageView safety_training_listview_item_state = ListViewHolder.get(convertView, R.id.safety_training_listview_item_state);

        safety_training_listview_item_img.setImageResource(R.drawable.sfcheck_reform);
        safety_training_listview_item_content.setText(safetyTrainingEntity.getTrain_content());
        safety_training_listview_item_date.setText(safetyTrainingEntity.getDate());
        safety_training_listview_item_state.setImageResource(safetyTrainingEntity.getState());
        return convertView;
    }
}
