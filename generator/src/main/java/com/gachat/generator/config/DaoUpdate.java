package com.gachat.generator.config;

import android.util.Log;
import com.gachat.generator.helper.GreenDaoHelper;
import com.gachat.generator.model.UserBean;


public class DaoUpdate {

    private static final String TAG = "UpdateUserData";


//    public static boolean updateUser(int params,int uid){
//        synchronized (DaoUpdate.class) {
//            UserBean userBean = DaoQuery.queryUserbeanByUid(uid);
//            Log.i(TAG, "before 全部数据：" + DaoQuery.queryUserlist());
//            if (userBean != null) {
//                userBean.setAge(params);
//                GreenDaoHelper.getInstance().writeUser().update(userBean);
//                Log.i(TAG, "after 全部数据：" + DaoQuery.queryUserlist());
//                return true;
//            }
//            return false;
//        }
//    }

    public static boolean updateUser(UserBean bean,int uid){
        synchronized (DaoUpdate.class) {
//            GreenDaoHelper.close();
//            int size = DaoQuery.queryUserListByUid(uid).size();
            Log.i("UpdateUserData", "before 全部数据--->>>" + DaoQuery.queryUserlist());
            if (bean != null) {
                GreenDaoHelper.getInstance().writeUser().update(bean);
                Log.i("UpdateUserData", "after 全部数--->>>" + DaoQuery.queryUserlist());
                return true;
            }

            return false;
        }
    }

}
