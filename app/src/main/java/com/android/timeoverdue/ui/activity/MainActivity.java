package com.android.timeoverdue.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;

import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobGoods;
import com.android.timeoverdue.databinding.ActivityMainBinding;
import com.android.timeoverdue.ui.adapter.GoodsAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private GoodsAdapter goodsAdapter;
    private List<BmobGoods> data;
    private GoodsAdapter.OnItemClickListener listener = new GoodsAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {

        }
    };

    @Override
    protected void initData() {
        getGoodsList();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onClick() {
        //设置按钮点击事件
        viewBinding.ivSet.setOnClickListener(v->{
            jumpToActivity(SettingActivity.class);
        });
        //添加物品按钮点击事件
        viewBinding.ivAdd.setOnClickListener(v->{
            jumpToActivity(AddGoodsActivity.class);
        });
    }

    private void getGoodsList(){
        BmobQuery<BmobGoods> query = new BmobQuery<>();
        query.order("-createAt");
        query.findObjects(new FindListener<BmobGoods>() {
            @Override
            public void done(List<BmobGoods> list, BmobException e) {
                if (e == null){
                    data = list;
                    if (goodsAdapter == null){
                        goodsAdapter = new GoodsAdapter(mContext,list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        goodsAdapter.setOnItemClickListener(listener);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(goodsAdapter);
                    }else {
                        goodsAdapter.setData(data);
                    }
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGoodsList();
    }
}