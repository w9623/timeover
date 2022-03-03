package com.android.timeoverdue.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobClassify;
import com.android.timeoverdue.bean.BmobCompany;
import com.android.timeoverdue.databinding.ActivityCompanyBinding;
import com.android.timeoverdue.ui.adapter.ClassifyAdapter;
import com.android.timeoverdue.ui.adapter.CompanyAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class CompanyActivity extends BaseActivity<ActivityCompanyBinding> {

    private List<BmobCompany> list;
    private CompanyAdapter companyAdapter;

    private ClassifyAdapter.OnItemClickListener onItemClickListener = new ClassifyAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            if (list.get(position).getSystem()){
                showToast("系统单位不支持编辑！");
            }else {
                Intent intent = new Intent(CompanyActivity.this,AddCompanyActivity.class);
                intent.putExtra(TAG,"update");
                intent.putExtra("companyName",list.get(position).getName());
                intent.putExtra("mObjectId",list.get(position).getObjectId());
                startActivity(intent);
            }

        }
    };

    private CompanyAdapter.DelClickListener delClickListener = new CompanyAdapter.DelClickListener() {
        @Override
        public void onClick(int position) {
            showDelDialog(position);
        }
    };

    @Override
    protected void initData() {
        getCompanyList();
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.tvTitle.setText("自定义单位");
        viewBinding.includeTop.tvSave.setVisibility(View.GONE);
        viewBinding.includeTop.ivMore.setVisibility(View.VISIBLE);
        viewBinding.includeTop.ivMore.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add));
    }

    @Override
    protected void onClick() {
        //返回按钮点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //添加按钮点击事件
        viewBinding.includeTop.ivMore.setOnClickListener(v->{
            Intent intent = new Intent(this,AddCompanyActivity.class);
            intent.putExtra(TAG,"add");
            startActivity(intent);
        });
    }

    //查询分类列表
    private void getCompanyList(){
        BmobQuery<BmobCompany> query = new BmobQuery<BmobCompany>();
        query.order("createAt");
        query.findObjects(new FindListener<BmobCompany>() {
            @Override
            public void done(List<BmobCompany> data, BmobException e) {
                if (e == null){
                    list = data;
                    if (companyAdapter == null){
                        companyAdapter = new CompanyAdapter(mContext,data);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        companyAdapter.setOnItemClickListener(onItemClickListener);
                        companyAdapter.setDelClickListener(delClickListener);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(companyAdapter);
                    }else{
                        companyAdapter.setData(list);
                    }
                }else {
                    Log.e(TAG,e.toString());
                }

            }
        });
    }

    //弹窗提示当前要删除item
    private void showDelDialog(int position){
        AlertDialog builder = new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_del,null);
        final TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final TextView tvDel = view.findViewById(R.id.tv_del);
        final TextView tvDelContent = view.findViewById(R.id.tv_del_content);
        tvDelContent.setText("确定删除“"+list.get(position).getName()+"”吗？");
        builder.setView(view);	//把弹出窗口的页面设定为dialog_del.xml设定的页面
        tvCancel.setOnClickListener(v->{
            builder.cancel();
        });
        tvDel.setOnClickListener(v->{
            delete(list.get(position).getObjectId(),position);
            builder.cancel();
        });
        builder.show();
    }

    /**
     * 删除一条数据
     */
    private void delete(String mObjectId,int position) {
        BmobCompany bmobCompany = new BmobCompany();
        bmobCompany.delete(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    showToast("删除成功！");
                    companyAdapter.removePosition(position);
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCompanyList();
    }
}
