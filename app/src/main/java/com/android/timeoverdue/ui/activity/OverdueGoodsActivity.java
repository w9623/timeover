package com.android.timeoverdue.ui.activity;

import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobGoods;
import com.android.timeoverdue.databinding.ActivityOverdueGoodsBinding;
import com.android.timeoverdue.ui.adapter.GoodsAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class OverdueGoodsActivity extends BaseActivity<ActivityOverdueGoodsBinding> {

    private GoodsAdapter goodsAdapter;
    private List<BmobGoods> data;
    private GoodsAdapter.OnItemClickListener listener = new GoodsAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            Intent intent = new Intent(OverdueGoodsActivity.this, GoodsActivity.class);
            intent.putExtra("mObjectId", data.get(position).getObjectId());
            startActivity(intent);
        }
    };
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        viewBinding.smart.setOnRefreshListener(refreshLayout -> {
            getGoodsList();
        });
        viewBinding.smart.setEnableLoadMore(false);
        getGoodsList();
    }

    private void getGoodsList() {
        BmobQuery<BmobGoods> query = new BmobQuery<>();
        query.order("-createAt");
        query.findObjects(new FindListener<BmobGoods>() {
            @Override
            public void done(List<BmobGoods> list, BmobException e) {
                viewBinding.smart.finishRefresh();
                if (e == null) {
                    data = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (GoodsAdapter.dateDiff("-1", list.get(i).getExpirationTime()) < 0) {
                            data.add(list.get(i));
                        }
                    }
                    if (goodsAdapter == null) {
                        goodsAdapter = new GoodsAdapter(mContext, data);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        goodsAdapter.setOnItemClickListener(listener);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(goodsAdapter);
                    } else {
                        goodsAdapter.setData(data);
                    }
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    @Override
    protected void onClick() {
        viewBinding.ivBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGoodsList();
    }
}
