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
public class GaoduGzk extends Fragment {
    private View view;
    private ListView gaodu_gzk_list;

    private String url;
    private String user_token;
    private Intent intent;
    //高毒物品id
    private String gdwpid;

    private String[] key = {"km", "jkwh", "lhtx", "yjcl", "fhcs", "jjdh", "zxxx", "xfdh"};
    private String[] values = {"卡名：", "健康危害：", "理化特性：", "应急处理：", "防护措施：", "急救电话：", "咨询信息：", "消防电话："};
    private List<GaoduBean> mDatas;
    private GaoduJbkAdapter adapter;

    public GaoduGzk() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_gaodu_gzk, null);
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
        gaodu_gzk_list = (ListView) view.findViewById(R.id.gaodu_gzk_list);
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
                                bean.setZwmz(jsonArray.getJSONObject(1).getString(key[i]));
                                bean.setYwmz(values[i]);
                                mDatas.add(bean);
                            }
                            adapter = new GaoduJbkAdapter(getActivity(), mDatas);
                            gaodu_gzk_list.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void setOnclick() {

    }

}
