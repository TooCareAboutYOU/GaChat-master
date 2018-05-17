package com.gachat.main.ui.user.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gachat.main.R;
import com.gachat.main.api.QueryType;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseFragment;
import com.gachat.main.beans.GiftListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.GiftListPresenter;
import com.gachat.main.ui.user.MyDollsActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PostDollsFragment extends BaseFragment implements OnPresenterListener.OnViewListener<BaseBean<GiftListBean>> {

    private static final String TAG = "GetDolls";

    ImageView mFailedView;
    RecyclerView mRecyclerView;


    private GiftListPresenter mPresenter;
    private List<GiftListBean.AssetsBean> mList;
    private RecyclerViewAdapter mAdapter;


    @SuppressLint("StaticFieldLeak")
    private static PostDollsFragment instance;

    public static synchronized PostDollsFragment newInstance() {
        if (instance == null) {
            instance = new PostDollsFragment();
        }
        return instance;
    }

    private WeakReference<MyDollsActivity> mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = new WeakReference<>((MyDollsActivity) getActivity());
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_dolls;
    }

    @Override
    public void initView(View view) {

        mFailedView=view.findViewById(R.id.iv_loadFailed);
        mRecyclerView=view.findViewById(R.id.recyclerview);

        Log.i(TAG, "PostDollsFragment  initView: ");
        mPresenter = new GiftListPresenter(this);

        mList = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mFailedView.setOnClickListener(v ->
                LoadData()
        );
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            Log.i(TAG, "onFragmentVisibleChange: PostDollsFragment 显示");
            LoadData();
        } else {
            Log.i(TAG, "onFragmentVisibleChange: PostDollsFragment 隐藏");
        }
    }

    private void LoadData() {
        mPresenter.getUserGiftlist(QueryType.LOSE);
    }

    @Override
    public void onSuccess(BaseBean<GiftListBean> result) {
        if (result.getResult() == null) return;

        if (result.getCode() == 0 && result.getResult() != null) {
            mActivity.get().runOnUiThread(() -> {
                if (result.getResult().getCount() == 0) {
                    mFailedView.setVisibility(View.VISIBLE);
                } else {
                    Log.i(TAG, "实现：");
                    if (mList.size() > 0) {
                        mList.clear();
                    }
                    mList.addAll(result.getResult().getAssets());
                    mAdapter.notifyDataSetChanged();
                    mFailedView.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onFailed(Throwable throwable) {
        Log.i(TAG, "onFailed: " + throwable.getMessage());
        mActivity.get().runOnUiThread(() -> {
            Log.i(TAG, "onFailed run: ");
            Toast.makeText(mActivity.get(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

        int LAYOUT_GIVE_FRIEND=0;
        int LAYOUT_READY_SEND_GOODS=1;
        int LAYOUT_SENDED_GOODS=2;
        int LAYOUT_OVERDUE=3;


        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_lostdoll_layout, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!TextUtils.isEmpty(mList.get(position).getExpress().getExpress_sn())) {
                holder.mTvExpressSn.setText(mList.get(position).getExpress().getExpress_sn());
            }

            if (!TextUtils.isEmpty(mList.get(position).getItem().getImage_url())) {
                holder.mSdvImage.setImageURI(Uri.parse(mList.get(position).getItem().getImage_url()));
            }

            if (!TextUtils.isEmpty(mList.get(position).getItem().getName())) {
                holder.mTvGoodsName.setText(mList.get(position).getItem().getName());
            }
            if (!TextUtils.isEmpty(mList.get(position).getCreate_time())) {
                holder.mTvTime.setText(mList.get(position).getCreate_time());
            }

            String status="";
            switch (mList.get(position).getStatus()){
                case 2:{
                    status="已送出";
                    break;
                }
                case 3:{
                    status="准备发货";
                    break;
                }
                case 4: {
                    status="已发货";
                    break;
                }
                case 5:{
                    status="已过期";
                    break;
                }
            }

            holder.mTvStatus.setText(status);
        }

        @Override
        public int getItemCount() {  return mList.size();  }

        @Override
        public int getItemViewType(int position) {
            if (mList.get(position).getStatus() == 2) {
                return LAYOUT_GIVE_FRIEND;
            }else if (mList.get(position).getStatus() == 3) {
                return LAYOUT_READY_SEND_GOODS;
            }else if (mList.get(position).getStatus() == 4) {
                return LAYOUT_SENDED_GOODS;
            }else {
                return LAYOUT_OVERDUE;
            }
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView mTvExpressSn;
            SimpleDraweeView mSdvImage;
            TextView mTvGoodsName;
            TextView mTvTime;
            TextView mTvStatus;

            ItemViewHolder(View itemView) {
                super(itemView);
                mTvExpressSn = itemView.findViewById(R.id.tv_express_sn);
                mSdvImage = itemView.findViewById(R.id.sdv_image);
                mTvGoodsName = itemView.findViewById(R.id.tv_goods_name);
                mTvTime = itemView.findViewById(R.id.tv_time);
                mTvStatus = itemView.findViewById(R.id.tv_status);
            }
        }

    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mList != null) {
            mList.clear();
            mList=null;
        }

        super.onDestroyView();
    }
}
