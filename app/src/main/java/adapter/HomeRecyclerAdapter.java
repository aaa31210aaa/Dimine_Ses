package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;

import bean.HomeBean;


public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<HomeBean> mDates;
    private ArrayList<HomeBean> mImages;
    private LayoutInflater inflater;
    private MyViewHolder myViewHolder;
    private OnItemClickLitener mOnItemClickLitener;


    public HomeRecyclerAdapter(Context context, ArrayList<HomeBean> mDates, ArrayList<HomeBean> mImages) {
        this.context = context;
        this.mDates = mDates;
        this.mImages = mImages;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_recyclerview_item, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    /**
     * 绑定viewholder里面的数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        HomeBean imghomeEntity = mImages.get(position);
        HomeBean tvhomeEntity = mDates.get(position);
        holder.home_recyclerview_item_img.setImageResource(imghomeEntity.getRecycler_img());
        holder.home_recyclerview_item_tv.setText(tvhomeEntity.getRecycler_tv());

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView home_recyclerview_item_img;
        TextView home_recyclerview_item_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            home_recyclerview_item_img = (ImageView) itemView.findViewById(R.id.home_recyclerview_item_img);
            home_recyclerview_item_tv = (TextView) itemView.findViewById(R.id.home_recyclerview_item_tv);
        }
    }
}
