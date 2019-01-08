package wfp_query;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.GaoduJbkAdapter;
import bean.GaoduBean;
import okhttp3.Call;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class GaoduJbk extends Fragment {
    private View view;
    private ListView gaodu_jbk_listview;
    private List<GaoduBean> mDatas;
    private GaoduJbkAdapter adapter;

    private String url;
    private String user_token;
    private Intent intent;
    //高毒物品id
    private String gdwpid;

    private String[] key = {"cash", "zwmz", "ywmz", "gwwpbm", "bm", "lhxz", "zyjc", "jrtj", "jkyx", "zyjcxz", "gzcsjc", "fhss", "tjxm", "tjzq", "zyjj", "jjhzl", "knyqdzyb"};
    private String[] value = {"CAS号：", "中文名字：", "英文名字：", "高危物品编码：", "别名：", "理化性质：", "职业接触：", "进入途径：", "健康影响：", "职业接触限值：", "工作场所监测：", "防护设置和个人：", "体检项目：", "体检周期：", "职业禁忌：", "急救和治疗：", "可能引起的职业病："};

    public GaoduJbk() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_gaodu_jbk, null);
            initView();
            initData();
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    private void initView() {
        gaodu_jbk_listview = (ListView) view.findViewById(R.id.gaodu_jbk_listview);
    }

    private void initData() {
        user_token = SharedPrefsUtil.getValue(getActivity(), "userInfo", "user_token", null);
        url = PortIpAddress.HtoxicmatterDetail();
        intent = getActivity().getIntent();
        gdwpid = intent.getStringExtra("gdwpid");
        mOkhttp();
    }


    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("gdwpid", gdwpid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast.showToastNoWait(getActivity(), R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("ces", jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("info");
                            mDatas = new ArrayList<>();
                            for (int i = 0; i < key.length; i++) {
                                GaoduBean bean = new GaoduBean();
                                bean.setZwmz(jsonArray.getJSONObject(0).getString(key[i]));
                                bean.setYwmz(value[i]);
                                mDatas.add(bean);
                            }
                            adapter = new GaoduJbkAdapter(getActivity(), mDatas);
                            gaodu_jbk_listview.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



}
