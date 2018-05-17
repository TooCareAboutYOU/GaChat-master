package com.gachat.main.base;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gachat.main.R;
import com.gachat.main.base.broadcastreciver.NetWorkBroadCastReceiver;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 2018/3/8.
 */

public abstract class BaseFragment extends Fragment implements NetWorkBroadCastReceiver.onNetWorkListener {

//    private static final String TAG = "BaseFragment";

    /** rootView是否初始化标志，防止回调函数在rootView为空的时候触发 */
    private boolean hasCreateView;

    /**  当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发  */
    private boolean isFragmentVisible;

    /** onCreateView()里返回的view，修饰为protected,所以子类继承该类时，在onCreateView里必须对该变量进行初始化 */
    private View mParentView;

    protected boolean isVisible; //是否可见
    protected boolean isPrepared=false;  //标识位，标识Fragment已经初始化完成

    private boolean isConnected=false; //网络连接

    private NetWorkBroadCastReceiver mNetWorkReciver;

    protected View getFragmentParentView(){ return mParentView; }
    protected boolean getNetConnectState(){ return isConnected; }
    public abstract int setLayoutResourceID();
    public abstract void initView(View view);
    public void onActivityCreate(Bundle savedInstanceState){}
    public void onVisible(){}
    public void onInVisible(){}


    public Unbinder unbinder;

    public Toolbar getToolbar(){ return mParentView.findViewById(R.id.toolbar_base); }
    public ImageView getBack(){ return mParentView.findViewById(R.id.tootlbar_iv_back); }
    public TextView getToolbarTitle(){ return mParentView.findViewById(R.id.tootlbar_iv_title); }

    public void setToolbarTitle(CharSequence title){
        if (getToolbarTitle() != null) {
            getToolbarTitle().setText(title);
        }else {
            getToolbar().setTitle(title);
            ((AppCompatActivity)getActivity()).setSupportActionBar(getToolbar());
        }
    }

    public void setToolbarTitle(int title){
        if (getToolbarTitle() != null) {
            getToolbarTitle().setText(title);
        }else {
            getToolbar().setTitle(title);
            ((AppCompatActivity)getActivity()).setSupportActionBar(getToolbar());
        }
    }

    public void setBackIcon(int backIcon){
        if (getToolbar() != null) {
            getBack().setImageResource(backIcon);
        }
    }

    /*回退*/
    public void setBackPrevious(){
        if (getToolbar() != null) {
            getBack().setOnClickListener(v -> {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }else {
                    getActivity().finish();
                }
            });
        }
    }


    public void switchFrag(int containerViewId,BaseFragment to){
        getFragmentManager().beginTransaction().replace(containerViewId,to).addToBackStack(null).commit();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Log.i(TAG, "base setUserVisibleHint: ");

        if (mParentView == null) {
            return;
        }
        hasCreateView=true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }

        if (getUserVisibleHint()) {
            isVisible=true;
            onVisible();
        }else {
            isVisible=false;
            onInVisible();
        }


    }

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasCreateView=false;
        isFragmentVisible=false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView=inflater.inflate(setLayoutResourceID(), container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        isPrepared=true;

        mNetWorkReciver=new NetWorkBroadCastReceiver();
//        Log.i(TAG, "base onCreateView: ");
        initView(mParentView);
        return mParentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.i(TAG, "base onActivityCreated: ");
//        if (mParentView != null && isVisible) {
            onActivityCreate(savedInstanceState);
//        }

        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isFragmentVisible=true;
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.i(TAG, "base onActivityCreated: ");
//        if (mParentView != null && isVisible) {
//            onActivityCreate(savedInstanceState);
//        }
    }


    @Override
    public void onStart() {
        super.onStart();
//        Log.i(TAG, "base onStart: ");
    }


    @Override
    public void onResume() {
        super.onResume();
//        Log.i(TAG, "base onResume: ");
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(NetWorkBroadCastReceiver.ACTION);
        mNetWorkReciver.setOnNetWorkListener(this);
        Objects.requireNonNull(getContext()).registerReceiver(mNetWorkReciver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNetWorkReciver != null) {
            Objects.requireNonNull(getContext()).unregisterReceiver(mNetWorkReciver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.i(TAG, "base onDestroyView: ");
        unbinder.unbind();

    }

    @Override
    public void onConnected(boolean isConnect) {
//        Log.i(TAG, "base onConnected: "+isConnect);
        this.isConnected=isConnect;
    }
}
