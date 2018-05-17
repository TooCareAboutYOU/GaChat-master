package com.gachat.main.ui.chat;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.application.MyApplication;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseDialogFragment;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.SendReportPresenter;

/**
 * 举报
 */

public class ReportDialogFragment extends BaseDialogFragment implements OnPresenterListener.OnViewListener<BaseBean<MessageBean>>{

    @SuppressLint("StaticFieldLeak")
    private static ReportDialogFragment instance;
    public ReportDialogFragment (){}
    public static synchronized ReportDialogFragment getInstance() {
         if (instance == null) {
             instance = new ReportDialogFragment();
         }
     return instance;
    }

    private SendReportPresenter mPresenter;

    @Override
    public int setResLayoutId() { return R.layout.chat_report_dialog; }

    @Override
    public void initView(View view) {
        mPresenter=new SendReportPresenter(this);
        view.findViewById(R.id.tv_reason_one).setOnClickListener(v -> reasonType(0));
        view.findViewById(R.id.tv_reason_two).setOnClickListener(v -> reasonType(1));
        view.findViewById(R.id.tv_reason_three).setOnClickListener(v -> reasonType(2));
        view.findViewById(R.id.cancel).setOnClickListener(v -> getDialog().dismiss());
    }


    private void reasonType(int reasonId){
        long userId= Constant.getCalleeId();
        if (userId != -1) {
            mPresenter.sendReport(userId, reasonId);
            dismiss();
        }else {
            Toast.makeText(MyApplication.getInstance(),"不能举报自己！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(BaseBean<MessageBean> result) {
        if (result.getCode() == 0){
            if (result.getResult() != null) {
                ToastUtils.showShort(result.getResult().getMessage());
            }
        }else {
            if (result.getError() != null) {
                for (String s : result.getError().getMessage()) {
                    if (!TextUtils.isEmpty(s)) {
                        ToastUtils.showShort(s);
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(Throwable throwable) {
        ToastUtils.showShort(throwable.getMessage());
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
