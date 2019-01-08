package data_report;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class DataReport extends BaseActivity implements OnChartValueSelectedListener {
    private ImageView data_report_back;
    private TextView data_report_switch;
    //企业用户
    private TextView report_comusertotal;
    //待审核企业
    private TextView report_toauditusertotal;
    //统计日期
    private TextView report_tjdate;
    //本月上报一般隐患
    private TextView report_bysbybdangertotal;
    //整改日期到期的一般隐患
    private TextView report_dqdangertotal;
    //未整改完成的一般隐患
    private TextView report_bywwcybdangertotal;
    //本月上报重大隐患
    private TextView report_bysbzddangertotal;
    //等待审核的重大隐患
    private TextView report_toauditdangertotal;
    //未整改完成的重大隐患
    private TextView report_bywwczddangertotal;
    //本月未上报企业
    private TextView report_wsbcomtotal;
    //本年度共排查一般隐患
    private TextView report_yearybdangertotal;
    //其中已整改一般隐患
    private TextView report_havechangedybtotal;
    //其中未整改一般隐患
    private TextView report_nochangeybtotal;
    //本年度共排查重大隐患
    private TextView report_yearzddangertotal;
    //其中已整改重大隐患
    private TextView report_havechangedzdtotal;
    //其中未整改重大隐患
    private TextView report_nochangezdtotal;
    //本年度共排查隐患
    private TextView report_dangertotal;
    //整改率
    private TextView report_zgl;


    private boolean myBit = true;

    //柱形图
    private BarChart barChart;
    private BarDataSet dataset, dataset2, dataset3;
    //柱形图X轴数据集
    private ArrayList<String> barXValues = new ArrayList<String>();
    private XAxis barXAxis; //X坐标轴
    private YAxis barYAxis; //Y

    //总隐患
    private ArrayList<BarEntry> barTotalNum = new ArrayList<BarEntry>();
    //已完成的
    private ArrayList<BarEntry> barCompleted = new ArrayList<BarEntry>();
    //未完成的
    private ArrayList<BarEntry> barIncomplete = new ArrayList<BarEntry>();

    //折线图
    private LineChart lineChart;
    private LineDataSet ldateset, ldataset2, ldataset3;
    //折线图X轴数据集
    private ArrayList<String> lineXValues = new ArrayList<String>();
    private XAxis lineXAxis;
    private YAxis lineYAxis;

    //总隐患
    private ArrayList<Entry> lineTotalNum = new ArrayList<Entry>();
    //已完成的
    private ArrayList<Entry> lineCompleted = new ArrayList<Entry>();
    //未完成的
    private ArrayList<Entry> lineIncomplete = new ArrayList<Entry>();

    private int value1;
    private int value2;
    private int value3;

    private float xscaleLine = 2;

    private String url;
    private String user_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_report);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        data_report_back = (ImageView) findViewById(R.id.data_report_back);
        data_report_switch = (TextView) findViewById(R.id.data_report_switch);

        report_comusertotal = (TextView) findViewById(R.id.report_comusertotal);
        report_toauditusertotal = (TextView) findViewById(R.id.report_toauditusertotal);
        report_tjdate = (TextView) findViewById(R.id.report_tjdate);
        report_bysbybdangertotal = (TextView) findViewById(R.id.report_bysbybdangertotal);
        report_dqdangertotal = (TextView) findViewById(R.id.report_dqdangertotal);
        report_bywwcybdangertotal = (TextView) findViewById(R.id.report_bywwcybdangertotal);
        report_bysbzddangertotal = (TextView) findViewById(R.id.report_bysbzddangertotal);
        report_toauditdangertotal = (TextView) findViewById(R.id.report_toauditdangertotal);
        report_bywwczddangertotal = (TextView) findViewById(R.id.report_bywwczddangertotal);
        report_wsbcomtotal = (TextView) findViewById(R.id.report_wsbcomtotal);
        report_yearybdangertotal = (TextView) findViewById(R.id.report_yearybdangertotal);
        report_havechangedybtotal = (TextView) findViewById(R.id.report_havechangedybtotal);
        report_nochangeybtotal = (TextView) findViewById(R.id.report_nochangeybtotal);
        report_yearzddangertotal = (TextView) findViewById(R.id.report_yearzddangertotal);
        report_havechangedzdtotal = (TextView) findViewById(R.id.report_havechangedzdtotal);
        report_nochangezdtotal = (TextView) findViewById(R.id.report_nochangezdtotal);
        report_dangertotal = (TextView) findViewById(R.id.report_dangertotal);
        report_zgl = (TextView) findViewById(R.id.report_zgl);

        barChart = (BarChart) findViewById(R.id.data_report_barchart);
        lineChart = (LineChart) findViewById(R.id.data_report_linechart);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.GetTjData();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        setBarChart();
        setLineChart();
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(DataReport.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            report_comusertotal.setText(jsonArray.getJSONObject(0).getString("comusertotal"));
                            report_toauditusertotal.setText(jsonArray.getJSONObject(0).getString("toauditusertotal"));
                            report_tjdate.setText(jsonArray.getJSONObject(0).getString("tjdate"));
                            report_bysbybdangertotal.setText(jsonArray.getJSONObject(0).getString("bysbybdangertotal"));
                            report_dqdangertotal.setText(jsonArray.getJSONObject(0).getString("dqdangertotal"));
                            report_bywwcybdangertotal.setText(jsonArray.getJSONObject(0).getString("bywwcybdangertotal"));
                            report_bysbzddangertotal.setText(jsonArray.getJSONObject(0).getString("bysbzddangertotal"));
                            report_toauditdangertotal.setText(jsonArray.getJSONObject(0).getString("toauditdangertotal"));
                            report_bywwczddangertotal.setText(jsonArray.getJSONObject(0).getString("bywwczddangertotal"));
                            report_wsbcomtotal.setText(jsonArray.getJSONObject(0).getString("wsbcomtotal"));
                            report_yearybdangertotal.setText(jsonArray.getJSONObject(0).getString("yearybdangertotal"));
                            report_havechangedybtotal.setText(jsonArray.getJSONObject(0).getString("havechangedybtotal"));
                            report_nochangeybtotal.setText(jsonArray.getJSONObject(0).getString("nochangeybtotal"));
                            report_yearzddangertotal.setText(jsonArray.getJSONObject(0).getString("yearzddangertotal"));
                            report_havechangedzdtotal.setText(jsonArray.getJSONObject(0).getString("havechangedzdtotal"));
                            report_nochangezdtotal.setText(jsonArray.getJSONObject(0).getString("nochangezdtotal"));
                            report_dangertotal.setText(jsonArray.getJSONObject(0).getString("dangertotal"));
                            report_zgl.setText(jsonArray.getJSONObject(0).getString("zgl"));
                            setTextColorAndClick(report_bysbybdangertotal);
                            setTextColorAndClick(report_dqdangertotal);
                            setTextColorAndClick(report_bywwcybdangertotal);
                            setTextColorAndClick(report_bysbzddangertotal);
                            setTextColorAndClick(report_toauditdangertotal);
                            setTextColorAndClick(report_bywwczddangertotal);
                            setTextColorAndClick(report_wsbcomtotal);
                            setTextColorAndClick(report_yearybdangertotal);
                            setTextColorAndClick(report_havechangedybtotal);
                            setTextColorAndClick(report_nochangeybtotal);
                            setTextColorAndClick(report_yearzddangertotal);
                            setTextColorAndClick(report_havechangedzdtotal);
                            setTextColorAndClick(report_nochangezdtotal);
                            setTextColorAndClick(report_dangertotal);
                            setTextColorAndClick(report_zgl);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setTextColorAndClick(TextView textView) {
        if (textView.getText().toString().equals("0")) {
            textView.setTextColor(Color.rgb(78, 238, 148));
            textView.setEnabled(false);
        } else {
            textView.setTextColor(Color.rgb(255, 0, 0));
            textView.setEnabled(true);
        }
    }


    private void setLineChart() {
        //设置数据描述
        lineChart.setDescription("");
        //设置动画
        lineChart.animateX(3000);
        lineChart.animateY(3000);
        //设置缩放是否在XY轴同时进行
        lineChart.setPinchZoom(false);
        //设置表的默认初始放大
//        ViewPortHandler lineHandler = lineChart.getViewPortHandler();
//        Matrix matrix = lineHandler.getMatrixTouch();
//        matrix.postScale(xscaleLine, 1);
        //将表格左移距离
//        lineHandler.setDragOffsetY(50);
        //启用手指触摸
//        linechart.setTouchEnabled(true);

        //设置格子背景色,参数是Color类型对象
        lineChart.setDrawGridBackground(false);
        //将标注字设置在下方
        lineXAxis = lineChart.getXAxis();
        lineXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X间隔为1
        lineXAxis.setSpaceBetweenLabels(1);
        lineYAxis = lineChart.getAxisLeft();
        //不要竖线网格
        lineXAxis.setDrawGridLines(false);
        // 隐藏右边 的坐标轴
        lineChart.getAxisRight().setEnabled(true);
        lineXAxis.setAvoidFirstLastClipping(false);
        initLineChartData(10, lineTotalNum, lineCompleted, lineIncomplete);  //添加Y轴数据
    }

    //这只柱形图
    private void setBarChart() {
        //设置数据描述
        barChart.setDescription("");
        //设置
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        //设置表的默认初始放大
        ViewPortHandler lineHandler = barChart.getViewPortHandler();
        Matrix matrix = lineHandler.getMatrixTouch();
        matrix.postScale(xscaleLine, 1);
        //设置纵向动画
//        barChart.animateX(3000);
        barChart.animateY(3000);
        //设置缩放是否在XY轴同时进行
        barChart.setPinchZoom(false);
        //设置格子背景色,参数是Color类型对象
        barChart.setDrawGridBackground(false);
        //将标注字设置在下方
        barXAxis = barChart.getXAxis();
        barXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //如果设置为true，图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
//      xAxis.setAvoidFirstLastClipping(true);
        //设置X间隔为1
        barXAxis.setSpaceBetweenLabels(1);
        barYAxis = barChart.getAxisLeft();

        //不要竖线网格
        barXAxis.setDrawGridLines(false);
        // 隐藏右边 的坐标轴
        barChart.getAxisRight().setEnabled(true);
        barXAxis.setAvoidFirstLastClipping(false);
        initBarChartData(10, barTotalNum, barCompleted, barIncomplete);  //添加Y轴数据
    }

    //设置折线图数据
    private void initLineChartData(int count, ArrayList<Entry> lineTotalNum, ArrayList<Entry> lineCompleted, ArrayList<Entry> lineIncomplete) {
        //给x轴赋值
        for (int i = 0; i < count; i++) {
            lineXValues.add("第" + (i + 1) + "天");
        }
        //总隐患数
        for (int j = 0; j < count; j++) {
            value1 = (int) (Math.random() * 80/*80以内的随机数*/) + 3;
            lineTotalNum.add(new Entry(value1, j));
        }
        //完成数
        for (int k = 0; k < count; k++) {
            value2 = (int) (Math.random() * 50/*50以内的随机数*/) + 3;
            lineCompleted.add(new Entry(value2, k));
        }
        //未完成数
        for (int p = 0; p < count; p++) {
            value3 = (int) (Math.random() * 20/*20以内的随机数*/) + 3;
            lineIncomplete.add(new Entry(value3, p));
        }


        ldateset = new LineDataSet(lineTotalNum, "隐患总数(个)");
        ldataset2 = new LineDataSet(lineCompleted, "已整改隐患数(个)");
        ldataset3 = new LineDataSet(lineIncomplete, "未整改隐患数(个)");
        //设置折线图颜色
        ldateset.setColor(ContextCompat.getColor(this, R.color.yhzs));
        ldataset2.setColor(ContextCompat.getColor(this, R.color.yhywc));
        ldataset3.setColor(ContextCompat.getColor(this, R.color.yhwwc));
        ldateset.setCircleColor(ContextCompat.getColor(this, R.color.yhzs));
        ldataset2.setCircleColor(ContextCompat.getColor(this, R.color.yhywc));
        ldataset3.setCircleColor(ContextCompat.getColor(this, R.color.yhwwc));


        //设置Y轴数据为整数
        ldateset.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "";
            }
        });

        ldataset2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "";
            }
        });

        ldataset3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "";
            }
        });


        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(ldateset);
        lineDataSets.add(ldataset2);
        lineDataSets.add(ldataset3);
        LineData data = new LineData(lineXValues, lineDataSets);
        lineChart.setData(data);
        lineChart.setVisibleXRangeMaximum(4f);
        //刷新
        lineChart.invalidate();
    }


    //设置柱形图数据
    private void initBarChartData(int count, ArrayList<BarEntry> barTotalNum, ArrayList<BarEntry> barCompleted, ArrayList<BarEntry> barIncomplete) {
        //给X轴赋值
        for (int i = 0; i < count; i++) {
            barXValues.add("第" + (i + 1) + "天");
        }
        //总隐患数
        for (int j = 0; j < count; j++) {
            value1 = (int) (Math.random() * 80/*80以内的随机数*/) + 3;
            barTotalNum.add(new BarEntry(value1, j));
        }
        //完成数
        for (int k = 0; k < count; k++) {
            value2 = (int) (Math.random() * 50) + 3;
            barCompleted.add(new BarEntry(value2, k));
        }
        //未完成数
        for (int p = 0; p < count; p++) {
            value3 = (int) ((Math.random() * 20) + 3);
            barIncomplete.add(new BarEntry(value3, p));
        }

        dataset = new BarDataSet(barTotalNum, "隐患总数(个)");
        dataset2 = new BarDataSet(barCompleted, "已整改隐患数(个)");
        dataset3 = new BarDataSet(barIncomplete, "未整改隐患数(个)");
//        dataset.setBarSpacePercent(50f);
//        dataset2.setBarSpacePercent(50f);
//        dataset3.setBarSpacePercent(50f);
        //设置柱形图颜色
        dataset.setColor(ContextCompat.getColor(this, R.color.yhzs));
        dataset2.setColor(ContextCompat.getColor(this, R.color.yhywc));
        dataset3.setColor(ContextCompat.getColor(this, R.color.yhwwc));
        //设置Y轴数据为整数
        dataset.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "";
            }
        });

        dataset2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "";
            }
        });

        dataset3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "";
            }
        });


        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(dataset);
        barDataSets.add(dataset2);
        barDataSets.add(dataset3);
        BarData data = new BarData(barXValues, barDataSets);
        barChart.setData(data);
//        barChart.setVisibleXRangeMaximum(19);
        //刷新
        barChart.invalidate();
    }


    @Override
    protected void setOnClick() {
        data_report_back.setOnClickListener(this);
        data_report_switch.setOnClickListener(this);
        report_bysbybdangertotal.setOnClickListener(this);
        report_dqdangertotal.setOnClickListener(this);
        report_bywwcybdangertotal.setOnClickListener(this);
        report_bysbzddangertotal.setOnClickListener(this);
        report_toauditdangertotal.setOnClickListener(this);
        report_bywwczddangertotal.setOnClickListener(this);
        report_wsbcomtotal.setOnClickListener(this);
        report_yearybdangertotal.setOnClickListener(this);
        report_havechangedybtotal.setOnClickListener(this);
        report_nochangeybtotal.setOnClickListener(this);
        report_yearzddangertotal.setOnClickListener(this);
        report_havechangedzdtotal.setOnClickListener(this);
        report_nochangezdtotal.setOnClickListener(this);
        report_dangertotal.setOnClickListener(this);
        report_zgl.setOnClickListener(this);
        barChart.setOnChartValueSelectedListener(DataReport.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_report_back:
                finish();
                break;
            case R.id.data_report_switch:
                if (myBit) {
                    lineChart.setVisibility(View.GONE);
                    barChart.setVisibility(View.VISIBLE);
                    myBit = false;
                } else {
                    lineChart.setVisibility(View.VISIBLE);
                    barChart.setVisibility(View.GONE);
                    myBit = true;
                }
                break;
            case R.id.report_bysbybdangertotal:
                break;
            case R.id.report_dqdangertotal:
                break;
            case R.id.report_bywwcybdangertotal:
                break;
            case R.id.report_bysbzddangertotal:
                break;
            case R.id.report_toauditdangertotal:
                break;
            case R.id.report_bywwczddangertotal:
                break;
            case R.id.report_wsbcomtotal:
                break;
            case R.id.report_yearybdangertotal:
                break;
            case R.id.report_havechangedybtotal:
                break;
            case R.id.report_nochangeybtotal:
                break;
            case R.id.report_yearzddangertotal:
                break;
            case R.id.report_havechangedzdtotal:
                break;
            case R.id.report_nochangezdtotal:
                break;
            case R.id.report_dangertotal:
                break;
            case R.id.report_zgl:
                break;
        }
    }

    //高亮选择回调
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
