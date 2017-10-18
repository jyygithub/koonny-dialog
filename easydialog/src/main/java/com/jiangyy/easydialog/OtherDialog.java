package com.jiangyy.easydialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangyy.easydialog.utils.DialogUtils;

/**
 * Created by JiangYY on 2017/8/10
 *
 * @author JiangYY
 */

public class OtherDialog {

    public static class Builder {

        private Context mContext;
        private Dialog mDialog;
        private View mView;
        private LayoutInflater mInflater;

        private WindowManager.LayoutParams mParams;
        private DisplayMetrics mDisplayMetrics;
        private ImageLoader mImageLoader;

        public Builder(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            initView();
        }

        private void initView() {

            mDialog = new Dialog(mContext, DialogUtils.getStyle());

            mDisplayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
            mParams = mDialog.getWindow().getAttributes();
            mParams.width = (int) (mDisplayMetrics.widthPixels * 0.8);
            mDialog.getWindow().setAttributes(mParams);
        }

        public Builder setContentView(int resource) {
            mView = mInflater.inflate(resource, null);
            mDialog.setContentView(mView);
            return this;
        }

        public Builder setText(@IdRes int id, String message) {
            TextView textView = mView.findViewById(id);
            textView.setText(message);
            return this;
        }

        public Builder setImageResource(@IdRes int id, int resid) {
            ImageView imageView = mView.findViewById(id);
            imageView.setImageResource(resid);
            return this;
        }

        public Builder setImageBitmap(@IdRes int id, Bitmap bitmap) {
            ImageView imageView = mView.findViewById(id);
            imageView.setImageBitmap(bitmap);
            return this;
        }

        public Builder setLoadImageType(ImageLoader imageLoader) {
            mImageLoader = imageLoader;
            return this;
        }

        public Builder setImageResource(@IdRes int id, String url) {
            ImageView imageView = mView.findViewById(id);
            if (mImageLoader != null) {
                mImageLoader.display(mContext, imageView, url);
            }
            return this;
        }

        public Builder setOnClickListener(@IdRes int id, final View.OnClickListener listener) {
            mView.findViewById(id).setOnClickListener(new View.OnClickListener() {
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

        public Builder setDismissButton(@IdRes int id) {
            mView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            return this;
        }

        public Builder setAdapter(@IdRes int id, Adapter adapter) {
            AdapterView view = mView.findViewById(id);
            view.setAdapter(adapter);
            return this;
        }

        public Builder setOnItemClickListener(@IdRes int id, final AdapterView.OnItemClickListener listener) {
            AdapterView view = mView.findViewById(id);
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dismiss();
                    if (listener != null) {
                        listener.onItemClick(adapterView, view, i, l);
                    }
                }
            });
            return this;
        }

        public Builder setOnClickListener(@IdRes int id, String message, final View.OnClickListener listener) {
            TextView textView = mView.findViewById(id);
            textView.setText(message);
            textView.setOnClickListener(new View.OnClickListener() {
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

        public Builder bundleInputListener(@IdRes int editTextId, @IdRes int clickViewId, final InputListener listener) {
            final EditText editText = mView.findViewById(editTextId);
            mView.findViewById(clickViewId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        listener.onClick(view, editText.getText().toString());
                    }
                }
            });
            return this;
        }

        public Builder setWidth(float width) {
            mParams.width = (int) (mDisplayMetrics.widthPixels * width);
            mDialog.getWindow().setAttributes(mParams);
            return this;
        }

        public Builder setHeight(float height) {
            mParams.height = (int) (mDisplayMetrics.heightPixels * height);
            mDialog.getWindow().setAttributes(mParams);
            return this;
        }

        public Builder setGravity(int gravity) {
            mDialog.getWindow().setGravity(gravity);
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

        public View getView() {
            return mView;
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

    }

    public interface InputListener {
        /**
         * 输入框输入完成后的点击事件
         *
         * @param view    点击的按钮View
         * @param message 获取到的输入内容
         */
        void onClick(View view, String message);
    }

    public interface ImageLoader {
        /**
         * 用于显示网络图片
         *
         * @param context   Context
         * @param imageView ImageView
         * @param url       图片地址
         */
        void display(Context context, ImageView imageView, String url);
    }

}
