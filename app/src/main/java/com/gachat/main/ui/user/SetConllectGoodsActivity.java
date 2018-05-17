package com.gachat.main.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.ConllectGoodsPresenter;
import com.gachat.main.util.MyUtils;

import butterknife.OnClick;

public class SetConllectGoodsActivity extends BaseActivity implements OnPresenterListener.OnViewListener<BaseBean<MessageBean>> {

    private static final String TAG = "SetConllectGoods";
    public static final String GOODID = "goodsId";

    EditText mEtName;
    EditText mEtMobile;
    EditText mEtAddress;
    LinearLayout mLlSendGoods;

    private ConllectGoodsPresenter mPresenter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_set_conllect_goods;
    }

    @Override
    protected void initView() {
        setBackIcon(R.drawable.icon_back);
        mEtName=findViewById(R.id.et_name);
        mEtMobile=findViewById(R.id.et_mobile);
        mEtAddress=findViewById(R.id.et_address);
        mLlSendGoods=findViewById(R.id.ll_sendGoods);
    }

    @Override
    protected void initOperate(Bundle savedInstanceState) {
        mPresenter = new ConllectGoodsPresenter(this);
    }

    @OnClick(R.id.ll_sendGoods)
    public void onViewClicked(View v) {
        String name=mEtName.getText().toString().trim();
        String mobile=mEtMobile.getText().toString().trim();
        String address=mEtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "收货人姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "收货人联系方式不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!MyUtils.checkMobileNumber(mobile)) {
            Toast.makeText(this, "手机号码错误!", Toast.LENGTH_SHORT).show();
            mEtMobile.setText("");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "收货人地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String goodsId = getIntent().getStringExtra(GOODID);
        Log.i(TAG, "goodsId: "+goodsId);
        if (TextUtils.isEmpty(goodsId)) {
            Toast.makeText(this, "物品异常", Toast.LENGTH_SHORT).show();
            return;
        }

        mPresenter.sendGoods(name,mobile,address,goodsId);
    }

    @Override
    public void onSuccess(BaseBean<MessageBean> result) {
        runOnUiThread(() -> {
            Log.i(TAG, "onSuccess: "+result.toString());

            if (result.getCode() == 0) {
                Toast.makeText(this, result.getResult().getMessage(), Toast.LENGTH_SHORT).show();
                SetConllectGoodsActivity.this.finish();
            }else {
                int size=result.getError().getMessage().size();
                if (size > 0){
                    for (int i = 0; i < size; i++) {
                        Toast.makeText(SetConllectGoodsActivity.this, result.getError().getMessage().get(i), Toast.LENGTH_SHORT).show();}

                }
            }

        });
    }

    @Override
    public void onFailed(Throwable throwable) {
        runOnUiThread(() -> Toast.makeText(SetConllectGoodsActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }
}
