package com.android.timeoverdue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.bean.BmobGoods;
import com.android.timeoverdue.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BmobGoods> mDatas;

    public GoodsAdapter(Context context, List<BmobGoods> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    public void removePosition(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,mDatas.size());
    }

    public void setData(List<BmobGoods> list){
        mDatas.clear();
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent,false);
        return new GoodsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GoodsHolder goodsHolder = (GoodsHolder) holder;
        goodsHolder.tvGoodsName.setText(mDatas.get(position).getName());
        if (StringUtil.isEmpty(mDatas.get(position).getNumber())){
            goodsHolder.tvNumber.setVisibility(View.GONE);
        }else {
            goodsHolder.tvNumber.setVisibility(View.VISIBLE);
            goodsHolder.tvNumber.setText("数量："+mDatas.get(position).getNumber());
        }
        goodsHolder.progressBar.setProgress(0);
        if (!StringUtil.isEmpty(mDatas.get(position).getExpirationTime())){
            int day = dateDiff(mDatas.get(position).getExpirationTime());
            if (day > 0 || day > 0){
                goodsHolder.tvExplain.setText("剩余");
                goodsHolder.tvDay.setTextColor(mContext.getResources().getColor(R.color.color_DC143C));
            }else {
                goodsHolder.tvExplain.setText("已过期");
                goodsHolder.tvDay.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            goodsHolder.tvDay.setText(day+"天");
        }
        goodsHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size() != 0 ? mDatas.size() : 0;
    }

    public class GoodsHolder extends RecyclerView.ViewHolder{
        public TextView tvGoodsName,tvNumber,tvExplain,tvDay;
        public ProgressBar progressBar;

        public GoodsHolder(View itemView) {
            super(itemView);

            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvExplain = itemView.findViewById(R.id.tv_explain);
            tvDay = itemView.findViewById(R.id.tv_day);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    /***
     * 获取当前日期距离过期时间的日期差值
     * @param endTime
     * @return
     */
    public static int dateDiff(String endTime) {
        int strTime;
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = sd.format(curDate);
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(str).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            if (day >= 1) {
                strTime = (int) day;
            } else {
                strTime = 0;
            }

            return strTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

}
