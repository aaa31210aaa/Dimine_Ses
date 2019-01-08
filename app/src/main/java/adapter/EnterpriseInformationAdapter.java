package adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.EnterpriseInformationBean;
import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import utils.ListViewHolder;

public class EnterpriseInformationAdapter extends BaseAdapter {
    private Context context;
    private List<EnterpriseInformationBean> mDatas;

    public EnterpriseInformationAdapter(Context context, List<EnterpriseInformationBean> mDatas) {
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
            convertView = View.inflate(context, R.layout.enterprise_information_item, null);
        }
        EnterpriseInformationBean bean = mDatas.get(position);
        TextView companyname = ListViewHolder.get(convertView, R.id.enterprise_information_item_companyname);
        TextView industry = ListViewHolder.get(convertView, R.id.enterprise_information_item_industry);
        TextView enterpriseaddress = ListViewHolder.get(convertView, R.id.enterprise_information_item_enterpriseaddress);
        TextView tablename = ListViewHolder.get(convertView, R.id.enterprise_information_item_tablename);
        //设置提示
        BGABadgeLinearLayout information_item_badge = ListViewHolder.get(convertView, R.id.information_item_badge);

        companyname.setText("企业名称：" + bean.getCompanyName());
        industry.setText("行业：" + bean.getIndustry());
        enterpriseaddress.setText("企业地址：" + bean.getEnterpriseAddress());

        String a = bean.getAddStatus();
        Log.e("ces", a);

        if (bean.getAddStatus().equals("1")) {
            tablename.setVisibility(View.VISIBLE);
            tablename.setText("更新时间：" + bean.getTablename());
            information_item_badge.showCirclePointBadge();
        }else{
            tablename.setVisibility(View.GONE);
            information_item_badge.hiddenBadge();
        }


        //监听拖拽消失后的操作
//        information_item_badge.setDragDismissDelegage(new BGADragDismissDelegate() {
//            @Override
//            public void onDismiss(BGABadgeable badgeable) {
//                //处理拖拽消失后的操作
//            }
//        });

        return convertView;
    }


    public void DataNotify(List<EnterpriseInformationBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }


}
