package com.android.timeoverdue.ui.activity;

import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobClassify;
import com.android.timeoverdue.bean.BmobCompany;
import com.android.timeoverdue.databinding.ActivityAddCompanyBinding;
import com.android.timeoverdue.utils.StringUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddCompanyActivity extends BaseActivity<ActivityAddCompanyBinding> {

    private String companyName;
    private String str;//用于区分是新增页面还是编辑页面
    private String name,mObjectId;//name:用于获取从上一个页面得到的名称

    @Override
    protected void initData() {
        str = getIntent().getStringExtra("CompanyActivity");
        name = getIntent().getStringExtra("companyName");
        mObjectId = getIntent().getStringExtra("mObjectId");
        viewBinding.etCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                companyName = s.toString();
                changeSave(StringUtil.isEmpty(companyName));
            }
        });
    }

    @Override
    protected void initView() {
        if (str.equals("add")) {
            viewBinding.includeTop.tvTitle.setText("新建单位");
            viewBinding.ivDel.setVisibility(View.GONE);
            changeSave(true);
        }else {
            viewBinding.includeTop.tvTitle.setText("编辑单位");
            viewBinding.ivDel.setVisibility(View.VISIBLE);
            viewBinding.etCompany.setText(name);
        }
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvSave.setVisibility(View.VISIBLE);
        viewBinding.includeTop.tvSave.setText("保存");
    }

    @Override
    protected void onClick() {
        //返回按钮点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //保存按钮点击事件
        viewBinding.includeTop.tvSave.setOnClickListener(v->{
            equal();
        });
        //删除按钮点击事件
        viewBinding.ivDel.setOnClickListener(v->{
            showDelDialog();
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
     * 查询数据库中是否已存在输入的分类
     */
    private void equal() {
        BmobQuery<BmobCompany> companyBmobQuery = new BmobQuery<>();
        companyBmobQuery.addWhereEqualTo("name", companyName);
        companyBmobQuery.findObjects(new FindListener<BmobCompany>() {
            @Override
            public void done(List<BmobCompany> object, BmobException e) {
                if (e == null) {
                    Log.e(TAG, String.valueOf(object.size()));
                    if (object.size() == 0){
                        if (str.equals("add")) {
                            save();
                        }else {
                            update();
                        }

                    }else {
                        showToast("单位名称已存在！");
                    }
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    /**
     * 新增一个分类
     */
    private void save() {
        BmobCompany bmobCompany = new BmobCompany();
        bmobCompany.setName(companyName);
        bmobCompany.setSystem(false);
        bmobCompany.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    showToast("添加成功!");
                    finish();
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    /**
     * 更新一条数据
     */
    private void update() {
        BmobCompany bmobCompany = new BmobCompany();
        bmobCompany.setName(companyName);
        bmobCompany.update(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showToast("修改成功!");
                    finish();
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    //弹窗提示当前要删除item
    private void showDelDialog(){
        AlertDialog builder = new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_del,null);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final TextView tvDel = view.findViewById(R.id.tv_del);
        final TextView tvDelContent = view.findViewById(R.id.tv_del_content);
        tvDelContent.setText("确定删除“"+name+"”吗？");
        builder.setView(view);	//把弹出窗口的页面设定为dialog_exit.xml设定的页面
        tvCancel.setOnClickListener(v->{
            builder.cancel();
        });
        tvDel.setOnClickListener(v->{
            delete(mObjectId);
            builder.cancel();
        });
        builder.show();
    }


    /**
     * 删除一条数据
     */
    private void delete(String mObjectId) {
        BmobCompany bmobCompany = new BmobCompany();
        bmobCompany.delete(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    showToast("删除成功！");
                    finish();
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

}
