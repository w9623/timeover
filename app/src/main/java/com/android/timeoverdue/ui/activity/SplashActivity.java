package com.android.timeoverdue.ui.activity;

import android.os.Handler;

import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.databinding.ActivitySplashBinding;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Override
    protected void initView() {
        //申请麦克风权限
        XXPermissions.with(this)
                .permission(Permission.RECORD_AUDIO)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (!all) {
                            showToast("权限未授予，部分功能不可使用！");
                        }
                        goToMainActivity();
                    }
                });

    }

    //延迟2秒跳转至登录页
    private void goToMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jumpToActivity(LoginOrRegisterActivity.class);
                finish();
            }
        }, 2000);
    }

}
