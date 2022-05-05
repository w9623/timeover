package com.android.timeoverdue.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobGoods;
import com.android.timeoverdue.databinding.ActivityGoodsBinding;
import com.android.timeoverdue.ui.adapter.GoodsAdapter;
import com.android.timeoverdue.utils.CommonUtils;
import com.android.timeoverdue.utils.StringUtil;
import com.bumptech.glide.Glide;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsActivity extends BaseActivity<ActivityGoodsBinding> {

    private String mObjectId;

    @Override
    protected void initData() {
        mObjectId = getIntent().getStringExtra("mObjectId");
        getGoodsInfo();
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.tvTitle.setText("详情");
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvSave.setVisibility(View.VISIBLE);
        viewBinding.includeTop.tvSave.setText("编辑");
    }

    @Override
    protected void onClick() {
        //返回点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v -> {
            finish();
        });
        //编辑点击事件
        viewBinding.includeTop.tvSave.setOnClickListener(v -> {
            Intent intent = new Intent(GoodsActivity.this, AddGoodsActivity.class);
            intent.putExtra("mObjectId", mObjectId);
            intent.putExtra("activity", "GoodsActivity");
            intent.putExtra("editStr", "editStr");
            startActivity(intent);
        });
        //删除点击事件
        viewBinding.ivDel.setOnClickListener(v -> {
            showDelDialog();
        });
    }

    private void getGoodsInfo() {
        BmobQuery<BmobGoods> query = new BmobQuery<>();
        query.getObject(mObjectId, new QueryListener<BmobGoods>() {
            @Override
            public void done(BmobGoods info, BmobException e) {
                if (e == null) {
                    viewBinding.tvName.setText(info.getName());
                    if (StringUtil.isEmpty(info.getNumber())) {
                        viewBinding.llNumber.setVisibility(View.GONE);
                    } else {
                        viewBinding.llNumber.setVisibility(View.VISIBLE);
                        viewBinding.tvNumberCompany.setText(info.getNumber());
                    }
                    int day = GoodsAdapter.dateDiff("-1", info.getExpirationTime());
                    if (day > 0) {
                        viewBinding.tvExplain.setText("保质期还有");
                        viewBinding.tvDays.setText(day + "天");
                    } else {
                        viewBinding.tvExplain.setText("已过期");
                        viewBinding.tvDays.setText(-day + "天");
                    }
                    if (StringUtil.isEmpty(info.getDateOfManufacture())) {
                        viewBinding.llDateOfManufacture.setVisibility(View.GONE);
                        viewBinding.llShelfLifeDays.setVisibility(View.GONE);
                    } else {
                        viewBinding.llDateOfManufacture.setVisibility(View.VISIBLE);
                        viewBinding.llShelfLifeDays.setVisibility(View.VISIBLE);
                        viewBinding.tvDateOfManufacture.setText(info.getDateOfManufacture());
                        viewBinding.tvShelfLifeDays.setText(info.getShelfLifeDays());
                    }
                    if (StringUtil.isEmpty(info.getRemarks())) {
                        viewBinding.tvRemarks.setText("无");
                    } else {
                        viewBinding.tvRemarks.setText(info.getRemarks());
                    }
                    viewBinding.tvExpirationTime.setText(info.getExpirationTime());
                    int day1 = GoodsAdapter.dateDiff(info.getDateOfManufacture(), info.getExpirationTime());
                    viewBinding.tvShelfLifeDays.setText(day1 + "天");
                    if (StringUtil.isEmpty(info.getPhoto())) {
                        viewBinding.llPhoto.setVisibility(View.GONE);
                    } else {
                        viewBinding.llPhoto.setVisibility(View.VISIBLE);
                        Bitmap bitmap = CommonUtils.convertStringToIcon(info.getPhoto());
                        viewBinding.ivPhoto.setImageBitmap(bitmap);
                    }
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    //弹窗提示当前要删除item
    private void showDelDialog() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_del, null);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final TextView tvDel = view.findViewById(R.id.tv_del);
        final TextView tvDelContent = view.findViewById(R.id.tv_del_content);
        tvDelContent.setText("确定删除“" + viewBinding.tvName.getText().toString() + "”吗？");
        builder.setView(view);    //把弹出窗口的页面设定为dialog_exit.xml设定的页面
        tvCancel.setOnClickListener(v -> {
            builder.cancel();
        });
        tvDel.setOnClickListener(v -> {
            delGoods();
            builder.cancel();
        });
        builder.show();
    }

    private void delGoods() {
        BmobGoods bmobGoods = new BmobGoods();
        bmobGoods.setObjectId(mObjectId);
        bmobGoods.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showToast("删除成功！");
                    finish();
                }else {
                    showToast("删除失败！");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGoodsInfo();
    }
}
