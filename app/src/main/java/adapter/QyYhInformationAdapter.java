package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.QyYhInformationBean;
import utils.ListViewHolder;

public class QyYhInformationAdapter extends BaseAdapter {
    private Context context;
    private List<QyYhInformationBean> mlists;
    private String mycode;

    public QyYhInformationAdapter(Context context, List<QyYhInformationBean> mlists, String mycode) {
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
            convertView = View.inflate(context, R.layout.qyyh_information_item, null);
        }
        TextView qyyh_information_item_crname = ListViewHolder.get(convertView, R.id.qyyh_information_item_crname);
        TextView qyyh_information_item_crtypename = ListViewHolder.get(convertView, R.id.qyyh_information_item_crtypename);
        TextView qyyh_information_item_pcdate = ListViewHolder.get(convertView, R.id.qyyh_information_item_pcdate);
        TextView qyyh_information_item_qyname = ListViewHolder.get(convertView, R.id.qyyh_information_item_qyname);
        QyYhInformationBean bean = mlists.get(position);
        qyyh_information_item_crname.setText("隐患名称：" + bean.getCrname());
        qyyh_information_item_crtypename.setText("隐患类型：" + bean.getCrtypename());
        qyyh_information_item_pcdate.setText("排查日期：" + bean.getPcdate());
        qyyh_information_item_qyname.setText("企业名称："+bean.getQyname());

        if (mycode.equals("home")) {
            qyyh_information_item_qyname.setVisibility(View.VISIBLE);
        } else {
            qyyh_information_item_qyname.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void DataNotify(List<QyYhInformationBean> lists) {
        this.mlists = lists;
        notifyDataSetChanged();
    }
}
