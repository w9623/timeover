package com.android.timeoverdue.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.UserBean;
import com.android.timeoverdue.databinding.ActivityLoginOrRegisterBinding;
import com.android.timeoverdue.utils.StringUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginOrRegisterActivity extends BaseActivity<ActivityLoginOrRegisterBinding> {

    private String userName,password;

    @Override
    protected void initView() {
        if (BmobUser.isLogin()){
            jumpToActivity(MainActivity.class);
        }else {
            //默认进入该页面为登录页面
            changePage(true);
        }
    }

    @Override
    protected void initData() {
        //账号输入框的监听事件
        viewBinding.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userName = s.toString();
                changeBtn(userName,password);
            }
        });
        //密码输入框的监听事件
        viewBinding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
                changeBtn(userName,password);
            }
        });

    }

    @Override
    protected void onClick() {
        //切换登录注册的按钮
        viewBinding.tvRegister.setOnClickListener(v->{
            if (viewBinding.tvRegister.getText().equals("立即注册")) {
                changePage(false);
            }else {
                changePage(true);
            }
        });
        //登录、注册按钮
        viewBinding.btnLogin.setOnClickListener(v->{
            if (viewBinding.tvTitle.getText().equals("账号登录")){
                login();
            }else {
                signUp();
            }
        });
        //忘记密码按钮
        viewBinding.tvForgetPassword.setOnClickListener(v->{

        });
    }

    //用于切换登录注册页面，默认进入该页面是登录页面
    private void changePage(Boolean isLogin) {
        if (isLogin) {
            viewBinding.tvTitle.setText("账号登录");
            viewBinding.tvForgetPassword.setVisibility(View.VISIBLE);
            viewBinding.tvRegister.setText("立即注册");
            viewBinding.btnLogin.setText("登录");
        }else {
            viewBinding.tvTitle.setText("账号注册");
            viewBinding.tvForgetPassword.setVisibility(View.GONE);
            viewBinding.tvRegister.setText("立即登录");
            viewBinding.btnLogin.setText("注册");
        }
        viewBinding.etUsername.setText("");
        viewBinding.etPassword.setText("");
    }

    //改变登录注册按钮的状态
    private void changeBtn(String userName,String password) {
        if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(password)) {
            viewBinding.btnLogin.setBackground(getResources().getDrawable(R.drawable.btn_login));
            viewBinding.btnLogin.setEnabled(true);
            viewBinding.btnLogin.setClickable(true);
        }else {
            viewBinding.btnLogin.setBackground(getResources().getDrawable(R.drawable.btn_un_login));
            viewBinding.btnLogin.setEnabled(false);
            viewBinding.btnLogin.setClickable(false);
        }
    }

    /**
     * 账号密码注册
     */
    private void signUp() {
        final UserBean user = new UserBean();
        user.setUsername(userName);
        user.setPassword(password);
        user.signUp(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean user, BmobException e) {
                if (e == null) {
                    showToast("注册成功！");
                } else {
                    showToast("注册失败！");
                }
            }
        });
    }

    /**
     * 账号密码登录
     */
    private void login() {
        final UserBean user = new UserBean();
        //此处替换为你的用户名
        user.setUsername(userName);
        //此处替换为你的密码
        user.setPassword(password);
        user.login(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean bmobUser, BmobException e) {
                if (e == null) {
                    UserBean user = BmobUser.getCurrentUser(UserBean.class);
                    showToast("登录成功！");
                    jumpToActivity(MainActivity.class);
                    finish();
                } else {
                    showToast("登录失败！");
                }
            }
        });
    }

}