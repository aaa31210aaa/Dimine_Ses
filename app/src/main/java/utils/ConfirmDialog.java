package utils;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.dimine_sis.DialogCallback;
import com.example.administrator.dimine_sis.R;

public class ConfirmDialog extends Dialog {
    DialogCallback callback;
    private TextView content;
    private TextView sureBtn;
    private TextView cancleBtn;

    public ConfirmDialog(Context context, DialogCallback callback) {
        super(context, R.style.Dialog);
        this.callback = callback;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.update_dialog, null);
        sureBtn = (TextView) mView.findViewById(R.id.positiveButton);
        cancleBtn = (TextView) mView.findViewById(R.id.negativeButton);
        content = (TextView) mView.findViewById(R.id.message);


        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(1);
                ConfirmDialog.this.cancel();
            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(0);
                ConfirmDialog.this.cancel();
            }
        });
        super.setContentView(mView);
    }


    public ConfirmDialog setContent(String s) {
        content.setText(s);
        return this;
    }
}
