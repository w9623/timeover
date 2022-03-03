package com.android.timeoverdue.ui.activity;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobCommonProblem;
import com.android.timeoverdue.databinding.ActivityCommonProblemBinding;
import com.android.timeoverdue.ui.adapter.CommonProblemAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CommonProblemActivity extends BaseActivity<ActivityCommonProblemBinding> {

    private CommonProblemAdapter commonProblemAdapter;

    @Override
    protected void initData() {
        getCommonProblemList();
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.tvSave.setVisibility(View.GONE);
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvTitle.setText("常见问题");
    }

    @Override
    protected void onClick() {
        //返回按钮点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
    }

    private void getCommonProblemList(){
        BmobQuery<BmobCommonProblem> query = new BmobQuery<BmobCommonProblem>();
        query.order("createAt");
        query.findObjects(new FindListener<BmobCommonProblem>() {
            @Override
            public void done(List<BmobCommonProblem> data, BmobException e) {
                if (e == null){
                    if (commonProblemAdapter == null){
                        commonProblemAdapter = new CommonProblemAdapter(mContext,data);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(commonProblemAdapter);
                    }
                }else {
                    Log.e(TAG,e.toString());
                }

            }
        });
    }
}
