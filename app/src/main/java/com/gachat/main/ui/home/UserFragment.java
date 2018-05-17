package com.gachat.main.ui.home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.widget.NormalDialog;
import com.gachat.main.R;
import com.gachat.main.base.BaseFragment;
import com.gachat.main.event.ExitEvent;
import com.gachat.main.ui.MainActivity;
import com.gachat.main.ui.login.activity.LoginActivity;
import com.gachat.main.ui.user.AboutUsActivity;
import com.gachat.main.ui.user.DisclaimerActivity;
import com.gachat.main.ui.user.FeedbackActivity;
import com.gachat.main.ui.user.MyDollsActivity;
import com.gachat.main.ui.user.MyNewsActivity;
import com.gachat.main.util.DataCleanManager;
import com.gachat.main.util.JumpToActivityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFragment extends BaseFragment {

    private static final String TAG = "UserFragment";

    RelativeLayout topView;
    ImageView mIvUserLogo;
    TextView mTvAccount;
    TextView mTvDiamonds;
    ListView mListViewMenu;

    private  List<ListMenuBean> list;
    private ListAdapter mListAdapter;

    private MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (MainActivity) context;
    }

    @Override
    public int setLayoutResourceID() {return R.layout.fragment_user; }

   private String cacheSize;

    @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view) {
        Log.i(TAG, "initView: ");
        topView=view.findViewById(R.id.user_bg);
        mIvUserLogo=view.findViewById(R.id.iv_userLogo);
        mTvAccount=view.findViewById(R.id.tv_account);
        mTvDiamonds=view.findViewById(R.id.tv_Diamonds);
        mListViewMenu=view.findViewById(R.id.listview_menu);
        
        
        if (mActivity.UserData() != null) {
            mTvAccount.setText(mActivity.UserData().getUsername());
            mTvDiamonds.setText(mActivity.UserData().getDiamond()+"");
            topView.setBackgroundResource(mActivity.UserData().getGender().equals("male") ? R.drawable.kachat_home_bg_boy : R.drawable.kachat_home_bg_girl);
            mIvUserLogo.setImageResource(mActivity.UserData().getGender().equals("male") ? R.drawable.kachathome_logo_boy : R.drawable.kachathome_logo_girl);
        }

        try {
            cacheSize = DataCleanManager.getTotalCacheSize(Objects.requireNonNull(getActivity()).getApplicationContext());
            Log.i(TAG, "initView: "+cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        list=new ArrayList<>();
        list.add(0,new ListMenuBean(R.drawable.user_icon_baby,R.string.txt_user_MyDolls,R.drawable.user_icon_baby));
        list.add(1,new ListMenuBean(R.drawable.user_icon_mynews,R.string.txt_user_Mynews,R.drawable.user_icon_mynews));
        list.add(2,new ListMenuBean(R.drawable.user_icon_delete,R.string.txt_user_ClearCache,R.drawable.user_icon_delete,cacheSize));
        list.add(3,new ListMenuBean(R.drawable.icon_retroaction,R.string.txt_user_Feedback,R.drawable.user_icon_mynews));
        list.add(4,new ListMenuBean(R.drawable.user_icon_report,R.string.txt_user_Disclaimer,R.drawable.user_icon_report));
        list.add(5,new ListMenuBean(R.drawable.user_icon_about,R.string.txt_user_AboutUs,R.drawable.user_icon_about));
        list.add(6,new ListMenuBean(R.drawable.user_icon_out,R.string.txt_user_Exit,R.drawable.user_icon_out));
        mListAdapter=new ListAdapter();
        mListViewMenu.setAdapter(mListAdapter);

        mListViewMenu.setOnItemClickListener((parent, view1, position, id) -> {
            switch (position) {
                case 0:{  JumpToActivityUtil.jumpNoParams(getActivity(), MyDollsActivity.class,false); break;  }
                case 1:{  JumpToActivityUtil.jumpNoParams(getActivity(), MyNewsActivity.class,false); break; }
                case 2:{  ClearCacheDialog(); break; } //                    startActivity(new Intent(getActivity(),TestActivity.class));
                case 3:{  JumpToActivityUtil.jumpNoParams(getActivity(), FeedbackActivity.class,false); break;  }
                case 4:{  JumpToActivityUtil.jumpNoParams(getActivity(), DisclaimerActivity.class,false); break;  }
                case 5:{  JumpToActivityUtil.jumpNoParams(getActivity(),AboutUsActivity.class,false); break; }
                case 6:{  ExitLoginDialog(); break;  }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            Log.i("isVisible", "onFragmentVisibleChange: UserFragment 显示");
            if (mActivity.UserData() != null) {
                mTvAccount.setText(mActivity.UserData().getUsername());
                mTvDiamonds.setText(mActivity.UserData().getDiamond() + "");
            }
        }else {
            Log.i("isVisible", "onFragmentVisibleChange: UserFragment 隐藏");
        }
    }

    private class ListAdapter extends BaseAdapter {

        static final int LAYOUT_TYPE_ONE=0;
        static final int LAYOUT_TYPE_TWO=1;

        @Override
        public int getCount() {return list.size();}

        @Override
        public Object getItem(int position) {return list.get(position);}

        @Override
        public long getItemId(int position) {return position;}

        @Override
        public int getViewTypeCount() { return 2;}

        @Override
        public int getItemViewType(int position) {
            return position == 2 ? LAYOUT_TYPE_TWO : LAYOUT_TYPE_ONE;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_userfragment_item, null);
                holder.icon = convertView.findViewById(R.id.iv_icon);
                holder.title = convertView.findViewById(R.id.tv_title);
                holder.iconJump = convertView.findViewById(R.id.iv_jump);
                holder.cahcedata = convertView.findViewById(R.id.tv_cache);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setImageResource(list.get(position).icon);
            holder.title.setText(list.get(position).title);

            switch (getItemViewType(position)) {
                case LAYOUT_TYPE_ONE:{
                    holder.iconJump.setVisibility(View.VISIBLE);
                    holder.cahcedata.setVisibility(View.GONE);
                    break;
                }
                case LAYOUT_TYPE_TWO:{
                    if (!TextUtils.isEmpty(list.get(position).cahcedata)) {
                        holder.iconJump.setVisibility(View.GONE);
                        holder.cahcedata.setVisibility(View.VISIBLE);
                        holder.cahcedata.setText(list.get(position).cahcedata);
                    }
                    break;
                }
            }
            return convertView;
        }

        class ViewHolder {
            private ImageView icon;
            private TextView title;
            private ImageView iconJump;
            private TextView cahcedata;
            ViewHolder() {}
        }
    }

    class ListMenuBean{
        private int icon;
        private int title;
        private int iconJump;
        private String cahcedata;

         ListMenuBean(int icon, int title, int iconJump) {
            this.icon = icon;
            this.title = title;
            this.iconJump = iconJump;
        }

         ListMenuBean(int icon, int title, int iconJump, String cahcedata) {
            this.icon = icon;
            this.title = title;
            this.iconJump = iconJump;
            this.cahcedata = cahcedata;
        }

        @Override
        public String toString() {
            final StringBuilder sb;
            sb = new StringBuilder("{");
            sb.append("\"icon\":")
                    .append(icon);
            sb.append(",\"title\":\"")
                    .append(title).append('\"');
            sb.append(",\"iconJump\":")
                    .append(iconJump);
            sb.append(",\"cahcedata\":\"")
                    .append(cahcedata).append('\"');
            sb.append('}');
            return sb.toString();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
        if (list.size() > 0) {
            list.clear();
            list = null;
        }
    }

    private void ClearCacheDialog() {
        final NormalDialog dialog = new NormalDialog(getContext());
        dialog.isTitleShow(true)
                .style(NormalDialog.STYLE_TWO)
                .bgColor(Color.WHITE)
                .cornerRadius(5)
                .title("提示：")
                .titleTextSize(18)
                .titleTextColor(R.color.colorGrayDark)
                .content("确定要清除缓存？")
                .contentGravity(Gravity.CENTER)
                .contentTextSize(18f)
                .contentTextColor(Color.BLACK)
                .dividerColor(Color.parseColor("#8F9BA3"))
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(R.color.colotTabNormal, Color.parseColor("#12B5FF"))
                .widthScale(0.85f)
                .show();

        dialog.setOnBtnClickL(dialog::dismiss, () -> {
            DataCleanManager.clearAllCache(Objects.requireNonNull(getActivity()).getApplicationContext());
            list.set(2,new ListMenuBean(R.drawable.user_icon_delete,R.string.txt_user_ClearCache,R.drawable.user_icon_delete,0+"k"));
            dialog.dismiss();
            Log.i(TAG, "清除成功 ");
            mListAdapter.notifyDataSetChanged();
        });
    }

    private void ExitLoginDialog() {
        final NormalDialog dialog = new NormalDialog(getContext());
        dialog.isTitleShow(true)
                .style(NormalDialog.STYLE_TWO)
                .bgColor(Color.WHITE)
                .cornerRadius(5)
                .title("提示：")
                .titleTextSize(18)
                .titleTextColor(R.color.colorGrayDark)
                .content("确定要提出登录？")
                .contentGravity(Gravity.CENTER)
                .contentTextSize(18f)
                .contentTextColor(Color.BLACK)
                .dividerColor(Color.parseColor("#8F9BA3"))
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(R.color.colotTabNormal, Color.parseColor("#12B5FF"))
                .widthScale(0.85f)
                .show();

        dialog.setOnBtnClickL(dialog::dismiss, () -> {
            ExitEvent.exitLogin();
            JumpToActivityUtil.jumpNoParams(mActivity, LoginActivity.class,true);
            dialog.dismiss();
        });
    }

}

