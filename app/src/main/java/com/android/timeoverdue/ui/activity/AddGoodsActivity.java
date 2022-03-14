package com.android.timeoverdue.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobClassify;
import com.android.timeoverdue.bean.BmobCompany;
import com.android.timeoverdue.databinding.ActivityAddGoodsBinding;
import com.android.timeoverdue.ui.adapter.ClassifyAdapter;
import com.android.timeoverdue.ui.adapter.CompanyAdapter;
import com.android.timeoverdue.ui.adapter.ScrollPickerAdapter;
import com.android.timeoverdue.ui.picker.PickValueView;
import com.android.timeoverdue.ui.picker.ScrollPickerView;
import com.android.timeoverdue.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddGoodsActivity extends BaseActivity<ActivityAddGoodsBinding> {

    private String goodsName,remarks,number,company,numberAll;
    private List<String> companyList;
    private String[] classifyList = new String[]{};
    private ScrollPickerAdapter.ScrollPickerAdapterBuilder<String> builder1;
    private ScrollPickerAdapter mScrollPickerAdapter;

    @Override
    protected void initData() {
        getCompany();
        getClassifyList();
        //物品名称输入框
        viewBinding.etGoodsName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                goodsName = s.toString();
            }
        });
        //备注输入框
        viewBinding.etRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                remarks = s.toString();
            }
        });
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.tvTitle.setText("新增");
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvSave.setVisibility(View.VISIBLE);
        viewBinding.includeTop.tvSave.setText("保存");

        if (classifyList.length > 0){
            viewBinding.tvClassify.setText(classifyList[0]);
        }
    }

    @Override
    protected void onClick() {
        //返回按钮点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //数量点击事件
        viewBinding.llNumberPicker.setOnClickListener(v->{
            showNumberDialog();
        });
        //分类点击事件
        viewBinding.llClassify.setOnClickListener(v->{
            showClassifyDialog();
        });

    }

    //数量弹窗
    private void showNumberDialog(){
        AlertDialog builder = new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_good_number,null);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final TextView tvSure = view.findViewById(R.id.tv_sure);
        final EditText etNumber = view.findViewById(R.id.et_number);
        final ScrollPickerView scrollPickerView = view.findViewById(R.id.pickerView);
        final LinearLayout llAddCompany = view.findViewById(R.id.ll_add_company);
        builder.setView(view);	//把弹出窗口的页面设定为dialog_exit.xml设定的页面
        tvCancel.setOnClickListener(v->{
            builder.cancel();
        });
        tvSure.setOnClickListener(v->{
            if (!StringUtil.isEmpty(number)){
                numberAll = number+company;
                viewBinding.tvNumber.setText(numberAll);
            }
            builder.dismiss();
        });
        llAddCompany.setOnClickListener(v->{
            Intent intent = new Intent(AddGoodsActivity.this,AddCompanyActivity.class);
            intent.putExtra("CompanyActivity","add");
            startActivity(intent);
            builder.dismiss();
        });
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                number = s.toString();
            }
        });
        scrollPickerView.setLayoutManager(new LinearLayoutManager(mContext));
        builder1 = new ScrollPickerAdapter.ScrollPickerAdapterBuilder<String>(mContext)
                    .setDataList(companyList)
                    .selectedItemOffset(1)
                    .visibleItemNumber(3)
                    .setDivideLineColor("#E5E5E5")
                    .setItemViewProvider(null)
                    .setOnScrolledListener(new ScrollPickerAdapter.OnScrollListener() {
                        @Override
                        public void onScrolled(View currentItemView) {
                            company = (String)currentItemView.getTag();
                        }
                    });
        mScrollPickerAdapter = builder1.build();
        scrollPickerView.setAdapter(mScrollPickerAdapter);
        builder.show();
    }

    //获取单位
    private void getCompany() {
        BmobQuery<BmobCompany> query = new BmobQuery<BmobCompany>();
        query.order("createAt");
        query.findObjects(new FindListener<BmobCompany>() {
            @Override
            public void done(List<BmobCompany> data, BmobException e) {
                if (e == null){
                    companyList = new ArrayList<>();
                    for (BmobCompany str : data){
                        companyList.add(str.getName());
                    }
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    //数量弹窗
    private void showClassifyDialog(){
        AlertDialog builder = new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_classify,null);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final TextView tvSure = view.findViewById(R.id.tv_sure);
        final PickValueView pickValueView = view.findViewById(R.id.pickValueView);
        final LinearLayout llAddClassify = view.findViewById(R.id.ll_add_classify);
        builder.setView(view);	//把弹出窗口的页面设定为dialog_exit.xml设定的页面

        pickValueView.setValueData(classifyList,classifyList[0]);
        tvCancel.setOnClickListener(v->{
            builder.cancel();
        });
        tvSure.setOnClickListener(v->{
            viewBinding.tvClassify.setText(numberAll);
            builder.dismiss();
        });
        llAddClassify.setOnClickListener(v->{
            Intent intent = new Intent(AddGoodsActivity.this,AddClassifyActivity.class);
            intent.putExtra("ClassifyActivity","add");
            startActivity(intent);
            builder.dismiss();
        });

        builder.show();
    }

    //查询分类列表
    private void getClassifyList(){
        BmobQuery<BmobClassify> query = new BmobQuery<BmobClassify>();
        query.order("createAt");
        query.findObjects(new FindListener<BmobClassify>() {
            @Override
            public void done(List<BmobClassify> data, BmobException e) {
                if (e == null){
                    for (int i = 0;i<data.size();i++){
                        classifyList[i] = data.get(i).getName();
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
        getCompany();
        getClassifyList();
    }

}
