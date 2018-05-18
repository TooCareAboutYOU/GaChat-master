package com.gachat.main.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dnion.VAChatAPI;
import com.gachat.main.R;
import com.gachat.main.base.BaseDialogFragment;

import java.lang.ref.WeakReference;

/**
 * 聊天评价
 */

public class CommentDialogFragment extends BaseDialogFragment {

    private TextView mTextView;
    private WeakReference<ChatRoomActivity> mActivity;

    @SuppressLint("StaticFieldLeak")
    private static CommentDialogFragment instance;
    public CommentDialogFragment (){ }
    public static synchronized CommentDialogFragment getInstance() {
         if (instance == null) {
             instance = new CommentDialogFragment();
         }
     return instance;
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void show(FragmentManager manager, String tag) {
        Log.i("DialogFragment", "show: ");
//        if (mActivity != null && (getDialog() != null || !getDialog().isShowing())) {
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.add(this,tag);
            transaction.commitNowAllowingStateLoss();
            transaction.show(this);
//        }else {
//            super.show(manager, tag);
//        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= new WeakReference<>((ChatRoomActivity) context);
    }

    @Override
    public int setResLayoutId() {
        return R.layout.chat_comment_dialog;
    }

    @Override
    public void initView(View view) {
        mTextView=view.findViewById(R.id.tv_gotoLiked);
        mTextView.setOnClickListener(v -> {
            mActivity.get().showLikedDialogFragment();
            VAChatAPI.getInstance().rateChat("0");
            dismiss();
        });

        view.findViewById(R.id.tv_nervermeet).setOnClickListener(v -> {
            VAChatAPI.getInstance().rateChat("1");
            dismiss();
        });

        view.findViewById(R.id.cancel).setOnClickListener(v -> {
            VAChatAPI.getInstance().rateChat("2");
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        mTextView = null;
        super.onDestroyView();
    }
}
