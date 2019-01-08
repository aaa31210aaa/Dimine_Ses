package hidden_danger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import adapter.HiddenDangerDetailAdapter;
import bean.HiddenBean;
import utils.BaseActivity;
import utils.GridItemDecoration;
import utils.ShowToast;

public class HiddenDanger extends BaseActivity {
    private ImageView hidden_danger_back;
    private RecyclerView hidden_danger_recycler;
    private List<HiddenBean> mDatas;
    //    private CheckBox hidden_danger_item_checkbox;
//    private CheckBox hidden_danger_check_all;
    private HiddenDangerDetailAdapter adapter;
    //    private boolean[] flag;
    private HiddenBean entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        hidden_danger_back = (ImageView) findViewById(R.id.hidden_danger_back);
        hidden_danger_recycler = (RecyclerView) findViewById(R.id.hidden_danger_recycler);
//        hidden_danger_item_checkbox = (CheckBox) findViewById(R.id.hidden_danger_item_checkbox);
//        hidden_danger_check_all = (CheckBox) findViewById(R.id.hidden_danger_check_all);
    }

    @Override
    protected void initData() {
        //设置布局类型
        hidden_danger_recycler.setLayoutManager(new LinearLayoutManager(this));
        hidden_danger_recycler.setHasFixedSize(true);
        hidden_danger_recycler.addItemDecoration(new GridItemDecoration(this));
//        flag = new boolean[50];
        mDatas = new ArrayList<HiddenBean>();
        for (int i = 1; i < 50; i++) {
            entity = new HiddenBean();
            entity.setDate("2017-02-08");
            entity.setInvestigationType("类型" + i);
            entity.setChecker("检查人" + i);
            mDatas.add(entity);
        }
        hidden_danger_recycler.setAdapter(new CommonAdapter<HiddenBean>(this, R.layout.hidden_danger_item, mDatas) {
            @Override
            protected void convert(final ViewHolder holder, HiddenBean hiddenEntity, final int position) {
                holder.setText(R.id.hidden_danger_item_date, mDatas.get(position).getDate());
                holder.setText(R.id.hidden_danger_item_type, mDatas.get(position).getInvestigationType());
                holder.setText(R.id.hidden_danger_item_checker, mDatas.get(position).getChecker());
                holder.setOnClickListener(R.id.hidden_danger_item_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowToast.showToastNowait(HiddenDanger.this, "这是点击的" + position);
                        Intent intent = new Intent(HiddenDanger.this, HiddenDangerDetail.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }


    @Override
    protected void setOnClick() {
        hidden_danger_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hidden_danger_back:
                finish();
                break;
        }
    }
}
