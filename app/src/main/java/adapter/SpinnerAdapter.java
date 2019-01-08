package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items = new String[]{};

    public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }

    //Spinner上显示数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setPadding(20, 0, 60, 0);
        tv.setSingleLine(false);
        tv.setText(items[position]);
        tv.setTextColor(Color.BLACK);
        return convertView;
    }

    //下拉框里的数据
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.my_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setSingleLine(false);
        tv.setText(items[position]);
        tv.setTextColor(Color.BLACK);
        return convertView;
    }
}
