package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.GdXqBean;
import utils.ListViewHolder;

/**
 * Created by Administrator on 2017-03-25.
 */
public class GdJbkAdapter extends BaseAdapter {
    private Context context;
    private List<GdXqBean> mlist;

    public GdJbkAdapter(Context context, List<GdXqBean> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gdjbk_item, null);
        }

        GdXqBean bean = mlist.get(position);
        TextView gdjbk_title = ListViewHolder.get(convertView, R.id.gdjbk_title);
        TextView gdjbk_content = ListViewHolder.get(convertView, R.id.gdjbk_content);


        return convertView;
    }
}
