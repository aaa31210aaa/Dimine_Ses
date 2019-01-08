package wfp_query;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class GaoduJyk extends Fragment {
    private View view;
    private ListView gaodu_jyk_list;
    private List<GaoduBean> mDatas;

    private String url;
    private String user_token;
    private Intent intent;
    //高毒物品id
    private String gdwpid;

    private String[] key = {"firebom", "health", "safemeo", "keep", "evacuate", "fire", "leak", "rescue"};
    private String[] value = {"火灾及爆炸：", "健康：", "安全描述：", "防护服：", "现场疏散：", "火灾：", "泄露：", "救援："};


    public GaoduJyk() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_gaodu_jyk, null);
            initView();
            initData();
            setOnclick();
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
        gaodu_jyk_list = (ListView) view.findViewById(R.id.gaodu_jyk_list);
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
                            JSONArray jsonArray = jsonObject.getJSONArray("info");
                            mDatas = new ArrayList<>();
                            for (int i = 0; i < key.length; i++) {
                                GaoduBean bean = new GaoduBean();
                                bean.setZwmz(jsonArray.getJSONArray(2).getJSONObject(0).getString(key[i]));
                                bean.setYwmz(value[i]);
                                mDatas.add(bean);
                            }
                            GaoduJbkAdapter adapter = new GaoduJbkAdapter(getActivity(), mDatas);
                            gaodu_jyk_list.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void setOnclick() {

    }
}
