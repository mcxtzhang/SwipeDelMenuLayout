package mcxtzhang.swipedelmenu.FullDemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mcxtzhang.listswipemenudemo.R;
import mcxtzhang.swipedelmenu.SwipeBean;
import mcxtzhang.swipedelmenu.view.CstSwipeDelMenu;

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
    public void onBindViewHolder(final FullDelDemoVH holder, final int position) {
        ((CstSwipeDelMenu) holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);//这句话关掉IOS阻塞式交互效果
        holder.content.setText(mDatas.get(position).name + (position % 2 == 0 ? "我右白虎" : "我左青龙"));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnDelListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnDelListener.onDel(holder.getAdapterPosition());
                }
            }
        });
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        (holder.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "" + mDatas.get(position).name, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
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
        TextView content;
        Button btnDelete;

        public FullDelDemoVH(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}

