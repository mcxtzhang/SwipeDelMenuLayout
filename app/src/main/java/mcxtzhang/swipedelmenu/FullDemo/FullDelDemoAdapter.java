package mcxtzhang.swipedelmenu.FullDemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import mcxtzhang.listswipemenudemo.R;
import mcxtzhang.swipedelmenu.SwipeBean;

/**
 * 介绍：
 * 作者：zhangxutong
 * 邮箱：zhangxutong@imcoming.com
 * 时间： 2016/9/12.
 */

public class FullDelDemoAdapter extends RecyclerView.Adapter<FullDelDemoAdapter.FullDelDemoVH> {
    private Context mContext;
    private LayoutInflater mInfalter;
    private List<SwipeBean> mDatas;

    public FullDelDemoAdapter(Context context, List<SwipeBean> mDatas) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public FullDelDemoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FullDelDemoVH(mInfalter.inflate(R.layout.item_cst_swipe, parent, false));
    }

    @Override
    public void onBindViewHolder(final FullDelDemoVH holder, int position) {
        holder.tv.setText(mDatas.get(position).name);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnDelListener) {
                    mOnDelListener.onDel(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != mDatas ? mDatas.size() : 0;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onDelListener {
        void onDel(int pos);
    }

    private onDelListener mOnDelListener;

    public onDelListener getOnDelListener() {
        return mOnDelListener;
    }

    public void setOnDelListener(onDelListener mOnDelListener) {
        this.mOnDelListener = mOnDelListener;
    }

    class FullDelDemoVH extends RecyclerView.ViewHolder {
        TextView tv;
        Button btnDelete;

        public FullDelDemoVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}

