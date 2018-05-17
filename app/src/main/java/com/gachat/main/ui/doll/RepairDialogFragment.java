package com.gachat.main.ui.doll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.gachat.main.R;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseDialogFragment;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.RepairPresenter;

import java.lang.ref.WeakReference;

/**
 * 报修
 */

public class RepairDialogFragment extends BaseDialogFragment implements OnPresenterListener.OnViewListener<BaseBean<MessageBean>>{

//    private static final String TAG = "RepairDialogFragment";

    @SuppressLint("StaticFieldLeak")
    private static RepairDialogFragment instance;
    public RepairDialogFragment (){}
    public static synchronized RepairDialogFragment getInstance() {
         if (instance == null) {
             instance = new RepairDialogFragment();
         }
     return instance;
    }

    private RepairPresenter mPresenter;

    private WeakReference<DollRoomActivity> mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=new WeakReference<>((DollRoomActivity) context);
    }

    @Override
    public int setResLayoutId() { return R.layout.doll_repair_dialog; }

    @Override
    public void initView(View view) {
        mPresenter=new RepairPresenter(this);
        int roomId=Integer.parseInt(mActivity.get().roomId);
        view.findViewById(R.id.reason0).setOnClickListener(v -> {
            mPresenter.sendRepair(roomId,0);
            dismiss();
        });
        view.findViewById(R.id.reason1).setOnClickListener(v -> {
            mPresenter.sendRepair(roomId,1);
            dismiss();
        });
        view.findViewById(R.id.reason2).setOnClickListener(v -> {
            mPresenter.sendRepair(roomId,2);
            dismiss();
        });
        view.findViewById(R.id.reason3).setOnClickListener(v -> {
//            mPresenter.sendRepair(0,4);
            dismiss();
        });
    }

    @Override
    public void onSuccess(BaseBean<MessageBean> result) {
        if (result != null) {
            switch (result.getCode()) {
                case 0:{  break;}
                case 10031:{  break;}
                case 10401:{  break;}
                case 10400:{  break;}
            }
            if (!TextUtils.isEmpty(result.getResult().getMessage())) {
                ToastUtils.showShort(result.getResult().getMessage());
            }
        }
    }

    @Override
    public void onFailed(Throwable throwable) {
        if (throwable != null && !TextUtils.isEmpty(throwable.getMessage())) {
            ToastUtils.showShort(throwable.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }
        super.onDestroyView();
    }
}
