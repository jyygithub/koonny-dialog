package com.jiangyy.easydialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiangyy.easydialog.utils.DialogUtils;

/**
 * Created by JiangYY on 2017/8/5
 *
 * @author JiangYY
 */

public class LoadingDialog {

    public static class Builder {

        private Context mContext;
        private Dialog mDialog;
        private Builder.ViewHolder mViewHolder;
        private Handler mViewUpdateHandler;
        private View mView;

        public Builder(Activity context) {
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

        public Builder setTitle(int resid) {
            mViewHolder.tvTitle.setText(resid);
            return this;
        }

        public Builder setTitle(int resid, int color) {
            mViewHolder.tvTitle.setText(resid);
            mViewHolder.tvTitle.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setCancelable(boolean flag) {
            mDialog.setCancelable(flag);
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean flag) {
            mDialog.setCanceledOnTouchOutside(flag);
            return this;
        }

        public Builder showProgress(boolean flag) {
            mViewHolder.progressNum.setVisibility(flag ? View.VISIBLE : View.GONE);
            return this;
        }

        public void setProgress(int precent) {
            mViewUpdateHandler.sendEmptyMessage(precent);
        }

        public Dialog create() {
            return mDialog;
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

        private void initView() {
            mDialog = new Dialog(mContext, DialogUtils.getStyle());
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_dialog, null);
            mViewHolder = new Builder.ViewHolder(mView);
            mDialog.setContentView(mView);

            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
//            lp.width = (int) (dm.widthPixels * 0.3);
//            lp.height = (int) (dm.widthPixels * 0.3);
            mDialog.getWindow().setAttributes(lp);

            mViewUpdateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    mViewHolder.progressNum.setText(msg.what + "");
                }
            };
            mViewUpdateHandler.sendEmptyMessage(0);
        }


        class ViewHolder {

            TextView tvTitle;
            TextView progressNum;
            ProgressBar progressBar;
            LinearLayout linearLayout;

            public ViewHolder(View view) {
                tvTitle = view.findViewById(R.id.dialog_title);
                progressBar = view.findViewById(R.id.progress_bar);
                progressNum = view.findViewById(R.id.progress_num);
                linearLayout = view.findViewById(R.id.dialog_layout);
            }
        }

    }

}
