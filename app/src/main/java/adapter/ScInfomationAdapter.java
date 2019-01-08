package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.ScInfomationBean;
import utils.ListViewHolder;

public class ScInfomationAdapter extends BaseAdapter{
    private Context context;
    private List<ScInfomationBean> mlist;
    private String mycode;


    public ScInfomationAdapter(Context context, List<ScInfomationBean> mlist,String mycode) {
        this.context = context;
        this.mlist = mlist;
        this.mycode = mycode;
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
        if (convertView == null){
            convertView = View.inflate(context, R.layout.scinfomation_item,null);
        }
        TextView sc_information_item_checkname = ListViewHolder.get(convertView,R.id.sc_information_item_checkname);
        TextView sc_information_item_problems = ListViewHolder.get(convertView,R.id.sc_information_item_problems);
        TextView sc_information_item_iscommit = ListViewHolder.get(convertView,R.id.sc_information_item_iscommit);
        TextView sc_information_item_qyname = ListViewHolder.get(convertView,R.id.sc_information_item_qyname);
        TextView sc_infomation_date = ListViewHolder.get(convertView,R.id.sc_infomation_date);
        ScInfomationBean bean = mlist.get(position);

        sc_information_item_checkname.setText("安全检查项目："+bean.getCheckname());
        sc_information_item_problems.setText("查处问题："+bean.getProblems());
        sc_information_item_iscommit.setText("是否提交："+bean.getIscommit());
        sc_infomation_date.setText("检查时间："+bean.getScdate());
        sc_information_item_qyname.setText("企业名称："+bean.getQyname());


        if (mycode.equals("home")) {
            sc_information_item_qyname.setVisibility(View.VISIBLE);
        } else {
            sc_information_item_qyname.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void DataNotify(List<ScInfomationBean> lists) {
        this.mlist = lists;
        notifyDataSetChanged();
    }


}
