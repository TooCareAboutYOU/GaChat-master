package com.gachat.main.base;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 2018/4/12.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected abstract int setResLayoutId();
    protected abstract void initView(View view);

    private View mView;
    public Unbinder unbinder;

    public View getParentView(){
        if (mView != null){
            return mView;
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView= inflater.inflate(setResLayoutId(),null);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        unbinder = ButterKnife.bind(this, mView);
        initView(mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        if (mView != null) {
            mView=null;
        }
        super.onDestroyView();
        unbinder.unbind();

    }

}
