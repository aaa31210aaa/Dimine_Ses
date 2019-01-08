package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;

import bean.TodoBean;
import utils.ListViewHolder;

public class TodoListViewAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<TodoBean> mDatas;

    public TodoListViewAdapter(Context context, ArrayList<TodoBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.todo_listview_item,null);
        }
        TextView todo_listview_item_name = ListViewHolder.get(convertView,R.id.todo_listview_item_name);
        TextView todo_listview_item_state = ListViewHolder.get(convertView,R.id.todo_listview_item_state);
        TodoBean todoEntity = mDatas.get(position);
        todo_listview_item_name.setText(todoEntity.getTodoName());
        todo_listview_item_state.setText(todoEntity.getTodoState());

        return convertView;
    }
}
