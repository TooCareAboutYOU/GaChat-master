package com.gachat.main.base;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = "BaseDialogFragment";

    protected abstract int setResLayoutId();
    protected abstract void initView(View view);

    private View mView;
    public Unbinder unbinder;
    private boolean isExist=false;

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

    @SuppressLint("CommitTransaction")
    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isExist) {
            super.show(manager, tag);
            isExist = !isExist;
            Log.i(TAG, "show: "+isExist);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (isExist) {
            isExist=false;
            Log.i(TAG, "onDismiss: ");
            super.dismiss();
        }
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
