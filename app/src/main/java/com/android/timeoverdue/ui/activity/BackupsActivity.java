package com.android.timeoverdue.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.app.Contents;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.UserBean;
import com.android.timeoverdue.databinding.ActivityBackupsBinding;
import com.android.timeoverdue.utils.ZSPTool;

import cn.bmob.v3.BmobUser;

public class BackupsActivity extends BaseActivity<ActivityBackupsBinding> {

    private UserBean userBean;

    @Override
    protected void initData() {
        userBean = BmobUser.getCurrentUser(UserBean.class);
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvSave.setVisibility(View.GONE);
        viewBinding.includeTop.tvTitle.setText("数据备份");
        //显示账号
        viewBinding.tvUsername.setText("当前账号："+ userBean.getUsername());
    }

    @Override
    protected void onClick() {
        //返回按钮
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //退出登录按钮
        viewBinding.btnExitUser.setOnClickListener(v->{
            showDialog();
        });
    }

    //弹窗提示当前要退出账号
    private void showDialog(){
        AlertDialog builder = new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_exit,null);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final TextView tvExit = view.findViewById(R.id.tv_exit);
        builder.setView(view);	//把弹出窗口的页面设定为dialog_exit.xml设定的页面
        tvCancel.setOnClickListener(v->{
            builder.cancel();
        });
        tvExit.setOnClickListener(v->{
            BmobUser.logOut();
            ZSPTool.remove(Contents.USER_ID);
            finish();
        });
        builder.show();
    }
}
