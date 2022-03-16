package com.android.timeoverdue.ui.activity;

import static com.wheelpicker.PickOption.getPickDefaultOptionBuilder;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import com.android.timeoverdue.bean.BmobGoods;
import com.android.timeoverdue.bean.BmobTimeReminder;
import com.android.timeoverdue.databinding.ActivityAddGoodsBinding;
import com.android.timeoverdue.ui.adapter.ClassifyAdapter;
import com.android.timeoverdue.ui.adapter.CompanyAdapter;
import com.android.timeoverdue.ui.adapter.ScrollPickerAdapter;
//import com.android.timeoverdue.ui.picker.PickValueView;
import com.android.timeoverdue.ui.picker.ScrollPickerView;
import com.android.timeoverdue.utils.StringUtil;
import com.wheelpicker.DataPicker;
import com.wheelpicker.DateTimePicker;
import com.wheelpicker.IDateTimePicker;
import com.wheelpicker.OnDatePickListener;
import com.wheelpicker.PickMode;
import com.wheelpicker.PickOption;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.addapp.pickers.picker.DatePicker;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import top.defaults.view.PickerView;

public class AddGoodsActivity extends BaseActivity<ActivityAddGoodsBinding> {

    private String goodsName,remarks,number,company,numberAll,classify,mObjectId;
    private List<String> companyList,classifyList;
    private ScrollPickerAdapter.ScrollPickerAdapterBuilder<String> builder1;
    private ScrollPickerAdapter mScrollPickerAdapter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

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
        //生产日期
        viewBinding.llDateOfManufacture.setOnClickListener(v->{
            final DatePicker picker = new DatePicker(this);
            picker.setTopPadding(15);
            picker.setRangeStart(2000, 8, 29);
            picker.setRangeEnd(2111, 1, 11);
            picker.setSelectedItem(2022, 3, 15);
            picker.setWeightEnable(true);
            picker.setLineColor(Color.BLACK);
            picker.setTitleText("生产日期");
            picker.setTextSize(15);
            picker.setGravity(Gravity.BOTTOM);
            picker.setLabel(null,null,null);
            picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String year, String month, String day) {
                    viewBinding.tvDateOfManufacture.setText(year+"-"+month+"-"+day);
                }
            });
            picker.setOnWheelListener(new DatePicker.OnWheelListener() {
                @Override
                public void onYearWheeled(int index, String year) {
//                    picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
                }

                @Override
                public void onMonthWheeled(int index, String month) {
//                    picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
                }

                @Override
                public void onDayWheeled(int index, String day) {
//                    picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
                }
            });
            picker.show();
        });
        //过期日期和保质天数点击事件
        viewBinding.llDays.setOnClickListener(v->{
            if (viewBinding.tvDays.getText().equals("过期日期")){
                final DatePicker picker = new DatePicker(this);
                picker.setTopPadding(15);
                picker.setRangeStart(2000, 8, 29);
                picker.setRangeEnd(2111, 1, 11);
                picker.setSelectedItem(2022, 3, 15);
                picker.setWeightEnable(true);
                picker.setLineColor(Color.BLACK);
                picker.setTitleText("过期日期");
                picker.setTextSize(15);
                picker.setGravity(Gravity.BOTTOM);
                picker.setLabel(null,null,null);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        viewBinding.tvDisplayDays.setText(year+"-"+month+"-"+day);
                    }
                });
                picker.setOnWheelListener(new DatePicker.OnWheelListener() {
                    @Override
                    public void onYearWheeled(int index, String year) {
//                    picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
                    }

                    @Override
                    public void onMonthWheeled(int index, String month) {
//                    picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
                    }

                    @Override
                    public void onDayWheeled(int index, String day) {
//                    picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
                    }
                });
                picker.show();
            }
        });
        //过期提醒点击事件
        viewBinding.llRemind.setOnClickListener(v->{
            Intent intent = new Intent(AddGoodsActivity.this,EditTimeReminderActivity.class);
            intent.putExtra("mObjectId",mObjectId);
            intent.putExtra("classifyName",viewBinding.tvClassify.getText().toString());
            intent.putExtra("reminderTime",viewBinding.tvRemind.getText().toString());
            intent.putExtra("activity","AddGoodsActivity");
            startActivity(intent);
        });
        //保存按钮点击事件
        viewBinding.includeTop.tvSave.setOnClickListener(v->{
            BmobGoods bmobGoods = new BmobGoods();
            if (StringUtil.isEmpty(viewBinding.etGoodsName.getText().toString())){
                showToast("名称不能为空！");
                return;
            }else {
                bmobGoods.setName(viewBinding.etGoodsName.getText().toString());
            }
            if (!StringUtil.isEmpty(viewBinding.tvNumber.getText().toString())){
                bmobGoods.setNumber(viewBinding.tvNumber.getText().toString());
            }
            if (!StringUtil.isEmpty(viewBinding.etRemarks.getText().toString())){
                bmobGoods.setRemarks(viewBinding.etRemarks.getText().toString());
            }
            if (StringUtil.isEmpty(viewBinding.tvDateOfManufacture.getText().toString())){
                bmobGoods.setDateOfManufacture(String.valueOf(System.currentTimeMillis()));
            }else {
                bmobGoods.setDateOfManufacture(viewBinding.tvDateOfManufacture.getText().toString());

            }
            if (viewBinding.tvDays.getText().equals("保质天数") && StringUtil.isEmpty(viewBinding.tvDisplayDays.getText().toString())){
                showToast("保质天数不能为空！");
                return;
            }else {
                bmobGoods.setShelfLifeDays(viewBinding.tvDisplayDays.getText().toString());
            }
            bmobGoods.setClassify(viewBinding.tvClassify.getText().toString());
            if (viewBinding.tvDays.getText().equals("过期日期") && StringUtil.isEmpty(viewBinding.tvDisplayDays.getText().toString())){
                showToast("过期日期不能为空！");
                return;
            }else {
                bmobGoods.setExpirationTime(viewBinding.tvDisplayDays.getText().toString());
            }
            bmobGoods.setTimeReminder(viewBinding.tvRemind.getText().toString());
            bmobGoods.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        showToast("保存成功!");
                        finish();
                    }else {
                        showToast("保存失败!");
                        Log.e(TAG,e.toString());
                    }
                }
            });
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
        ScrollPickerView pickerView = view.findViewById(R.id.pickerView);
        final LinearLayout llAddClassify = view.findViewById(R.id.ll_add_classify);
        builder.setView(view);	//把弹出窗口的页面设定为dialog_exit.xml设定的页面


        tvCancel.setOnClickListener(v->{
            builder.cancel();
        });
        tvSure.setOnClickListener(v->{
            viewBinding.tvClassify.setText(classify);
            getClassifyReminderTime();
            builder.dismiss();
        });
        llAddClassify.setOnClickListener(v->{
            Intent intent = new Intent(AddGoodsActivity.this,AddClassifyActivity.class);
            intent.putExtra("ClassifyActivity","add");
            startActivity(intent);
            builder.dismiss();
        });
        pickerView.setLayoutManager(new LinearLayoutManager(mContext));
        builder1 = new ScrollPickerAdapter.ScrollPickerAdapterBuilder<String>(mContext)
                .setDataList(classifyList)
                .selectedItemOffset(1)
                .visibleItemNumber(3)
                .setDivideLineColor("#E5E5E5")
                .setItemViewProvider(null)
                .setOnScrolledListener(new ScrollPickerAdapter.OnScrollListener() {
                    @Override
                    public void onScrolled(View currentItemView) {
                        classify = (String)currentItemView.getTag();
                    }
                });
        mScrollPickerAdapter = builder1.build();
        pickerView.setAdapter(mScrollPickerAdapter);
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
                    classifyList = new ArrayList<>();
                    for (BmobClassify str : data){
                        classifyList.add(str.getName());
                    }
                    viewBinding.tvClassify.setText(data.get(0).getName());
                    getClassifyReminderTime();
                }else {
                    Log.e(TAG,e.toString());
                }

            }
        });
    }

    //获取分类对应的提醒时间
    private void getClassifyReminderTime(){
        BmobQuery<BmobTimeReminder> query = new BmobQuery<BmobTimeReminder>();
        query.addWhereEqualTo("classifyName",viewBinding.tvClassify.getText().toString());
        query.findObjects(new FindListener<BmobTimeReminder>() {
            @Override
            public void done(List<BmobTimeReminder> list, BmobException e) {
                if (e == null){
                    if (list.get(0).getReminderDays().equals("W2IJKKKU")){
                        getReminderTime("W2IJKKKU");
                    }else {
                        viewBinding.tvRemind.setText(list.get(0).getReminderDays());
                    }
                    mObjectId = list.get(0).getObjectId();
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    //获取默认的提醒时间
    private void getReminderTime(String id){
        BmobQuery<BmobTimeReminder> query = new BmobQuery<BmobTimeReminder>();
        query.addWhereEqualTo("objectId",id);
        query.findObjects(new FindListener<BmobTimeReminder>() {
            @Override
            public void done(List<BmobTimeReminder> list, BmobException e) {
                if (e == null ){
                    viewBinding.tvRemind.setText(list.get(0).getReminderDays());
                }else {
                    Log.e("TimeReminder",e.toString());
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
