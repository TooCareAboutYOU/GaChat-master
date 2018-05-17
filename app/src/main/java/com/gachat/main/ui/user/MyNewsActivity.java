package com.gachat.main.ui.user;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
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
import com.gachat.main.base.BaseActivity;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.NewsBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.MyNewsPresenter;

import java.util.ArrayList;
import java.util.List;

public class MyNewsActivity extends BaseActivity implements OnPresenterListener.OnViewListener<BaseBean<NewsBean>> {

    private static final String TAG = "MyNewsActivity";

    RecyclerView mRecyclerView;
    ImageView mIvLoadFailed;


    private MyNewsPresenter mPresenter;
    private List<NewsBean.GiftsBean> mList;
    private RecyclerViewApdater mApdater;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_my_news;
    }

    @Override
    protected void initView() {
        setBackIcon(R.drawable.icon_back);
        mRecyclerView=findViewById(R.id.recyclerview);
        mIvLoadFailed=findViewById(R.id.iv_loadFailed);
    }

    @Override
    protected void initOperate(Bundle savedInstanceState) {
        mPresenter = new MyNewsPresenter(this);
        mList=new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(manager);
        mApdater =new RecyclerViewApdater();
        mRecyclerView.setAdapter(mApdater);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        mPresenter.getMyNews();
    }

    @Override
    public void onSuccess(BaseBean<NewsBean> result) {
        runOnUiThread(() -> {
            Log.i(TAG, "onSuccess: " + result.toString());
            if (result.getResult() != null && result.getResult().getCount() > 0) {
                mIvLoadFailed.setVisibility(View.GONE);
                mList.addAll(result.getResult().getGifts());
                mApdater.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFailed(Throwable throwable) {
        runOnUiThread(() -> {
            Log.i(TAG, "onFailed: " + throwable.getMessage());
            Toast.makeText(MyNewsActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    public class RecyclerViewApdater extends RecyclerView.Adapter<RecyclerViewApdater.ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(MyNewsActivity.this).inflate(R.layout.item_mynews_layout, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!TextUtils.isEmpty(mList.get(position).getUser_from_asset().getImage_url())) {
                holder.mSdvImage.setImageURI(Uri.parse(mList.get(position).getUser_from_asset().getImage_url()));
            }

            if (!TextUtils.isEmpty(mList.get(position).getUser_from_asset().getName())) {
                holder.mTvGoodsName.setText(mList.get(position).getUser_from_asset().getName());
            }
            if (!TextUtils.isEmpty(mList.get(position).getCreate_time())) {
                holder.mTvTime.setText(mList.get(position).getCreate_time());
            }

            if (!TextUtils.isEmpty(mList.get(position).getUser_from().getUsername())) {
                holder.mTvFrom.setText(mList.get(position).getUser_from().getUsername());
            }

        }

        @Override
        public int getItemCount() {  return mList.size();  }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView mSdvImage;
            TextView mTvGoodsName;
            TextView mTvTime;
            TextView mTvFrom;

            ItemViewHolder(View itemView) {
                super(itemView);
                mSdvImage = itemView.findViewById(R.id.sdv_image);
                mTvGoodsName = itemView.findViewById(R.id.tv_goods_name);
                mTvTime = itemView.findViewById(R.id.tv_time);
                mTvFrom=itemView.findViewById(R.id.tv_from);
            }
        }

    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        if (mPresenter != null) {
            mPresenter.detachView();

        }

        if (mList != null) {
            mList.clear();
            mList=null;
        }
        super.onDestroy();
    }
}
