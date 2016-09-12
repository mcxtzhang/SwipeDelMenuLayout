package mcxtzhang.swipedelmenu.FullDemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mcxtzhang.listswipemenudemo.R;
import mcxtzhang.swipedelmenu.SwipeBean;

/**
 * 介绍：完整的删除Demo
 * 作者：zhangxutong
 * 邮箱：zhangxutong@imcoming.com
 * 时间： 2016/9/12.
 */

public class FullDelDemoActivity extends Activity {
    private static final String TAG = "zxt";
    private RecyclerView mRv;
    private FullDelDemoAdapter mAdapter;
    private List<SwipeBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_del_demo);
        mRv = (RecyclerView) findViewById(R.id.rv);

        initDatas();
        mAdapter = new FullDelDemoAdapter(this, mDatas);
        mAdapter.setOnDelListener(new FullDelDemoAdapter.onDelListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(FullDelDemoActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                mDatas.remove(pos);
                mAdapter.notifyItemRemoved(pos);//推荐用这个
                //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                //mAdapter.notifyDataSetChanged();
            }
        });
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new SwipeBean("" + i));
        }
    }
}
