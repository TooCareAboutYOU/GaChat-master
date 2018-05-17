package com.gachat.generator.config;

import android.util.Log;

import com.gachat.generator.gen.UserBeanDao;
import com.gachat.generator.helper.GreenDaoHelper;
import com.gachat.generator.model.UserBean;

import java.util.List;

/**
 * Created by admin on 2018/3/12.
 */

public class DaoQuery {

    private static final String TAG = "DaoQuery";

    /**
     * @param uid
     * @return
     */
    public static List<UserBean> queryUserListByUid(int uid){
        synchronized (DaoQuery.class) {
            List<UserBean> userBeans = GreenDaoHelper.getInstance().writeUser().queryBuilder().where(UserBeanDao.Properties.Uid.lt(uid)).list();
            Log.i(TAG, "条数: " + userBeans.size());
            if (userBeans != null && userBeans.size() > 0) {
                for (UserBean bean : userBeans) {
                    Log.i(TAG, "数据: " + bean.toString());
                }
                return userBeans;
            }
            return null;
        }
    }

    public static UserBean queryUserbeanByUid(int uid){
        synchronized (DaoQuery.class) {
            UserBean userBeans = GreenDaoHelper.getInstance().writeUser().queryBuilder().where(UserBeanDao.Properties.Uid.eq(uid)).build().unique();
            if (userBeans != null) {
                Log.i("UpdateUserData", "1 精确查询--->>>>>>" + userBeans.toString());
                return userBeans;
            }
            return null;
        }
    }

    public static UserBean queryUserbean(){
        synchronized (DaoQuery.class) {
            List<UserBean> userBeans = GreenDaoHelper.getInstance().writeUser().queryBuilder().list();
            if (userBeans != null && userBeans.size() > 0) {
                return userBeans.get(0);
            }
            return null;
        }
    }

    public static int queryUserlistSize(){
        synchronized (DaoQuery.class) {
            int size=GreenDaoHelper.getInstance().writeUser().queryBuilder().list().size();
            return size;
        }
    }

    public static List<UserBean> queryUserlist(){
        synchronized (DaoQuery.class) {
            List<UserBean> userBeans = GreenDaoHelper.getInstance().writeUser().queryBuilder().list();
            Log.i(TAG, "条数: " + userBeans.size());
            if (userBeans != null && userBeans.size() > 0) {
                for (UserBean bean : userBeans) {
                    Log.i(TAG, "数据: " + bean.toString());
                }
                return userBeans;
            }
            return null;
        }
    }
}
