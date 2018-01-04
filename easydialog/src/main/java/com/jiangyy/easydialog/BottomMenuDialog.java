package com.jiangyy.easydialog;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jiangyy.easydialog.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4 0004.
 */

public class BottomMenuDialog {

    public static class Builder {

        private Context mContext;
        private Dialog mDialog;
        private View mView;
        private DisplayMetrics dm;
        private WindowManager windowManager;
        private ViewHolder mViewHolder;
        private MenuAdapter mAdapter;
        private OnItemClickListener mOnItemClickListener;

        public Builder(Context context) {
            mContext = context;
            initView();
        }

        private void initView() {

            mDialog = new Dialog(mContext, DialogUtils.getListStyle());

            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_menu_dialog, null);
            mDialog.setContentView(mView);
            mViewHolder = new ViewHolder(mView);

            dm = new DisplayMetrics();
            windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = dm.widthPixels;
//            lp.height = (int) (dm.heightPixels * 0.65);
            lp.gravity = Gravity.BOTTOM;
//            lp.y = (int) (dm.widthPixels * 0.025);
            mDialog.getWindow().setAttributes(lp);

            mAdapter = new MenuAdapter(mContext);
            mViewHolder.mGridView.setAdapter(mAdapter);

        }

        public Builder setOnItemClickListener(final OnItemClickListener onItemClickListener) {
            mViewHolder.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dismiss();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
            return this;
        }

        public Builder setList(List<MenuItemBean> list) {
            mAdapter.setData(list);
            return this;
        }

        public void show() {
            if (mDialog != null) {
                mDialog.show();
            }
        }

        public void dismiss() {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }

    }

    public static class MenuItemBean {

        @DrawableRes
        private int icon;

        private CharSequence title;

        public MenuItemBean() {
        }

        public MenuItemBean(@DrawableRes int icon, CharSequence title) {
            this.icon = icon;
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public CharSequence getTitle() {
            return title;
        }

        public void setTitle(CharSequence title) {
            this.title = title;
        }
    }

    public interface OnItemClickListener {
        /**
         * Each click event
         *
         * @param position position
         */
        void onItemClick(int position);
    }

    public static class MenuAdapter extends BaseAdapter {

        private Context mContext;
        private List<MenuItemBean> mList;

        public MenuAdapter(Context context) {
            mContext = context;
            mList = new ArrayList<>();
        }

        public void setData(List<MenuItemBean> data) {
            mList = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bottom_menu_dialog, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mView.setText(mList.get(position).title);
            viewHolder.mIcon.setImageResource(mList.get(position).icon);
            return convertView;
        }


        static class ViewHolder {
            TextView mView;
            ImageView mIcon;

            public ViewHolder(View view) {
                mView = view.findViewById(R.id.item_title);
                mIcon = view.findViewById(R.id.item_icon);
            }
        }

    }


    static class ViewHolder {
        GridView mGridView;

        public ViewHolder(View view) {
            mGridView = view.findViewById(R.id.dialog_list);
        }
    }

}
