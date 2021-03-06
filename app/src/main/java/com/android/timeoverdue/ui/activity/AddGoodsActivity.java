package com.android.timeoverdue.ui.activity;

import static com.android.timeoverdue.utils.FileUtil.getRealFilePathFromUri;
import static com.wheelpicker.PickOption.getPickDefaultOptionBuilder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.timeoverdue.R;
import com.android.timeoverdue.app.Contents;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobClassify;
import com.android.timeoverdue.bean.BmobCompany;
import com.android.timeoverdue.bean.BmobGoods;
import com.android.timeoverdue.bean.BmobTimeReminder;
import com.android.timeoverdue.databinding.ActivityAddGoodsBinding;
import com.android.timeoverdue.ui.adapter.ClassifyAdapter;
import com.android.timeoverdue.ui.adapter.CompanyAdapter;
import com.android.timeoverdue.ui.adapter.GoodsAdapter;
import com.android.timeoverdue.ui.adapter.ScrollPickerAdapter;
//import com.android.timeoverdue.ui.picker.PickValueView;
import com.android.timeoverdue.ui.picker.ScrollPickerView;
import com.android.timeoverdue.utils.CommonUtils;
import com.android.timeoverdue.utils.FileManager;
import com.android.timeoverdue.utils.StringUtil;
import com.android.timeoverdue.utils.ZSPTool;
import com.bumptech.glide.Glide;
import com.wheelpicker.DataPicker;
import com.wheelpicker.DateTimePicker;
import com.wheelpicker.IDateTimePicker;
import com.wheelpicker.OnDatePickListener;
import com.wheelpicker.PickMode;
import com.wheelpicker.PickOption;

import java.io.File;
import java.io.FileNotFoundException;
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
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import top.defaults.view.PickerView;

public class AddGoodsActivity extends BaseActivity<ActivityAddGoodsBinding> {

    private String goodsName,remarks,number,company,numberAll,classify,mObjectId,activity,editStr,photoPath = "",mClassObjectId;
    private List<String> companyList,classifyList;
    private ScrollPickerAdapter.ScrollPickerAdapterBuilder<String> builder1;
    private ScrollPickerAdapter mScrollPickerAdapter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
    private int type;//???????????????????????????????????????????????????????????????0?????????1??????
    //?????????????????????????????????
    private File tempFile;
    //????????????
    private static final int REQUEST_CAPTURE = 100;
    //????????????
    private static final int REQUEST_PICK = 101;
    //????????????
    private static final int REQUEST_CROP_PHOTO = 102;

    @Override
    protected void initData() {
        getCompany();
        getClassifyList();
        mObjectId = getIntent().getStringExtra("mObjectId");
        activity = getIntent().getStringExtra("activity");
        editStr = getIntent().getStringExtra("editStr");
        if (!StringUtil.isEmpty(activity) && activity.equals("GoodsActivity")){
            getGoodsInfo();
        }
        //?????????????????????
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
        //???????????????
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
        if (editStr.equals("editStr")) {
            viewBinding.includeTop.tvTitle.setText("??????");
        }else {
            viewBinding.includeTop.tvTitle.setText("??????");
        }
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvSave.setVisibility(View.VISIBLE);
        viewBinding.includeTop.tvSave.setText("??????");
    }

    @Override
    protected void onClick() {
        //????????????????????????
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //??????????????????
        viewBinding.llNumberPicker.setOnClickListener(v->{
            showNumberDialog();
        });
        //??????????????????
        viewBinding.llClassify.setOnClickListener(v->{
            showClassifyDialog();
        });
        //????????????
        viewBinding.llDateOfManufacture.setOnClickListener(v->{
            final DatePicker picker = new DatePicker(this);
            picker.setTopPadding(15);
            picker.setRangeStart(2000, 8, 29);
            picker.setRangeEnd(2111, 1, 11);
            picker.setSelectedItem(2022, 3, 15);
            picker.setWeightEnable(true);
            picker.setLineColor(Color.BLACK);
            picker.setTitleText("????????????");
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
        //???????????????????????????????????????
        viewBinding.llDays.setOnClickListener(v->{
            if (viewBinding.tvDays.getText().equals("????????????")){
                final DatePicker picker = new DatePicker(this);
                picker.setTopPadding(15);
                picker.setRangeStart(2000, 8, 29);
                picker.setRangeEnd(2111, 1, 11);
                picker.setSelectedItem(2022, 3, 15);
                picker.setWeightEnable(true);
                picker.setLineColor(Color.BLACK);
                picker.setTitleText("????????????");
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
        //????????????????????????
        viewBinding.llRemind.setOnClickListener(v->{
            Intent intent = new Intent(AddGoodsActivity.this,EditTimeReminderActivity.class);
            intent.putExtra("mObjectId",mClassObjectId);
            intent.putExtra("classifyName",viewBinding.tvClassify.getText().toString());
            intent.putExtra("reminderTime",viewBinding.tvRemind.getText().toString());
            intent.putExtra("activity","AddGoodsActivity");
            startActivity(intent);
        });
        //????????????????????????
        viewBinding.rlPhoto.setOnClickListener(v -> {
            uploadImage();
        });
        //????????????????????????
        viewBinding.includeTop.tvSave.setOnClickListener(v->{
            showLoading("?????????...", this);
            BmobGoods bmobGoods = new BmobGoods();
            bmobGoods.setUserId(ZSPTool.getString(Contents.USER_ID));
            if (StringUtil.isEmpty(viewBinding.etGoodsName.getText().toString())){
                showToast("?????????????????????");
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
            if (!StringUtil.isEmpty(viewBinding.tvDateOfManufacture.getText().toString())){
//                bmobGoods.setDateOfManufacture(new SimpleDateFormat("yyyy-MM-dd")
//                        .format(new Date(System.currentTimeMillis())));
//            }else {
                bmobGoods.setDateOfManufacture(viewBinding.tvDateOfManufacture.getText().toString());
            }
            if (viewBinding.tvDays.getText().toString().equals("????????????")){
                if (StringUtil.isEmpty(viewBinding.tvDisplayDays.getText().toString())){
                    showToast("???????????????????????????");
                    return;
                }else {
                    bmobGoods.setShelfLifeDays(viewBinding.tvDisplayDays.getText().toString());
                }
            }else {
                if (StringUtil.isEmpty(viewBinding.tvDisplayDays.getText().toString())){
                    showToast("???????????????????????????");
                    return;
                }else {
                    bmobGoods.setExpirationTime(viewBinding.tvDisplayDays.getText().toString());
                }
            }
            bmobGoods.setClassify(viewBinding.tvClassify.getText().toString());
            bmobGoods.setTimeReminder(viewBinding.tvRemind.getText().toString());
            bmobGoods.setPhoto(photoPath);
            if (editStr.equals("editStr")) {
                bmobGoods.update(mObjectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        finishLoading();
                        if (e == null) {
                            showToast("????????????");
                            finish();
                        }else {
                            showToast("????????????!");
                            Log.e(TAG,e.toString());
                        }
                    }
                });
            }else {
                bmobGoods.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        finishLoading();
                        if (e == null){
                            showToast("????????????!");
                            finish();
                        }else {
                            showToast("????????????!");
                            Log.e(TAG,e.toString());
                        }
                    }
                });
            }
        });
    }

    private void uploadImage() {
        //1?????????Dialog?????????style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2???????????????
        View view = View.inflate(this, R.layout.dialog_update_head, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //??????????????????
        window.setGravity(Gravity.BOTTOM);
        //??????????????????
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //?????????????????????
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 0;
                checkTakePhotoRequestPermission();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_photo_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                checkPhotoRequestPermission();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //??????????????????
    @TargetApi(Build.VERSION_CODES.M)
    private void checkTakePhotoRequestPermission() {
        List<String> list = new ArrayList<>();
        if (!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            list.add(Manifest.permission.CAMERA);
        }
        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (list.size() == 0) {
            gotoCamera();
        } else {
            String[] requestPermissions = new String[list.size()];
            list.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }


    /**
     * ??????????????????
     */
    private void gotoCamera() {
        Log.d("evan", "*****************????????????********************");
        //?????????????????????????????????
        //tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");
        tempFile = FileManager.createFileWithReturnFile(Contents.ROOT_PATH, "ivActivityPhoto.jpg");
        //???????????????????????????
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //??????7.0???????????????????????????????????????xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri;
            if (Build.VERSION.SDK_INT >=24) {
                contentUri = FileProvider.getUriForFile(mContext, "com.android.timeoverdue.fileProvider", tempFile);
            }else {
                contentUri = Uri.fromFile(tempFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    //????????????????????????
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPhotoRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        // ??????????????????????????????????????????SDK
        if (lackedPermission.size() == 0) {
            gotoPhoto();
        } else {
            // ??????????????????????????????onRequestPermissionsResult???????????????????????????????????????????????????????????????SDK?????????????????????SDK???
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    /**
     * ???????????????
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************????????????********************");
        //???????????????????????????
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "???????????????"), REQUEST_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_CAPTURE: //????????????????????????
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //????????????????????????
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //??????????????????
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
//                    Glide.with(this).load(uri).into(viewBinding.ivPhoto);
                    photoPath = intent.getStringExtra("photo");
                    viewBinding.llPhoto.setVisibility(View.GONE);

                    String cropImagePath = getRealFilePathFromUri(getBaseContext(), uri);
                    try {
                        Bitmap bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        viewBinding.ivPhoto.setImageBitmap(bitMap);
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * ??????????????????
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    //????????????
    private void showNumberDialog(){
        AlertDialog builder = new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_good_number,null);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final TextView tvSure = view.findViewById(R.id.tv_sure);
        final EditText etNumber = view.findViewById(R.id.et_number);
        final ScrollPickerView scrollPickerView = view.findViewById(R.id.pickerView);
        final LinearLayout llAddCompany = view.findViewById(R.id.ll_add_company);
        builder.setView(view);	//?????????????????????????????????dialog_exit.xml???????????????
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

    //????????????
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

    //????????????
    private void showClassifyDialog(){
        AlertDialog builder = new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_classify,null);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final TextView tvSure = view.findViewById(R.id.tv_sure);
        ScrollPickerView pickerView = view.findViewById(R.id.pickerView);
        final LinearLayout llAddClassify = view.findViewById(R.id.ll_add_classify);
        builder.setView(view);	//?????????????????????????????????dialog_exit.xml???????????????


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

    //??????????????????
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

    //?????????????????????????????????
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
                    mClassObjectId = list.get(0).getObjectId();
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    //???????????????????????????
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

    private void getGoodsInfo(){
        BmobQuery<BmobGoods> query = new BmobQuery<>();
        query.getObject(mObjectId, new QueryListener<BmobGoods>() {
            @Override
            public void done(BmobGoods info, BmobException e) {
                if (e == null){
                    viewBinding.etGoodsName.setText(info.getName());
                    if(!StringUtil.isEmpty(info.getNumber())){
                        viewBinding.tvNumber.setText(info.getNumber());
                    }
                    viewBinding.tvClassify.setText(info.getClassify());
                    if (viewBinding.tvDays.getText().equals("????????????")){
                        viewBinding.tvDisplayDays.setText(info.getExpirationTime());
                    }else {
                        if (!StringUtil.isEmpty(info.getShelfLifeDays())){
                            viewBinding.tvDisplayDays.setText(info.getShelfLifeDays());
                        }
                    }
                    if (!StringUtil.isEmpty(info.getDateOfManufacture())){
                        viewBinding.tvDateOfManufacture.setText(info.getDateOfManufacture());
                    }
                    if (StringUtil.isEmpty(info.getRemarks())){
                        viewBinding.etRemarks.setText("");
                    }else {
                        viewBinding.etRemarks.setText(info.getRemarks());
                    }
                    viewBinding.tvRemind.setText(info.getTimeReminder());
                    if (StringUtil.isEmpty(info.getPhoto())) {
                        viewBinding.llPhoto.setVisibility(View.VISIBLE);
                    }else {
                        viewBinding.llPhoto.setVisibility(View.GONE);
                        Bitmap bitmap = CommonUtils.convertStringToIcon(info.getPhoto());
                        viewBinding.ivPhoto.setImageBitmap(bitmap);
                        photoPath = info.getPhoto();
                    }
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

}
