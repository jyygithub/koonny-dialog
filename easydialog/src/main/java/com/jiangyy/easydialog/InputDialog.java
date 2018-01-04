package com.jiangyy.easydialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiangyy.easydialog.utils.DialogUtils;

/**
 * Created by JiangYY on 2017/8/5
 *
 * @author JiangYY
 */

public class InputDialog {

    public static class Builder {

        private Context mContext;
        private Dialog mDialog;
        private Builder.ViewHolder mViewHolder;

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

        public Builder setMessage(CharSequence title) {
            mViewHolder.etMessage.setText(title);
            return this;
        }

        public Builder setMessage(CharSequence title, int color) {
            mViewHolder.etMessage.setText(title);
            mViewHolder.etMessage.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setMessage(int resid) {
            mViewHolder.etMessage.setText(resid);
            return this;
        }

        public Builder setMessage(int resid, int color) {
            mViewHolder.etMessage.setText(resid);
            mViewHolder.etMessage.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setHint(CharSequence title) {
            mViewHolder.etMessage.setHint(title);
            return this;
        }

        public Builder setHint(CharSequence title, int color) {
            mViewHolder.etMessage.setHint(title);
            mViewHolder.etMessage.setHintTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setHint(int resid) {
            mViewHolder.etMessage.setHint(resid);
            return this;
        }

        public Builder setHint(int resid, int color) {
            mViewHolder.etMessage.setHint(resid);
            mViewHolder.etMessage.setHintTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setInputType(int type) {
            mViewHolder.etMessage.setInputType(type);
            return this;
        }

        public Builder setLines(int lines) {
            mViewHolder.etMessage.setLines(lines);
            return this;
        }

        public Builder setMaxLines(int lines) {
            mViewHolder.etMessage.setMaxLines(lines);
            return this;
        }

        public Builder setMaxEms(int maxEms) {
            mViewHolder.etMessage.setMaxEms(maxEms);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
            mViewHolder.tvPositiveButton.setVisibility(View.VISIBLE);
            mViewHolder.tvPositiveButton.setText(text);
            mViewHolder.tvPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        view.setTag(mViewHolder.etMessage.getText().toString().trim());
                        listener.onClick(view);
                    }
                }
            });
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener, int color) {
            mViewHolder.tvPositiveButton.setVisibility(View.VISIBLE);
            mViewHolder.tvPositiveButton.setText(text);
            mViewHolder.tvPositiveButton.setTextColor(ContextCompat.getColor(mContext, color));
            mViewHolder.tvPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        view.setTag(mViewHolder.etMessage.getText().toString().trim());
                        listener.onClick(view);
                    }
                }
            });
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
            mViewHolder.tvNegativeButton.setVisibility(View.VISIBLE);
            mViewHolder.tvNegativeButton.setText(text);
            mViewHolder.tvNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener, int color) {
            mViewHolder.tvNegativeButton.setVisibility(View.VISIBLE);
            mViewHolder.tvNegativeButton.setText(text);
            mViewHolder.tvNegativeButton.setTextColor(ContextCompat.getColor(mContext, color));
            mViewHolder.tvNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
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
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_input_dialog, null);
            mViewHolder = new Builder.ViewHolder(mView);
            mDialog.setContentView(mView);

            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels * 0.8);
            mDialog.getWindow().setAttributes(lp);
        }


        class ViewHolder {

            TextView tvTitle;
            EditText etMessage;
            TextView tvPositiveButton, tvNegativeButton;
            ConstraintLayout vgLayout;

            public ViewHolder(View view) {

                tvTitle = view.findViewById(R.id.dialog_title);
                etMessage = view.findViewById(R.id.dialog_message);
                tvPositiveButton = view.findViewById(R.id.dialog_positive);
                tvNegativeButton = view.findViewById(R.id.dialog_negative);
                vgLayout = view.findViewById(R.id.dialog_layout);
            }
        }

    }

}
