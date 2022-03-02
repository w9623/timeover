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
import com.android.timeoverdue.databinding.ActivityClassifyBinding;
import com.android.timeoverdue.ui.adapter.ClassifyAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ClassifyActivity extends BaseActivity<ActivityClassifyBinding> {

    private List<BmobClassify> list;
    private ClassifyAdapter classifyAdapter;
    private ClassifyAdapter.OnItemClickListener onItemClickListener = new ClassifyAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            if (list.get(position).getSystem()){
                showToast("系统分类不支持编辑！");
            }else {
                Intent intent = new Intent(ClassifyActivity.this,AddClassifyActivity.class);
                intent.putExtra(TAG,"update");
                intent.putExtra("classifyName",list.get(position).getName());
                intent.putExtra("mObjectId",list.get(position).getObjectId());
                startActivity(intent);
            }

        }
    };
    //item的删除按钮点击事件
    private ClassifyAdapter.DelClickListener delClickListener = new ClassifyAdapter.DelClickListener() {
        @Override
        public void onClick(int position) {
            showDelDialog(position);
        }
    };

    @Override
    protected void initData() {
        getClassifyList();
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.tvTitle.setText("自定义分类");
        viewBinding.includeTop.tvSave.setVisibility(View.GONE);
        viewBinding.includeTop.ivMore.setVisibility(View.VISIBLE);
        viewBinding.includeTop.ivMore.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add));
    }

    //查询分类列表
    private void getClassifyList(){
        BmobQuery<BmobClassify> query = new BmobQuery<BmobClassify>();
        query.order("createAt");
        query.findObjects(new FindListener<BmobClassify>() {
            @Override
            public void done(List<BmobClassify> data, BmobException e) {
                if (e == null){
                    list = data;
                    if (classifyAdapter == null){
                        classifyAdapter = new ClassifyAdapter(mContext,data);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        classifyAdapter.setOnItemClickListener(onItemClickListener);
                        classifyAdapter.setDelClickListener(delClickListener);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(classifyAdapter);
                    }else{
                        classifyAdapter.setData(list);
                    }
                }else {
                    Log.e(TAG,e.toString());
                }

            }
        });
    }

    @Override
    protected void onClick() {
        //添加按钮点击事件
        viewBinding.includeTop.ivMore.setOnClickListener(v->{
            Intent intent = new Intent(this,AddClassifyActivity.class);
            intent.putExtra(TAG,"add");
            startActivity(intent);
        });
        //返回按钮点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
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
        builder.setView(view);	//把弹出窗口的页面设定为dialog_exit.xml设定的页面
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
        BmobClassify bmobClassify = new BmobClassify();
        bmobClassify.delete(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    showToast("删除成功！");
                    classifyAdapter.removePosition(position);
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getClassifyList();
    }
}
