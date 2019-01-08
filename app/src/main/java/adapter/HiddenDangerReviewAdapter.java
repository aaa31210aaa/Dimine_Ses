package adapter;

import android.content.Context;

import java.util.List;

import utils.RecyclerCommonAdapter;
import utils.RecyclerViewHolder;

public class HiddenDangerReviewAdapter<T> extends RecyclerCommonAdapter<T> {

    public HiddenDangerReviewAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, T t) {

    }
}
