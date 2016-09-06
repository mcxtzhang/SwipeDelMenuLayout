package mcxtzhang.swipedelmenu.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用ListViewAdapter
 * Created by zhangxutong .
 * Date: 16/03/11
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    private static final String TAG = "zxt";
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        Log.d(TAG, "getView() called with: " + "position = [" + position + "], convertView = [" + convertView + "], parent = [" + parent + "]");
        convert(holder, getItem(position), position, convertView);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t, int position, View convertView);

    /**
     * 刷新数据，初始化数据
     *
     * @param list
     */
    public void setDatas(List<T> list) {
        if (this.mDatas != null) {
            if (null != list) {
                List<T> temp = new ArrayList<>();
                temp.addAll(list);
                this.mDatas.clear();
                this.mDatas.addAll(temp);
            } else {
                this.mDatas.clear();
            }
        } else {
            this.mDatas = list;
        }
        notifyDataSetChanged();
    }

    /**
     * 删除数据
     *
     * @param i
     */
    public void remove(int i) {
        if (null != mDatas && mDatas.size() > i && i > -1) {
            mDatas.remove(i);
            notifyDataSetChanged();
        }
    }

    /**
     * 加载更多数据
     *
     * @param list
     */
    public void addDatas(List<T> list) {
        if (null != list) {
            List<T> temp = new ArrayList<>();
            temp.addAll(list);
            if (this.mDatas != null) {
                this.mDatas.addAll(temp);
            } else {
                this.mDatas = temp;
            }
            notifyDataSetChanged();
        }

    }

    public List<T> getDatas() {
        return mDatas;
    }

}
