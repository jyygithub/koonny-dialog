package com.jiangyy.easydialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JYY on 2017/8/5.
 */

public class MultipleChoiceDialog {

    public static class Builder {

        private Context mContext;
        private Dialog mDialog;
        private View mView;
        private Builder.ViewHolder mViewHolder;
        private ListAdapter mAdapter;
        private List<String> mChoiceStringList;
        private List<Integer> mChoicePositionList;
        private DisplayMetrics dm;
        private WindowManager windowManager;
        private int mMaxCount = -1;
        private ExceedsListener mExceedsListener;

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

        public Builder setChoiceIcon(@DrawableRes int resid) {
            mAdapter.setChoiceIcon(resid);
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

        public Builder setMaxChoice(int count) {
            mMaxCount = count;
            return this;
        }

        public Builder setExceedsListener(ExceedsListener listener) {
            mExceedsListener = listener;
            return this;
        }

        public Builder addListener(final ClickListener onItemClickListener) {
            mViewHolder.lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mMaxCount == -1 || mAdapter.getBooleanList().get(i)) {
                        mAdapter.setData(i, !mAdapter.getBooleanList().get(i));
                    } else {
                        int c = 0;
                        for (Boolean aBoolean : mAdapter.getBooleanList()) {
                            if (aBoolean) {
                                c++;
                            }
                        }
                        if (c == mMaxCount) {
                            if (mExceedsListener != null) {
                                mExceedsListener.show();
                            } else {
                                Toast.makeText(mContext, "选择数量超过限制", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mAdapter.setData(i, !mAdapter.getBooleanList().get(i));
                        }
                    }
                }
            });
            mViewHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (onItemClickListener != null) {
                        mChoiceStringList = new ArrayList<>();
                        mChoicePositionList = new ArrayList<>();
                        for (int index = 0; index < mAdapter.getCount(); index++) {
                            if (mAdapter.getBooleanList().get(index)) {
                                mChoiceStringList.add(mAdapter.getItem(index).toString());
                                mChoicePositionList.add(index);
                            }
                        }
                        if (mChoiceStringList != null && mChoiceStringList.size() > 0) {
                            onItemClickListener.OnFinishClick(mChoiceStringList, mChoicePositionList);
                        }
                    }
                }
            });
            return this;
        }

        private void initView() {

            mDialog = new Dialog(mContext, R.style.EasyListDialogStyle);

            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_easy_list_dialog, null);
            mDialog.setContentView(mView);
            mViewHolder = new Builder.ViewHolder(mView);

            dm = new DisplayMetrics();
            windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels * 0.95);
            lp.gravity = Gravity.BOTTOM;
            lp.y = (int) (dm.widthPixels * 0.025);
            mDialog.getWindow().setAttributes(lp);

            mAdapter = new ListAdapter(mContext);
            mViewHolder.lvListView.setAdapter(mAdapter);

            mViewHolder.tvCancel.setText("完成");

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

    public interface ClickListener {
        void OnFinishClick(List<String> data, List<Integer> data0);
    }

    public interface ExceedsListener {
        void show();
    }

    private static class ListAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mList;
        private List<Boolean> mBooleanList;
        private int mChoiceIcon = R.drawable.ic_yes;

        public ListAdapter(Context context) {
            mContext = context;
            mList = new ArrayList<>();
            mBooleanList = new ArrayList<>();
        }

        public ListAdapter(Context context, List<String> list) {
            mContext = context;
            mList = list;
            mBooleanList = new ArrayList<>();
        }

        public void setChoiceIcon(int resid) {
            mChoiceIcon = resid;
            this.notifyDataSetChanged();
        }

        public void setNewData(List<String> list) {
            mList = list;
            mBooleanList.clear();
            for (int index = 0; index < list.size(); index++) {
                mBooleanList.add(false);
            }
            notifyDataSetChanged();
        }

        public void setData(int position, boolean flag) {
            mBooleanList.set(position, flag);
            notifyDataSetChanged();
        }

        public List<String> getData() {
            return mList;
        }

        public List<Boolean> getBooleanList() {
            return mBooleanList;
        }

        public void addData(List<String> list) {
            mList.addAll(list);
            for (int index = 0; index < list.size(); index++) {
                mBooleanList.add(false);
            }
            notifyDataSetChanged();
        }

        public void addData(String list) {
            mList.add(list);
            mBooleanList.add(false);
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
            ListAdapter.ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_multiple_choice_dialog, viewGroup, false);
                viewHolder = new ListAdapter.ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ListAdapter.ViewHolder) view.getTag();
            }
            viewHolder.mView.setText(mList.get(i));
            viewHolder.tvChoice.setBackgroundResource(mChoiceIcon);
            if (mBooleanList.get(i)) {
                viewHolder.tvChoice.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvChoice.setVisibility(View.GONE);
            }
            return view;
        }

        class ViewHolder {
            TextView mView;
            TextView tvChoice;

            public ViewHolder(View view) {
                mView = view.findViewById(R.id.item_title);
                tvChoice = view.findViewById(R.id.item_choice);
            }
        }

    }


}
