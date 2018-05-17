package com.gachat.main.ui.login.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.gachat.generator.config.DaoInsert;
import com.gachat.generator.model.UserBean;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseFragment;
import com.gachat.main.beans.LoginBean;
import com.gachat.main.beans.RegisterBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.LoginPresenter;
import com.gachat.main.mvp.presenters.RegisterPresenter;
import com.gachat.main.ui.MainActivity;
import com.gachat.main.ui.login.activity.RegisterActivity;
import com.gachat.main.util.JumpToActivityUtil;
import com.gachat.main.util.SharedPreferencesHelper;
import com.gachat.main.util.widgets.BottomDialog;
import com.gachat.main.util.widgets.wheelview.WheelView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;


public class RegisterPerfectInfoFragment extends BaseFragment implements OnPresenterListener.OnViewListener<BaseBean<RegisterBean>> {

    private static final String TAG = "MyRegisterActivity";

    @SuppressLint("StaticFieldLeak")
    private static RegisterPerfectInfoFragment instance;

    EditText mEtInputNickName;
    TextView mEtInputAge;
    RadioGroup mRgDisposition;
    RadioGroup mRgSex;
    RelativeLayout mRlFinished;

    private RegisterPresenter mRegisterPresenter;
    private LoginPresenter mLoginPresenter;

    private int disposition=0;
    private String gender="male";
    private int age=0;

    public RegisterPerfectInfoFragment() {}
    public static synchronized RegisterPerfectInfoFragment getInstance() {
        if (instance == null) {
            instance = new RegisterPerfectInfoFragment();
        }
        return instance;
    }

    private WeakReference<RegisterActivity> mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= new WeakReference<>((RegisterActivity) context);
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_perfect_info;
    }

    @Override
    public void initView(View view) {
        setToolbarTitle(R.string.txt_perfectInformation);
        setBackIcon(R.drawable.icon_back);
        setBackPrevious();
        mEtInputNickName=view.findViewById(R.id.et_inputNickName);
        mEtInputAge=view.findViewById(R.id.et_inputAge);
        mRgDisposition=view.findViewById(R.id.rg_disposition);
        mRgSex=view.findViewById(R.id.rg_sex);
        mRlFinished=view.findViewById(R.id.rl_finished);

        Log.i(TAG, "initView: PerfectInfo");
        mRegisterPresenter=new RegisterPresenter(this);

        mLoginPresenter=new LoginPresenter(new Login());

        mEtInputAge.setOnClickListener(v -> showWheelView() );

        mRgDisposition.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbtn_Outgoing:{  disposition=0;  break;  }
                case R.id.rbtn_Introverted:{  disposition=1;  break;  }
                case R.id.rbtn_Normal:{  disposition=2;  break;  }
            }
        });

        mRgSex.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbtn_male:{  gender="male";  break;  }
                case R.id.rbtn_female:{  gender="female";  break;  }
            }
        });

        mRlFinished.setOnClickListener(v -> registerUser());
    }

    private void registerUser(){
        String mobile= SharedPreferencesHelper.getInstance().getStringValueByKey(Constant.USER_MOBILE);
        String pwd= SharedPreferencesHelper.getInstance().getStringValueByKey(Constant.USER_PASSWORD);
        String name=mEtInputNickName.getText().toString().trim();
        String ages=mEtInputAge.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mActivity.get(), "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ages)) {
            Toast.makeText(mActivity.get(), "年龄不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mRegisterPresenter == null) {
            mRegisterPresenter=new RegisterPresenter(this);
            return;
        }
        Log.i(TAG, "registerUser: ");
        mRegisterPresenter.checkSmsCode(mobile,pwd,gender,age,disposition,name);
    }

    @Override
    public void onSuccess(BaseBean<RegisterBean> result) {
        Log.i(TAG, "onSuccess: token："+result.getResult().getToken()+"\n info："+result.getResult().getUser().toString());

        if (result.getCode() ==0 && result.getResult().getUser() != null) {
            Log.i(TAG, "注册成功");
            String mobile=SharedPreferencesHelper.getInstance().getStringValueByKey(Constant.USER_MOBILE);
            String pwd=SharedPreferencesHelper.getInstance().getStringValueByKey(Constant.USER_PASSWORD);
            mLoginPresenter.getLogin(mobile,pwd);
        }
        if (result.getCode() != 0){
            Toast.makeText(mActivity.get().getApplicationContext(), result.getError().getMessage().get(0), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailed(Throwable throwable) {
        Log.i(TAG, "onFailed: "+ throwable.getMessage());
        ToastUtils.showShort(throwable.getMessage());
    }

    BottomDialog mBottomDialog;
    View mView;
    @SuppressLint({"SetTextI18n", "CutPasteId", "InflateParams"})
    public void showWheelView(){
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_wheel_choice_age, null);
        WheelView wheelview =  mView.findViewById(R.id.wheel_view);
        TextView tvCancel=mView.findViewById(R.id.tv_cancel);
        TextView tvSure=mView.findViewById(R.id.tv_sure);
        ArrayList<String> list=new ArrayList<>();
        for (int i = 1950; i <= 2009; i++) {
            list.add(i+"年");
        }
        wheelview.setItems(list,list.size()-1);

        wheelview.setOnItemSelectedListener((selectedIndex, item) -> {
            int year=Integer.parseInt(item.substring(0,4));

            Calendar c = Calendar.getInstance();
            int currentYear = c.get(Calendar.YEAR); // 获取当前年份
            age=currentYear-year;
            Log.i(TAG, "showWheelView: "+age);
        });

        tvSure.setOnClickListener(v -> {
            mEtInputAge.setText(age+"岁");
            mBottomDialog.dismiss();
        });

        tvCancel.setOnClickListener(v -> mBottomDialog.dismiss());

        //防止弹出两个窗口
        if (mBottomDialog !=null && mBottomDialog.isShowing()) {
            return;
        }
        mBottomDialog = new BottomDialog(getContext(), R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        mBottomDialog.setContentView(mView);
        mBottomDialog.setCanceledOnTouchOutside(false);
        mBottomDialog.show();
    }

    private class Login implements OnPresenterListener.OnViewListener<BaseBean<LoginBean>>{
        @Override
        public void onSuccess(BaseBean<LoginBean> result) {

            Log.i(TAG, "activity onSuccess: "+result.toString());
            if (result.getCode() == 0 && result.getResult() != null) {
                LoginBean loginBean=result.getResult();
                if (result.getResult().getUser() != null){
                    UserBean bean=new UserBean(null,true,"JWT "+loginBean.getToken(),
                            result.getResult().getUser().getUsername(),
                            result.getResult().getUser().getGender(),
                            result.getResult().getUser().getDiamond(),
                            result.getResult().getUser().getUid(),
                            result.getResult().getUser().getAge(),
                            result.getResult().getUser().getCharacter(),
                            result.getResult().getUser().getRank(),
                            result.getResult().getUser().getClaw_doll_time(),
                            result.getResult().getUser().getGift());
                    DaoInsert.insertUser(bean);
                    SharedPreferencesHelper.getInstance().setBooleanValue(Constant.LOGIN_STATUE,Constant.IsTrue);
                }
                JumpToActivityUtil.jumpNoParams(mActivity.get(), MainActivity.class, true);
            }else {
                if (result.getError() != null){
                    int size=result.getError().getMessage().size();
                    for (int i = 0; i < size; i++) {
                        ToastUtils.showShort(result.getError().getMessage().get(i));
                    }
                }
            }
        }

        @Override
        public void onFailed(Throwable e) {
            Log.i(TAG, "activity  onFailed: "+e.getMessage());
            ToastUtils.showShort(e.getMessage());
        }
    }


    @Override
    public void onDestroyView() {
        Log.e(TAG, "onDestroyView: PerfectInfo");
        if (mRegisterPresenter != null) {
            mRegisterPresenter.detachView();
            mRegisterPresenter=null;
        }
        if (mLoginPresenter != null) {
            mLoginPresenter.detachView();
            mLoginPresenter=null;
        }
        super.onDestroyView();
        mActivity.get().mTransaction.remove(this);
    }

}
