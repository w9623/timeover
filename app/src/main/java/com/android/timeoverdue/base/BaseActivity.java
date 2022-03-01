package com.android.timeoverdue.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.android.timeoverdue.R;
import com.android.timeoverdue.utils.ActivityUtil;
import com.android.timeoverdue.utils.StatusBarUtil;
import com.android.timeoverdue.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {


    public T viewBinding;
    /** 白黑沉浸状态栏，true：黑字，false：白字 **/
    private boolean isSetStatusBar = true;
    /** 是否允许全屏 **/
    private boolean mAllowFullScreen = false;

    //获取TAG的activity名称
    protected final String TAG = this.getClass().getSimpleName();
    //获取上下文
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class);
            viewBinding = (T) inflate.invoke(null, getLayoutInflater());
            setContentView(viewBinding.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException| InvocationTargetException e) {
            e.printStackTrace();
        }

        mContext = this;
        //Activity管理
        ActivityUtil.getInstance().addActivity(this);
        if (isSetStatusBar) {
            steepStatusBar(isSetStatusBar);
        }
        //设置数据
        initData();
        //初始化布局
        initView();
        //点击事件响应
        onClick();
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar(Boolean isSetStatusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 透明状态栏
//            getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明导航栏
//            getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (isSetStatusBar){
                StatusBarUtil.setStatusBarMode(this,true, R.color.white);
            }else {
                StatusBarUtil.setStatusBarMode(this,false,R.color.black);
            }

        }
    }

    /**
     * 初始化view
     */
    protected void initView() {

    }

    /**
     * 设置数据
     */
    protected void initData() {

    }

    /**
     * 点击事件
     */
    protected void onClick() {

    }

    /**
     * show shotToast
     *
     * @param msg 提示内容
     */
    public void showToast(String msg) {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 普通跳转
     *
     * @param activity activity
     */
    protected void jumpToActivity(Class<?> activity) {
        startActivity(new Intent(mContext, activity));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.getInstance().removeActivity(this);
    }

}
