package com.gachat.main.ui.doll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gachat.main.R;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseDialogFragment;
import com.gachat.main.beans.RechargeListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.GetRechargeListPresenter;
import com.gachat.main.util.MyAnimations;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 充值
 */
public class RechargeDialogFragment extends BaseDialogFragment implements OnPresenterListener.OnViewListener<BaseBean<RechargeListBean>>{

    private static final String TAG = "RechargeDialogFragment";
    
    @SuppressLint("StaticFieldLeak")
    private static RechargeDialogFragment instance;

    private RadioGroup mRadioGroup;
    private RecyclerView mPriceRecyclerView;
    private GetRechargeListPresenter mPresenter;
    private List<RechargeListBean.GoodsInfoBean> mList;
    private PriceListAdapter mAdapter;

    public RechargeDialogFragment() { }
    public static synchronized RechargeDialogFragment getInstance() {
        if (instance == null) {
            instance = new RechargeDialogFragment();
        }
        return instance;
    }

    private WeakReference<DollRoomActivity> mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=new WeakReference<>((DollRoomActivity) context);
    }

    @Override
    protected int setResLayoutId() {
        return R.layout.fragment_recharge_dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        params.gravity = Gravity.BOTTOM; // 显示在底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度填充满屏
        window.setAttributes(params);

        // 这里用透明颜色替换掉系统自带背景
        int color = ContextCompat.getColor(mActivity.get(), android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(color));
    }

    @Override
    protected void initView(View view) {
        mRadioGroup=view.findViewById(R.id.rbtngroup);
        mPriceRecyclerView=view.findViewById(R.id.lv_RecyclerView);

        mList=new ArrayList<>();
        mAdapter=new PriceListAdapter();

        mPresenter=new GetRechargeListPresenter(this);

        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        mPriceRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity.get(),DividerItemDecoration.VERTICAL));
        mPriceRecyclerView.setLayoutManager(manager);
        mPriceRecyclerView.setAdapter(mAdapter);
        mPresenter.getRechargeListPresenter();
        MyAnimations.startUpAnimation(view);
        view.findViewById(R.id.iv_close).setOnClickListener(v -> {
            MyAnimations.startDownAnimation(view);
            dismiss();
        });

        view.findViewById(R.id.ll_finish).setOnClickListener(v -> {
            MyAnimations.startDownAnimation(view);
            dismiss();
        });

        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbtn_wachatpay:{

                    break;
                }
                case R.id.rbtn_alipay:{

                    break;
                }
            }
        });
    }

    @Override
    public void onSuccess(BaseBean<RechargeListBean> result) {
        if (result.getCode() == 0 && result.getResult() != null) {
            Log.i(TAG, "onSuccess: "+result.toString());
            mList.addAll(result.getResult().getCharge_goods());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailed(Throwable throwable) {
        Log.i(TAG, "onFailed: "+throwable.getMessage());
    }

    private class PriceListAdapter extends RecyclerView.Adapter<PriceListAdapter.PriceViewHolder> {

        @NonNull
        @Override
        public PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PriceViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_recharge_list_layout,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull PriceViewHolder holder, int position) {
            holder.tvDiamondsCount.setText(mList.get(position).getDiamond()+"钻");
            holder.tvMoney.setText("￥"+mList.get(position).getPrice());
            holder.tvMoney.setOnClickListener(v -> Log.i(TAG, "onBindViewHolder: "+mList.get(position).getCharge_good_id()));
        }

        @Override
        public int getItemCount() {   return mList.size();  }

        class PriceViewHolder extends RecyclerView.ViewHolder{
            TextView tvDiamondsCount,tvMoney;
             PriceViewHolder(View itemView) {
                super(itemView);
                tvDiamondsCount=itemView.findViewById(R.id.tv_DiamondsCount);
                tvMoney=itemView.findViewById(R.id.tv_Money);
            }
        }

    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }

        if (mList != null) {
            mList.clear();
            mList=null;
        }

        if (mAdapter != null) {
            mAdapter=null;
        }

        if (mPriceRecyclerView != null) {
            mPriceRecyclerView.removeAllViews();
            mPriceRecyclerView=null;
        }

        super.onDestroyView();
    }

}
