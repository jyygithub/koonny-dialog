package com.jiangyy.easydialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JYY on 2017/8/4.
 */

public class SingleChoiceDialog {

    public static class Builder {

        private Context mContext;
        private Dialog mDialog;
        private View mView;
        private ViewHolder mViewHolder;
        private ListAdapter mAdapter;
        private DisplayMetrics dm;
        private WindowManager windowManager;

        public Builder(Context context) {
            mContext = context;
            initView();
        }

        public Builder setTitle(CharSequence title) {
            mViewHolder.tvTitle.setText(title);
            return this;
        }

        public Builder setTitle(CharSequence title, int color) {
            mViewHolder.tvTitle.setText(title);
            mViewHolder.tvTitle.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setCancelTextColor(int color) {
            mViewHolder.tvCancel.setTextColor(color);
            return this;
        }

        public Builder addList(List<String> list) {
            mAdapter.addData(list);
            if (mAdapter.getCount() >= 10)
                mViewHolder.lvListView.getLayoutParams().height = (int) (dm.heightPixels * 0.65);
            return this;
        }

        public Builder addList(String[] list) {
            mAdapter.addData(Arrays.asList(list));
            if (mAdapter.getCount() >= 10)
                mViewHolder.lvListView.getLayoutParams().height = (int) (dm.heightPixels * 0.65);
            return this;
        }

        public Builder addList(String list) {
            mAdapter.addData(list);
            if (mAdapter.getCount() >= 10)
                mViewHolder.lvListView.getLayoutParams().height = (int) (dm.heightPixels * 0.65);
            return this;
        }

        public Builder setOnItemClickListener(final OnItemClickListener onItemClickListener) {
            mViewHolder.lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dismiss();
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(mAdapter.getData().get(i), i);
                    }
                }
            });
            return this;
        }

        private void initView() {

            mDialog = new Dialog(mContext, R.style.EasyListDialogStyle);

            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_easy_list_dialog, null);
            mDialog.setContentView(mView);
            mViewHolder = new ViewHolder(mView);

            dm = new DisplayMetrics();
            windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels * 0.95);
//            lp.height = (int) (dm.heightPixels * 0.65);
            lp.gravity = Gravity.BOTTOM;
            lp.y = (int) (dm.widthPixels * 0.025);
            mDialog.getWindow().setAttributes(lp);

            mAdapter = new ListAdapter(mContext);
            mViewHolder.lvListView.setAdapter(mAdapter);
            mViewHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        public void show() {
            if (mDialog != null) {
                mDialog.show();
            }
        }

        public void dismiss() {
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        class ViewHolder {

            TextView tvTitle;
            ListView lvListView;
            TextView tvCancel;
            LinearLayout linearLayout;

            public ViewHolder(View view) {

                tvTitle = view.findViewById(R.id.dialog_title);
                tvCancel = view.findViewById(R.id.dialog_cancel);
                lvListView = view.findViewById(R.id.dialog_list);
                linearLayout = view.findViewById(R.id.dialog_layout);

            }
        }

    }

    public interface OnItemClickListener {
        void OnItemClick(String title, int position);
    }

    private static class ListAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mList;

        public ListAdapter(Context context) {
            mContext = context;
            mList = new ArrayList<>();
        }

        public ListAdapter(Context context, List<String> list) {
            mContext = context;
            mList = list;
        }

        public void setNewData(List<String> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public List<String> getData() {
            return mList;
        }

        public void addData(List<String> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

        public void addData(String list) {
            mList.add(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_easy_list_dialog, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.mView.setText(mList.get(i));
            return view;
        }

        class ViewHolder {
            TextView mView;

            public ViewHolder(View view) {
                mView = view.findViewById(R.id.item_title);
            }
        }

    }

}
