package com.example.administrator.dimine_sis;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.TodoListViewAdapter;
import bean.TodoBean;
import utils.BaseActivity;
import utils.ShowToast;

public class Todo extends BaseActivity {
    private ImageView todo_back;
    private ListView todo_listview;
    private ArrayList<TodoBean> mDatas;
    private String[] arr_name = {"隐患核实压滤工段", "副井-920中段隐患核实", "隐患核实测试"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        todo_back = (ImageView) findViewById(R.id.todo_back);
        todo_listview = (ListView) findViewById(R.id.todo_listview);
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<TodoBean>();
        for (int i = 0; i < 3; i++) {
            TodoBean todoEntity = new TodoBean();
            todoEntity.setTodoName(arr_name[i]);
            todoEntity.setTodoState("待处理");
            mDatas.add(todoEntity);
        }
        TodoListViewAdapter adapter = new TodoListViewAdapter(this, mDatas);
        todo_listview.setAdapter(adapter);

        todo_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowToast.showShort(Todo.this, "请前往平台端处理");
            }
        });
    }


    @Override
    protected void setOnClick() {
        todo_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.todo_back:
                finish();
                break;
            default:
                break;
        }
    }
}
