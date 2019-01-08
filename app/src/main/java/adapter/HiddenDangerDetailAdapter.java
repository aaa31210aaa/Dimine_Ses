package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.HiddenDangerDetailBean;
import risk_management.AddCommonly;
import risk_management.AddHiddenDangerRegistration;

public class HiddenDangerDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HiddenDangerDetailBean> mDatas;
    private boolean[] flag;
    private String qyid;
    private String crid;

    public HiddenDangerDetailAdapter(Context context, List<HiddenDangerDetailBean> mDatas, boolean[] flag) {
        this.context = context;
        this.mDatas = mDatas;
        this.flag = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hidden_danger_detail_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final HiddenDangerDetailBean bean = mDatas.get(position);

        myViewHolder.hidden_danger_detail_item_index.setText(bean.getIndex());
        myViewHolder.hidden_danger_detail_item_pczq.setText("排查周期："+bean.getPczq()+"天");
        myViewHolder.hidden_danger_detail_item_pcdeptname.setText("排查部门："+bean.getPcdeptname());
        myViewHolder.hidden_danger_detail_item_namestr.setText("隐患数量："+bean.getAmount());
        myViewHolder.hidden_danger_detail_item_rscdesc.setText("隐患标准类别："+bean.getNamestr());
        myViewHolder.hidden_danger_detail_item_crid.setText("标准描述："+bean.getRscdesc());
        qyid = bean.getQyid();

        myViewHolder.checkBox.setOnCheckedChangeListener(null);//先设置一次CheckBox的选中监听器，传入参数null
        myViewHolder.checkBox.setChecked(flag[position]);//用数组中的值设置CheckBox的选中状态

        myViewHolder.hidden_danger_detail_item_genera_btn.setVisibility(bean.ismVisible() ? View.VISIBLE : View.GONE);
        //监听一般按钮
        myViewHolder.hidden_danger_detail_item_genera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddCommonly.class);
                intent.putExtra("clickId", qyid);
                intent.putExtra("crid", myViewHolder.hidden_danger_detail_item_crid.getText().toString().trim());
                Log.e("kkk", myViewHolder.hidden_danger_detail_item_crid.getText().toString().trim() + "-------" + qyid);
                context.startActivity(intent);
            }
        });

        myViewHolder.hidden_danger_detail_item_major_btn.setVisibility(bean.ismVisible() ? View.VISIBLE : View.GONE);
        //监听重大按钮
        myViewHolder.hidden_danger_detail_item_major_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddHiddenDangerRegistration.class);
                intent.putExtra("clickId", qyid);
                intent.putExtra("crid", crid);
                context.startActivity(intent);
            }
        });


        //再设置一次CheckBox的选中监听器，当CheckBox的选中状态发生改变时，把改变后的状态储存在数组中
        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                flag[position] = b;
                bean.setmVisible(!b);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView hidden_danger_detail_item_pczq;
        private TextView hidden_danger_detail_item_pcdeptname;
        private TextView hidden_danger_detail_item_namestr;
        private TextView hidden_danger_detail_item_rscdesc;

        private CheckBox checkBox;
        private Button hidden_danger_detail_item_genera_btn;
        private Button hidden_danger_detail_item_major_btn;
        private TextView hidden_danger_detail_item_index;
        private TextView hidden_danger_detail_item_crid;

        public MyViewHolder(View itemView) {
            super(itemView);
            hidden_danger_detail_item_pczq = (TextView) itemView.findViewById(R.id.hidden_danger_detail_item_pczq);
            hidden_danger_detail_item_pcdeptname = (TextView) itemView.findViewById(R.id.hidden_danger_detail_item_pcdeptname);
            hidden_danger_detail_item_namestr = (TextView) itemView.findViewById(R.id.hidden_danger_detail_item_namestr);
            hidden_danger_detail_item_rscdesc = (TextView) itemView.findViewById(R.id.hidden_danger_detail_item_rscdesc);
            hidden_danger_detail_item_crid = (TextView) itemView.findViewById(R.id.hidden_danger_detail_item_crid);

            checkBox = (CheckBox) itemView.findViewById(R.id.hidden_danger_detail_item_checkbox);
            hidden_danger_detail_item_genera_btn = (Button) itemView.findViewById(R.id.hidden_danger_detail_item_genera_btn);
            hidden_danger_detail_item_major_btn = (Button) itemView.findViewById(R.id.hidden_danger_detail_item_major_btn);
            hidden_danger_detail_item_index = (TextView) itemView.findViewById(R.id.hidden_danger_detail_item_index);
        }
    }

    public void DataNotify(List<HiddenDangerDetailBean> lists,boolean[] flag) {
        this.mDatas = lists;
        this.flag = flag;
        notifyDataSetChanged();
    }
}
