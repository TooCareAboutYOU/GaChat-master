package com.gachat.main.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.FeedBackBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.FeedBacksPresenter;
import com.gachat.main.util.MyUtils;

public class FeedbackActivity extends BaseActivity implements OnPresenterListener.OnViewListener<BaseBean<FeedBackBean>>{

    EditText mEtInfo;
    EditText mEtContact;

    private FeedBacksPresenter mPresenter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        setBackIcon(R.drawable.icon_back);
        mEtInfo=findViewById(R.id.et_info);
        mEtContact=findViewById(R.id.et_contact);
    }

    @Override
    protected void initOperate(Bundle savedInstanceState) {
        mPresenter=new FeedBacksPresenter(this);
        findViewById(R.id.tv_finish).setOnClickListener(v -> {
            String info=mEtInfo.getText().toString().trim();
            if (TextUtils.isEmpty(info)) {
                ToastUtils.showShort("意见内容不能为空");
                return;
            }

            if (info.length() > 200  ||  info.length() < 10) {
                ToastUtils.showShort("意见内容字数不够");
                return;
            }

            String contact=mEtContact.getText().toString().trim();
            if (TextUtils.isEmpty(contact)) {
                ToastUtils.showShort("联系方式不能为空");
                return;
            }

            if (!MyUtils.checkMobileNumber(contact) && !MyUtils.checkEmail(contact)) {
                ToastUtils.showShort("请输入正确手机号或邮箱");
                return;
            }

            if (mPresenter != null) {
                mPresenter.sendFeedBacks(info,contact);
            }
        });
    }

    @Override
    public void onSuccess(BaseBean<FeedBackBean> result) {
        if (result != null){
            if (result.getCode() == 0){
                mEtInfo.setText("");
                mEtContact.setText("");
                ToastUtils.showShort("反馈成功");
            }else {
                int size=result.getError().getMessage().size();
                if (size > 0){
                    for (String s : result.getError().getMessage()) {
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
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }
    }
}
