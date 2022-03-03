package com.android.timeoverdue.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobClassify;
import com.android.timeoverdue.bean.BmobFeedBack;
import com.android.timeoverdue.databinding.ActivityFeedbackBinding;
import com.android.timeoverdue.utils.StringUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedBackActivity extends BaseActivity<ActivityFeedbackBinding> {

    private String content;//反馈内容
    @Override
    protected void initData() {
        viewBinding.etFeedBack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString();
                changeSave(StringUtil.isEmpty(content));
            }
        });
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.tvTitle.setText("信息反馈");
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvSave.setVisibility(View.VISIBLE);
        viewBinding.includeTop.tvSave.setText("发送");
        changeSave(true);
    }

    @Override
    protected void onClick() {
        //返回按钮点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //常见问题按钮点击事件
        viewBinding.btnCommonProblem.setOnClickListener(v->{

        });
        //发送按钮点击事件
        viewBinding.includeTop.tvSave.setOnClickListener(v->{
            save();
        });
    }

    private void changeSave(boolean isNull){
        if (isNull){
            viewBinding.includeTop.tvSave.setEnabled(false);
            viewBinding.includeTop.tvSave.setClickable(false);
            viewBinding.includeTop.tvSave.setTextColor(getResources().getColor(R.color.color_DCDCDC));
        }else {
            viewBinding.includeTop.tvSave.setEnabled(true);
            viewBinding.includeTop.tvSave.setClickable(true);
            viewBinding.includeTop.tvSave.setTextColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * 新增一条数据
     */
    private void save() {
        BmobFeedBack bmobFeedBack = new BmobFeedBack();
        bmobFeedBack.setContent(content);
        bmobFeedBack.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    showToast("发送成功!");
                    finish();
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }
}
