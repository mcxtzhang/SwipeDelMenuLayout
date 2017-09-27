package mcxtzhang.swipedelmenu.viewpager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

import mcxtzhang.listswipemenudemo.R;
import mcxtzhang.swipedelmenu.SwipeBean;

/**
 * Intro:
 * Author: zhangxutong
 * E-mail: mcxtzhang@163.com
 * Home Page: http://blog.csdn.net/zxt0601
 * Created:   2017/9/27.
 * History:
 */

public class FullDemoFragment extends Fragment {
    public static FullDemoFragment newInstance(int position) {
        Bundle args = new Bundle();
        FullDemoFragment fragment = new FullDemoFragment();
        args.putInt("index", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndex = getArguments().getInt("index");
    }

    private static final String TAG = "zxt";
    private RecyclerView mRecyclerView;
    private CommonAdapter<SwipeBean> mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<SwipeBean> mDatas;
    private int mIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.fragment_full_del_demo, container, false);

        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.rv);
        //区分一下不同页面
        switch (mIndex) {
            case 0:
                mRecyclerView.setBackgroundColor(Color.WHITE);
                break;
            case 1:
                mRecyclerView.setBackgroundColor(Color.YELLOW);
                break;
            default:
                mRecyclerView.setBackgroundColor(Color.GREEN);
                break;
        }


        initDatas();
        mAdapter = new CommonAdapter<SwipeBean>(getContext(), mDatas, R.layout.item_cst_swipe) {
            @Override
            public void convert(final ViewHolder holder, SwipeBean swipeBean) {
                ((SwipeMenuLayout) holder.itemView).setIos(true).setLeftSwipe(mIndex == 0 ? false : true);// 并依次打开左滑右滑

                holder.setText(R.id.content, swipeBean.name + (mIndex == 0 ? "我左青龙" : "我右白虎"));

                //验证长按
                holder.setOnLongClickListener(R.id.content, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(mContext, "longclig", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onLongClick() called with: v = [" + v + "]");
                        return false;
                    }
                });

                holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        if (pos >= 0 && pos < mDatas.size()) {
                            Toast.makeText(getContext(), "删除:" + pos, Toast.LENGTH_SHORT).show();
                            mDatas.remove(pos);
                            mAdapter.notifyItemRemoved(pos);//推荐用这个
                            //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                            //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                            //mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
                (holder).setOnClickListener(R.id.content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "onClick:" + mDatas.get(holder.getAdapterPosition()).name, Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onClick() called with: v = [" + v + "]");
                    }
                });
                //置顶：
                holder.setOnClickListener(R.id.btnTop, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();

                        if (pos > 0 && pos < mDatas.size()) {
                            SwipeBean swipeBean = mDatas.get(pos);
                            mDatas.remove(swipeBean);
                            mAdapter.notifyItemInserted(0);
                            mDatas.add(0, swipeBean);
                            mAdapter.notifyItemRemoved(pos + 1);
                            if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                                mRecyclerView.scrollToPosition(0);
                            }
                            //notifyItemRangeChanged(0,holder.getAdapterPosition()+1);
                        }

                    }
                });


            }
        };

        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(getContext(), 2));
        mRecyclerView.setLayoutManager(mLayoutManager = new LinearLayoutManager(getContext()));

        //6 2016 10 21 add , 增加viewChache 的 get()方法，
        // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大，
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });

        return inflate;
    }


    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new SwipeBean("" + i));
        }
    }
}
