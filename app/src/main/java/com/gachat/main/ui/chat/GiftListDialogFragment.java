package com.gachat.main.ui.chat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.dialog.widget.NormalDialog;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseDialogFragment;
import com.gachat.main.beans.GiftListBean;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.SendGiftPresenter;

import java.util.List;
import java.util.Objects;

/**
 * 点赞
 */

@SuppressLint("ValidFragment")
public class GiftListDialogFragment extends BaseDialogFragment {

    private static final String TAG = "GiftListDialogFragment";

    @SuppressLint("StaticFieldLeak")
    private List<GiftListBean.AssetsBean> mList;
    private SendGiftPresenter giftPresenter;


    @SuppressLint("StaticFieldLeak")
    private static GiftListDialogFragment instance;
    @SuppressLint("ValidFragment")
    public GiftListDialogFragment(List<GiftListBean.AssetsBean> list){
        this.mList=list;
    }
    public static GiftListDialogFragment getInstance(List<GiftListBean.AssetsBean> data){
        if(instance==null){
            instance=new GiftListDialogFragment(data);
        }
        return instance;
    }


    @Override
    public int setResLayoutId() {
        return R.layout.fragment_gift_list;
    }

    @Override
    public void initView(View view) {
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager manager=new GridLayoutManager(getContext(), 1);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new GiftListAdapter());
        view.findViewById(R.id.view_dismiss).setOnClickListener(v -> dismiss());

        giftPresenter=new SendGiftPresenter(new SendGiftMethod());

    }

    @Override
    public void onStart() {
        super.onStart();
        Window window=getDialog().getWindow();
        WindowManager.LayoutParams layoutParams= Objects.requireNonNull(window).getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setDimAmount(0.5f);
        window.setAttributes(layoutParams);
    }

    private class GiftListAdapter extends RecyclerView.Adapter<GiftListAdapter.GiftListViewHolder> {

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public GiftListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GiftListViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_fragment_gift_list, null));
        }

        @Override
        public void onBindViewHolder(@NonNull GiftListViewHolder holder, int position) {
            if (!TextUtils.isEmpty(mList.get(position).getItem().getImage_url())) {
                holder.mSdvPic.setImageURI(Uri.parse(mList.get(position).getItem().getImage_url()));
            }
            if (!TextUtils.isEmpty(mList.get(position).getItem().getName())){
                holder.mTvName.setText(mList.get(position).getItem().getName());
            }

            holder.itemView.setOnClickListener(v -> {
                Log.w(TAG, "点击,position= "+position+
                        "\tname="+mList.get(position).getItem().getName()+
                        "\tUser_asset_id："+mList.get(position).getUser_asset_id());

                SendGiftDialog(Constant.getCalleeId(),mList.get(position).getUser_asset_id());
//                giftPresenter.sendGift(Constant.getCalleeId(),mList.get(position).getUser_asset_id());
            });
        }

        @Override
        public int getItemCount() {   return mList.size() > 0 ? mList.size() : 0;   }

        class GiftListViewHolder extends RecyclerView.ViewHolder {
            TextView mTvName;
            SimpleDraweeView mSdvPic;
            TextView mTvDate;

            GiftListViewHolder(View itemView) {
                super(itemView);
                mTvName=itemView.findViewById(R.id.tv_name);
                mSdvPic=itemView.findViewById(R.id.sdv_pic);
                mTvDate=itemView.findViewById(R.id.tv_date);
            }
        }

    }

    private void SendGiftDialog(long toUserId,int dollId) {
        final NormalDialog dialog = new NormalDialog(getContext());
        dialog.isTitleShow(true)
                .style(NormalDialog.STYLE_TWO)
                .bgColor(Color.WHITE)
                .cornerRadius(5)
                .title("提示：")
                .titleTextSize(18)
                .titleTextColor(R.color.colorGrayDark)
                .content("是否送出？")
                .contentGravity(Gravity.CENTER)
                .contentTextSize(18f)
                .contentTextColor(Color.BLACK)
                .dividerColor(Color.parseColor("#8F9BA3"))
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(R.color.colotTabNormal, Color.parseColor("#12B5FF"))
                .widthScale(0.85f)
                .show();

        dialog.setOnBtnClickL(dialog::dismiss, () -> {
            Log.i("SendGiftModel", "SendGiftDialog: "+toUserId+"\t\t"+dollId);
            giftPresenter.sendGift(toUserId, dollId);
            dialog.dismiss();
            dismiss();
        });
    }

    private class SendGiftMethod implements OnPresenterListener.OnViewListener<BaseBean<MessageBean>>{
        @Override
        public void onSuccess(BaseBean<MessageBean> result) {
            Log.i("SendGiftModel", "onSuccess: "+result.toString());
            if (result.getCode() == 0) {
                if (result.getResult() != null ) {
//                    SendGiftDialogCallBack(result.getResult().getMessage());
                    ToastUtils.showShort(result.getResult().getMessage());
                }
            }else {
                SendGiftDialogCallBack("赠送失败!!!");
                if (result.getError() != null && result.getError().getMessage().size() > 0) {
                    for (String s : result.getError().getMessage()) {
                        ToastUtils.showShort(s);
                    }
                }
            }
        }

        @Override
        public void onFailed(Throwable throwable) {
            Log.i("SendGiftModel", "onFailed: "+throwable);
            ToastUtils.showShort(throwable.getMessage());
        }
    }

    private void SendGiftDialogCallBack(String msg) {
        final NormalDialog dialog = new NormalDialog(getContext());
        dialog.isTitleShow(true)
                .style(NormalDialog.STYLE_ONE)
                .bgColor(Color.WHITE)
                .cornerRadius(5)
                .title("提示：")
                .titleTextSize(18)
                .titleTextColor(R.color.colorGrayDark)
                .content(msg)
                .contentGravity(Gravity.CENTER)
                .contentTextSize(18f)
                .contentTextColor(Color.BLACK)
                .dividerColor(Color.parseColor("#8F9BA3"))
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(R.color.colotTabNormal, Color.parseColor("#12B5FF"))
                .widthScale(0.85f)
                .show();
        dialog.setOnBtnClickL(this::dismiss);
    }


    @Override
    public void onDestroyView() {
        if (giftPresenter != null) {
            giftPresenter.detachView();
            giftPresenter=null;
        }

        if (mList != null) {
            mList.clear();
            mList=null;
        }
        super.onDestroyView();
    }
}
