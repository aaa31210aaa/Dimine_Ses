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
public class YjCl extends BaseFragment {
    private View view;
    private ListView yj_cl_listview;
    private String url;
    private String user_token;
    private Intent intent;
    private String hxpid;
    private List<GaoduBean> mDatas;
    private GaoduJbkAdapter adapter;

    private String[] key = {"yhcf", "hl", "casno", "xlyjcl", "czzysx", "cczysx"};
    private String[] value = {"有害成分：", "含量：", "CAS No.：", "泄露应急处理：", "操作注意事项：", "存储注意事项："};


    public YjCl() {
        // Required empty public constructor
    }


    @Override
    public View makeView() {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_yj_cl, null);
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
        yj_cl_listview = (ListView) view.findViewById(R.id.yj_cl_listview);
        user_token = SharedPrefsUtil.getValue(getActivity(), "userInfo", "user_token", null);
        url = PortIpAddress.GetChemicaldetailPartSix();
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
                                yj_cl_listview.setAdapter(adapter);
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
