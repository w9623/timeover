package com.android.timeoverdue.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;


import com.android.timeoverdue.R;
import com.android.timeoverdue.app.Contents;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.databinding.ActivityClipImageBinding;
import com.android.timeoverdue.utils.ActivityUtil;
import com.android.timeoverdue.utils.CommonUtils;
import com.android.timeoverdue.utils.FileManager;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * 头像裁剪Activity
 */
public class ClipImageActivity extends BaseActivity<ActivityClipImageBinding> implements View.OnClickListener {

    private Uri mSaveUri;
    private static final String TAG = "ClipImageActivity";

    public void initView() {
        //设置点击事件监听器
        viewBinding.ivBack.setOnClickListener(this);
        viewBinding.tvCancel.setOnClickListener(this);
        viewBinding.tvOk.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinding.clipViewLayout2.setVisibility(View.VISIBLE);
        viewBinding.clipViewLayout1.setVisibility(View.GONE);
        Log.e("getdata", String.valueOf(getIntent().getData()));
        //设置图片资源
        viewBinding.clipViewLayout2.setImageSrc(getIntent().getData());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_ok:
                generateUriAndReturn();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap;
        zoomedCropBitmap = viewBinding.clipViewLayout2.clip();
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        //File file = FileManager.createFileWithReturnFile(rootPath, "cropped_" + System.currentTimeMillis() + ".jpg");
        File file = FileManager.createFileWithReturnFile(Contents.ROOT_PATH, "ivHead.jpg");
        if (Build.VERSION.SDK_INT >=24) {
            mSaveUri = FileProvider.getUriForFile(mContext, "com.android.timeoverdue.fileProvider", file);
        }else {
            mSaveUri = Uri.fromFile(file);
        }
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            intent.setData(mSaveUri);
            setResult(RESULT_OK, intent);

            intent.putExtra("photo", CommonUtils.changeToString(zoomedCropBitmap));
            ActivityUtil.getInstance().finishActivity(ClipImageActivity.class);


        }
    }

}
