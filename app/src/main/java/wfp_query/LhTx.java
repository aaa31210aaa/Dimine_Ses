package wfp_query;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import utils.BaseFragment;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class LhTx extends BaseFragment {
    private View view;
    private ListView lh_tx_listview;
    private String url;
    private String user_token;
    private Intent intent;
    private String hxpid;
    private List<GaoduBean> mDatas;
    private GaoduJbkAdapter adapter;

    private String[] key = {"zycf", "wgyxz", "phz", "rd", "fd", "xdmd", "xdzqmd", "bhzqy", "rsr", "ljwd", "ljyl", "xcdsz", "sd", "yrwd", "bzsx", "bzxx", "rjx", "zyyt", "qtlhxz"};
    private String[] value = {"主要成分：", "外观与形状：", "PH：", "熔点（℃）：", "沸点（℃）：", "相对密度（水=1）：", "相对蒸气密度：", "饱和蒸气压（kPa）：", "燃烧热（kJ/mol）："
            , "临界温度（℃）：", "临界压力（MPa）：", "辛醇/水分配系数的对数值：", "闪点（℃）：", "引燃温度（℃）：", "爆炸上限%（V/V）：", "爆炸下限%（V/V）：", "溶解性：", "主要用途：", "其它理化性质："};

    public LhTx() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_lh_tx, null);
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    protected void loadData() {
        lh_tx_listview = (ListView) view.findViewById(R.id.lh_tx_listview);
        user_token = SharedPrefsUtil.getValue(getActivity(), "userInfo", "user_token", null);
        url = PortIpAddress.GetChemicaldetailPartThree();
        intent = getActivity().getIntent();
        hxpid = intent.getStringExtra("hxpid");
        dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.loading);
        mOkhttp();
    }


    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("hxpid", hxpid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(getActivity(), R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("ces", jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                mDatas = new ArrayList<>();
                                for (int i = 0; i < key.length; i++) {
                                    GaoduBean bean = new GaoduBean();
                                    bean.setZwmz(jsonArray.getJSONObject(0).getString(key[i]));
                                    bean.setYwmz(value[i]);
                                    mDatas.add(bean);
                                }
                                adapter = new GaoduJbkAdapter(getActivity(), mDatas);
                                lh_tx_listview.setAdapter(adapter);
                            } else {
                                view = View.inflate(getActivity(), R.layout.nodata, null);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
