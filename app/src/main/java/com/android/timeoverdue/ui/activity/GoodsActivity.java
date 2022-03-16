package com.android.timeoverdue.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobGoods;
import com.android.timeoverdue.databinding.ActivityGoodsBinding;
import com.android.timeoverdue.ui.adapter.GoodsAdapter;
import com.android.timeoverdue.utils.StringUtil;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

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
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //编辑点击事件
        viewBinding.includeTop.tvSave.setOnClickListener(v->{
            Intent intent = new Intent(GoodsActivity.this,AddGoodsActivity.class);
            intent.putExtra("mObjectId",mObjectId);
            intent.putExtra("activity","GoodsActivity");
            startActivity(intent);
        });
    }

    private void getGoodsInfo(){
        BmobQuery<BmobGoods> query = new BmobQuery<>();
        query.getObject(mObjectId, new QueryListener<BmobGoods>() {
            @Override
            public void done(BmobGoods info, BmobException e) {
                if (e == null){
                    viewBinding.tvName.setText(info.getName());
                    if(StringUtil.isEmpty(info.getNumber())){
                        viewBinding.llNumber.setVisibility(View.GONE);
                    }else {
                        viewBinding.llNumber.setVisibility(View.VISIBLE);
                        viewBinding.tvNumberCompany.setText(info.getNumber());
                    }
                    int day = GoodsAdapter.dateDiff(info.getExpirationTime());
                    if (day > 0 || day > 0){
                        viewBinding.tvExplain.setText("保质期还有");
                    }else {
                        viewBinding.tvExplain.setText("已过期");
                    }
                    viewBinding.tvDays.setText(day+"天");
                    if (StringUtil.isEmpty(info.getDateOfManufacture())){
                        viewBinding.llDateOfManufacture.setVisibility(View.GONE);
                        viewBinding.llShelfLifeDays.setVisibility(View.GONE);
                    }else {
                        viewBinding.llDateOfManufacture.setVisibility(View.VISIBLE);
                        viewBinding.llShelfLifeDays.setVisibility(View.VISIBLE);
                        viewBinding.tvDateOfManufacture.setText(info.getDateOfManufacture());
                        viewBinding.tvShelfLifeDays.setText(info.getShelfLifeDays());
                    }
                    if (StringUtil.isEmpty(info.getRemarks())){
                        viewBinding.tvRemarks.setText("无");
                    }else {
                        viewBinding.tvRemarks.setText(info.getRemarks());
                    }

                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

}
