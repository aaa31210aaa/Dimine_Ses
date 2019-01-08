package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;
import java.util.List;

import risk_management.PhotoView;

public class QyYhInfoDetailAdapter extends RecyclerView.Adapter<QyYhInfoDetailAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<String> mImages;
    private MyViewHolder myViewHolder;
    private OnItemClickLitener mOnItemClickLitener;

    public QyYhInfoDetailAdapter(Context context, List<String> mImages) {
        this.context = context;
        this.mImages = mImages;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public QyYhInfoDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.qyyh_info_detail_recycler, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    /**
     * 绑定viewholder里面的数据
     * 加载图片显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final QyYhInfoDetailAdapter.MyViewHolder holder, final int position) {
        Glide
                .with(context)
                .load(mImages.get(position))
                .placeholder(R.mipmap.default_error)
                .error(R.mipmap.default_error)
                .centerCrop()
                .crossFade()
                .into(holder.qyyh_info_detail_recycler_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoView.class);
                intent.putExtra("position", position);
                intent.putStringArrayListExtra("urls", (ArrayList<String>) mImages);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView qyyh_info_detail_recycler_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            qyyh_info_detail_recycler_image = (ImageView) itemView.findViewById(R.id.qyyh_info_detail_recycler_image);
        }
    }
}
