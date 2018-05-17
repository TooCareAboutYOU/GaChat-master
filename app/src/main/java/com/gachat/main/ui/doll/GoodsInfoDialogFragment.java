package com.gachat.main.ui.doll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gachat.main.R;
import com.gachat.main.base.BaseDialogFragment;

import java.lang.ref.WeakReference;

/**
 * 产品详情
 */

public class GoodsInfoDialogFragment extends BaseDialogFragment {

    @SuppressLint("StaticFieldLeak")
    private static GoodsInfoDialogFragment instance;
    public GoodsInfoDialogFragment (){}
    public static synchronized GoodsInfoDialogFragment getInstance() {
         if (instance == null) {
             instance = new GoodsInfoDialogFragment();
         }
     return instance;
    }

    private WeakReference<DollRoomActivity> mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=new WeakReference<>((DollRoomActivity) context);
    }

    @Override
    public int setResLayoutId() {
        return R.layout.doll_goodsinfo_dialog;
    }

    @Override
    public void initView(View view) {

        view.findViewById(R.id.cancel).setOnClickListener(v -> dismiss());

        Bundle bundle=mActivity.get().getIntent().getExtras();
        if (bundle == null)   return;

        String name=bundle.getString(DollRoomActivity.GOODS_NAME);
        String desc=bundle.getString(DollRoomActivity.GOODS_DESC);

        TextView tvName=view.findViewById(R.id.tv_goods_name);
        if (!TextUtils.isEmpty(name)) {
            tvName.setText(name);
        }

        SimpleDraweeView sdvImg=view.findViewById(R.id.img_goods_img);
        sdvImg.setImageURI(Uri.parse(bundle.getString(DollRoomActivity.GOODS_IMG)));

        TextView tvDesc=view.findViewById(R.id.tv_desc);
        if (!TextUtils.isEmpty(desc)) {
            tvDesc.setText(desc);
        }
    }
}
