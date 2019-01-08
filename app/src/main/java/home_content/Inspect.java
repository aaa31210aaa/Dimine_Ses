package home_content;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;
import java.util.List;

import adapter.InspectAdapter;
import bean.EnterpriseBean;
import enterprise_information.EnterpriseInformation;
import utils.BaseActivity;
import utils.ShowToast;

public class Inspect extends BaseActivity {
    private ImageView inspect_back;
    private ImageView inspect_add;
    private EditText inspect_search;
    private ListView inspect_listview;
    private ImageView inspect_clear;
    private List<EnterpriseBean> mDatas;
    private List<EnterpriseBean> searchDatas;
    private InspectAdapter adapter;
    private String intnetIndex_zfjc;

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:

                    break;
                case 1:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        inspect_back = (ImageView) findViewById(R.id.inspect_back);
        inspect_add = (ImageView) findViewById(R.id.inspect_add);
        inspect_search = (EditText) findViewById(R.id.inspect_search);
        inspect_clear = (ImageView) findViewById(R.id.inspect_clear);
        inspect_listview = (ListView) findViewById(R.id.inspect_listview);
    }

    @Override
    protected void initData() {
        //初始化listview内容
        mDatas = new ArrayList<EnterpriseBean>();
        intnetIndex_zfjc = "inspect";
        for (int i = 0; i < 50; i++) {
            EnterpriseBean entity = new EnterpriseBean();
            entity.setInvestigationName("排查名称" + i);
            entity.setInvestigationType("排查类型" + i);
            entity.setInvestigators("排查人" + i);
            mDatas.add(entity);
        }
        adapter = new InspectAdapter(this, mDatas);
        inspect_listview.setAdapter(adapter);


        //listview点击事件
        inspect_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowToast.showShort(Inspect.this, "这是第" + position);
                Intent intent = new Intent(Inspect.this, EnterpriseInformation.class);
                intent.putExtra("intentIndex", intnetIndex_zfjc);
                startActivity(intent);
            }
        });


        //监听edittext
        inspect_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inspect_search.length() > 0) {
                    inspect_clear.setVisibility(View.VISIBLE);
                    search(inspect_search.getText().toString().trim());
                } else {
                    inspect_clear.setVisibility(View.GONE);
                    adapter.DataNotify(mDatas);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //搜索内容
    private void search(String str) {
        searchDatas = new ArrayList<>();
        for (EnterpriseBean entity : mDatas) {
            try {
                if (entity.getInvestigationName().contains(str) || entity.getInvestigationType().contains(str) || entity.getInvestigators().contains(str)) {
                    searchDatas.add(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        adapter.DataNotify(searchDatas);
    }

    @Override
    protected void setOnClick() {
        inspect_back.setOnClickListener(this);
        inspect_clear.setOnClickListener(this);
        inspect_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inspect_back:
                finish();
                break;
            case R.id.inspect_clear:
                inspect_search.setText("");
                inspect_clear.setVisibility(View.GONE);
                break;
            case R.id.inspect_add:
                Intent intent = new Intent(this, AddInspect.class);
                startActivity(intent);
                break;
        }
    }
}
