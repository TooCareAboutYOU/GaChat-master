package com.gachat.main.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gachat.main.R;
import com.gachat.main.api.QueryType;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseDialogFragment;
import com.gachat.main.beans.GiftListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.GiftListPresenter;

import java.lang.ref.WeakReference;

/**
 * 点赞
 */

public class LikedDialogFragment extends BaseDialogFragment implements OnPresenterListener.OnViewListener<BaseBean<GiftListBean>> {

    private static final String TAG = "ChatRoomActivity";

    @SuppressLint("StaticFieldLeak")
    private static LikedDialogFragment instance=null;
    public LikedDialogFragment() { }
    public static synchronized LikedDialogFragment getInstance() {
        if (instance == null) {
            instance = new LikedDialogFragment();
        }
        return instance;
    }

    private TextView mTextView;
    private WeakReference<ChatRoomActivity> mActivity;
    private GiftListPresenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity =new WeakReference<>((ChatRoomActivity) context);
    }

    @Override
    public int setResLayoutId() {
        return R.layout.chat_liked_dialog;
    }

    @Override
    public void initView(View view) {
        mPresenter = new GiftListPresenter(this);
        mTextView=view.findViewById(R.id.tv_gotoCatchDoll);
        mTextView.setOnClickListener(v -> {
            dismiss();
            mActivity.get().gotoDoll();
            mActivity.get().finish();
        });
        view.findViewById(R.id.tv_GiveGift).setOnClickListener(v -> {
            if (mPresenter != null) {
                mPresenter.getUserGiftlist(QueryType.HAVE);
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(v -> dismiss());
    }

    @Override
    public void onSuccess(BaseBean<GiftListBean> result) {
        Log.i(TAG, "onSuccess: " + result.toString());
        if (result.getCode() == 0 && result.getResult() != null) {
            if (result.getResult().getAssets().size() == 0) {
                    Toast.makeText(mActivity.get().getApplicationContext(), "拥有礼物数为："+result.getResult().getAssets().size(), Toast.LENGTH_SHORT).show();
            } else {
//                List<GiftListBean.AssetsBean> mList=new ArrayList<>();
//                for (int i = 0; i < 5; i++) {
//                    GiftListBean.AssetsBean beans=new GiftListBean.AssetsBean();
//                    GiftListBean.AssetsBean.ItemBean itemBean=new GiftListBean.AssetsBean.ItemBean();
//                    itemBean.setImage_url("https://ws1.sinaimg.cn/large/610dc034ly1foowtrkpvkj20sg0izdkx.jpg");
//                    itemBean.setName("赛亚人");
//                    beans.setItem(itemBean);
//                    mList.add(beans);
//                }
//                GiftListDialogFragment.getInstance(mActivity,mList).show(mActivity.getSupportFragmentManager(),"gift");
                GiftListDialogFragment.getInstance(result.getResult().getAssets()).show(mActivity.get().getSupportFragmentManager(),"gift");
            }
        }
    }

    @Override
    public void onFailed(Throwable throwable) {
        Log.i(TAG, "onFailed: " + throwable.getMessage());
    }



    @Override
    public void onPause() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }

        mTextView=null;
        mActivity=null;

        super.onPause();
    }


}
