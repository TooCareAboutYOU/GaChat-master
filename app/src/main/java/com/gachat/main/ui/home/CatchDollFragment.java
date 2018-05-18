package com.gachat.main.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.dnion.VADollAPI;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gachat.main.Config;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseFragment;
import com.gachat.main.beans.DollBannerBean;
import com.gachat.main.beans.DollListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.GetDollPageDataPresenter;
import com.gachat.main.ui.MainActivity;
import com.gachat.main.ui.doll.DollRoomActivity;
import com.gachat.main.util.JumpToActivityUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CatchDollFragment extends BaseFragment {

    private static final String TAG = "CatchDollFragment";
    
    private Banner mBanner;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private ScrollView mScrollView;

    private GetDollPageDataPresenter mBannersPresenter, mListPresenter;

    private String roomId,uid, token, diamonds,img,name,desc;

    private List<DollListBean.RoomListBean> mListBeanList;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private GridLayoutManager manager;

    private MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_catch_doll;
    }

    @Override
    public void initView(View view) {
        mBannersPresenter = new GetDollPageDataPresenter(new LoadBanner());
        mListPresenter = new GetDollPageDataPresenter(new LoadList());
        mListBeanList = new ArrayList<>();

        mBanner=view.findViewById(R.id.banner);
        mRecyclerView=view.findViewById(R.id.recyclerView);
        mSwipeLayout=view.findViewById(R.id.swipeLayout);
        mScrollView=view.findViewById(R.id.scrollView);

        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(3000);
        mBanner.setBannerAnimation(Transformer.Accordion);
        mBanner.setIndicatorGravity(View.TEXT_ALIGNMENT_CENTER);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.start();

        mBanner.setOnBannerListener(position -> Toast.makeText(getContext(), "亲,点我干嘛!!!", Toast.LENGTH_SHORT).show());

        mSwipeLayout.setOnRefreshListener(() -> new Handler().post(() -> {
            mBannersPresenter.getDollBannersData();
            mListPresenter.getDollList(1);
        }));

        init();

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (!Constant.getDollConnect()) {
            connectSDK();
        }
        if (isVisible) {
            Log.i("isVisible", "onFragmentVisibleChange: CatchDollFragment 显示");
//            init();
        }else {
            Log.i("isVisible", "onFragmentVisibleChange: CatchDollFragment 隐藏");
            if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
                mSwipeLayout.setRefreshing(false);
            }
        }
    }

    private void connectSDK() {
        if (mActivity.UserData() != null) {
            String uid = mActivity.UserData().getUid() + "";
            String token = mActivity.UserData().getToken();
//            Log.i("ApplicationHelper", "doll connecting: ,uid=="+uid+"\t\t token=="+token);
            VADollAPI.getInstance().connect(Config.DOLL_SDK_URL, uid, token);
        }
    }

    void init() {
        if (mRecyclerViewAdapter == null) {
            //  https://blog.csdn.net/coralline_xss/article/details/72887136
            manager = new GridLayoutManager(getContext(), 2){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            //  https://www.jianshu.com/p/5c6f9540f9f6
            mRecyclerView.setNestedScrollingEnabled(false); //禁止rcyc嵌套滑动
            mRecyclerView.setLayoutManager(manager);
            mRecyclerViewAdapter = new RecyclerViewAdapter();
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        }

        mSwipeLayout.setRefreshing(true);

        mBannersPresenter.getDollBannersData();
        mListPresenter.getDollList(1);
    }

    //广告
    public class LoadBanner implements OnPresenterListener.OnViewListener<BaseBean<DollBannerBean>> {
        @Override
        public void onSuccess(BaseBean<DollBannerBean> result) {
            if (result.getCode() == 0) {
                if (result.getResult() != null && result.getResult().getBanners().size()>0) {
                    mBanner.update(result.getResult().getBanners());
                    mScrollView.setVisibility(View.VISIBLE);
                }
            }else {
                if (result.getError() != null) {
                    for (String s : result.getError().getMessage()) {
                        ToastUtils.showShort(s);
                    }
                }
            }
            mSwipeLayout.setRefreshing(false);
        }

        @Override
        public void onFailed(Throwable e) {
            ToastUtils.showShort(e.getMessage());
            mSwipeLayout.setRefreshing(false);
        }
    }

    //列表
    public class LoadList implements OnPresenterListener.OnViewListener<BaseBean<DollListBean>> {
        @Override
        public void onSuccess(BaseBean<DollListBean> result) {

            if (result.getCode() == 0) {
                if (result.getResult() != null) {
                    if (result.getResult().getRooms().size() > 0) {
                        mListBeanList.clear();
                        mListBeanList.addAll(result.getResult().getRooms());
                        mScrollView.setVisibility(View.VISIBLE);
                    }
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }
            }else {
                if (result.getError() != null) {
                    for (String s : result.getError().getMessage()) {
                        ToastUtils.showShort(s);
                    }
                }
            }
            mSwipeLayout.setRefreshing(false);
        }

        @Override
        public void onFailed(Throwable e) {
            ToastUtils.showShort(e.getMessage());
            mSwipeLayout.setRefreshing(false);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

        int LAYOUT_FREE=0;
        int LAYOUT_USEING=1;

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == LAYOUT_FREE) {
                return new ItemViewHolder(LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.item_doll_home_layout1,parent,false));
            }else {
                return new ItemViewHolder(LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.item_doll_home_layout2,parent,false));
            }
        }

        @SuppressLint({"SetTextI18n", "CheckResult"})
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (mListBeanList.get(position).getStatus() == 0) { //空闲
                holder.mTvTagTitle.setText("空闲中");
            }else {  // 游戏
                holder.mTvTagTitle.setText("游戏中");
            }
            if (!TextUtils.isEmpty(mListBeanList.get(position).getRoom_sku().getName())) {
                holder.mTvName.setText(mListBeanList.get(position).getRoom_sku().getName());
            }

            if (!TextUtils.isEmpty(mListBeanList.get(position).getImage_url())) {
                holder.mSdvPic.setImageURI(Uri.parse(mListBeanList.get(position).getImage_url()));
            }

            holder.mTvPrice.setText(mListBeanList.get(position).getPrice()+"钻/次");

            holder.itemView.setOnClickListener(v -> {
                if (mActivity.UserData() != null) {
                    uid = mActivity.UserData().getUid() + "";
                    token = mActivity.UserData().getToken();
                    diamonds=mActivity.UserData().getDiamond()+"";
                    roomId=mListBeanList.get(position).getIndex()+"";
                    img=mListBeanList.get(position).getImage_url();
                    name=mListBeanList.get(position).getRoom_sku().getName();
                    desc=mListBeanList.get(position).getRoom_sku().getDesc();
                    String userName=mActivity.UserData().getUsername();
                    if (Constant.getDollConnect()) {
                        startDollRoomActivity(roomId, uid,userName, diamonds, token,img,name,desc);
                    } else {
                        ToastUtils.showShort("连接异常，重连中...");
                        VADollAPI.getInstance().connect(Config.DOLL_SDK_URL, uid, token);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {  return mListBeanList.size();  }

        @Override
        public int getItemViewType(int position) {
            if (mListBeanList.get(position).getStatus() == 0) {
                return LAYOUT_FREE;
            }else {
                return LAYOUT_USEING;
            }
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView mSdvTagBlue,mSdvTagYellow;  //此处报空指针
            TextView mTvTagTitle;
            SimpleDraweeView mSdvPic;
            TextView mTvName;
            TextView mTvPrice;

             ItemViewHolder(View itemView) {
                super(itemView);
                 mSdvTagBlue=itemView.findViewWithTag(R.id.sdv_tag_blue);
                 mSdvTagYellow=itemView.findViewWithTag(R.id.sdv_tag_yellow);
                 mTvTagTitle=itemView.findViewById(R.id.tv_tag_title);
                 mSdvPic=itemView.findViewById(R.id.sdv_pic);
                 mTvName=itemView.findViewById(R.id.tv_name);
                 mTvPrice=itemView.findViewById(R.id.tv_price);
            }
        }

    }

    public void startDollRoomActivity(String roomId, String uId,String username, String diamonds, String token,String img,String name,String desc) {
        Bundle bundle = new Bundle();
        bundle.putString(DollRoomActivity.ROOM_ID, roomId);
        bundle.putString(DollRoomActivity.USER_ID, uId);
        bundle.putString(DollRoomActivity.USER_NAME,username);
        bundle.putString(DollRoomActivity.DIAMONDS, diamonds);
        bundle.putString(DollRoomActivity.TOKEN, token);
        bundle.putString(DollRoomActivity.GOODS_IMG,img);
        bundle.putString(DollRoomActivity.GOODS_NAME,name);
        bundle.putString(DollRoomActivity.GOODS_DESC,desc);
        JumpToActivityUtil.jumpParams(mActivity,DollRoomActivity.class,bundle,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        if (mBannersPresenter != null || mListPresenter != null) {
            Objects.requireNonNull(mBannersPresenter).detachView();
            mBannersPresenter=null;
            mListPresenter=null;
        }

        if (mListBeanList != null) {
            mListBeanList.clear();
            mListBeanList=null;
        }
        if (manager != null) {
            manager=null;
        }

        super.onDestroyView();
    }

    private class FrescoImageLoader extends ImageLoader{
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            DollBannerBean.BannerBean bean= (DollBannerBean.BannerBean) path;
            imageView.setImageURI(Uri.parse(bean.getImage_url()));
        }

        @Override
        public ImageView createImageView(Context context) {
            return new SimpleDraweeView(getContext());
        }
    }

}
