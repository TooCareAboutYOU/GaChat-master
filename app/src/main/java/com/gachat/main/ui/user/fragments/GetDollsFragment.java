package com.gachat.main.ui.user.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.gachat.main.ui.user.SetConllectGoodsActivity;
import com.gachat.main.util.JumpToActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetDollsFragment extends BaseFragment implements OnPresenterListener.OnViewListener<BaseBean<GiftListBean>> {

    private static final String TAG = "GetDollsFragment";

    ImageView mFailedView;
    RecyclerView mRecyclerView;


    private GiftListPresenter mPresenter;
    private List<GiftListBean.AssetsBean> mList;
    private RecyclerViewAdapter mAdapter;

    @SuppressLint("StaticFieldLeak")
    private static GetDollsFragment instance;

    public static synchronized GetDollsFragment newInstance() {
        if (instance == null) {
            instance = new GetDollsFragment();
        }
        return instance;
    }

    private MyDollsActivity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MyDollsActivity) getActivity();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_get_dolls;
    }

    @Override
    public void initView(View view) {
        mFailedView=view.findViewById(R.id.iv_loadFailed);
        mRecyclerView=view.findViewById(R.id.recyclerview);

        Log.i(TAG, "GetDollsFragment  initView: ");
        mPresenter = new GiftListPresenter(this);

        mList = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter();
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
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
            Log.i(TAG, "onFragmentVisibleChange: GetDollsFragment 显示");
            LoadData();
        } else {
            Log.i(TAG, "onFragmentVisibleChange: GetDollsFragment 隐藏");
        }
    }

    private void LoadData() {
        mPresenter.getUserGiftlist(QueryType.HAVE);
    }

    @Override
    public void onSuccess(BaseBean<GiftListBean> result) {
        Log.i(TAG, "onSuccess: " + result.toString());
        if (result.getResult() == null) return;

        if (result.getCode() == 0 && result.getResult() != null) {
            mActivity.runOnUiThread(() -> {
                if (result.getResult().getCount() == 0) {
                    mFailedView.setVisibility(View.VISIBLE);
                } else {
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
        mActivity.runOnUiThread(() -> {
            Log.i(TAG, "onFailed run: ");
            Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

//        int LAYOUT_GET_CATCH=0;
//        int LAYOUT_GET_FRIENDY=1;

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_getdoll_layout, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!TextUtils.isEmpty(mList.get(position).getSource())) {
                holder.mTvSource.setText(mList.get(position).getSource());
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

            holder.mTvSendGoods.setOnClickListener(v -> {
                Bundle bundle=new Bundle();
                bundle.putString(SetConllectGoodsActivity.GOODID,mList.get(position).getUser_asset_id()+"");
                JumpToActivityUtil.jumpParams(mActivity, SetConllectGoodsActivity.class,bundle,false);
            });

        }

        @Override
        public int getItemCount() {  return mList.size();  }

//        @Override
//        public int getItemViewType(int position) {
//            if (mList.get(position).getStatus() == 0) {
//                return LAYOUT_GET_CATCH;
//            }else {
//                return LAYOUT_GET_FRIENDY;
//            }
//        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView mTvSource;
            SimpleDraweeView mSdvImage;
            TextView mTvGoodsName;
            TextView mTvTime;
            TextView mTvSendGoods;

            ItemViewHolder(View itemView) {
                super(itemView);
                mTvSource=itemView.findViewById(R.id.tv_source);
                mSdvImage=itemView.findViewById(R.id.sdv_image);
                mTvGoodsName=itemView.findViewById(R.id.tv_goods_name);
                mTvTime=itemView.findViewById(R.id.tv_time);
                mTvSendGoods=itemView.findViewById(R.id.tv_sendGoods);
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
