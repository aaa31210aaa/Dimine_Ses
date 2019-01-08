package home_content;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class AddInspect extends BaseActivity {
    private ImageView add_inspect_back;
    private ImageView add_inspect_submit;
    //排查类型
    private TextView add_inspect_type;
    //排查名称
    private TextView add_inspect_name;
    //排查人
    private TextView add_inspect_man;
    //备注
    private TextView add_inspect_bz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inspect);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {

    }

    @Override
    public void onClick(View v) {

    }
}
