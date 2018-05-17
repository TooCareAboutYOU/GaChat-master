package com.gachat.generator.config;


import com.gachat.generator.helper.GreenDaoHelper;
import com.gachat.generator.model.UserBean;

/**
 * Created by admin on 2018/3/12.
 */

public class DaoInsert {

    private static final String TAG = "DaoInsert";

    /**
     * 用例demo
     * @param userBean
     */
    public static void insertUser(UserBean userBean){
        synchronized (DaoInsert.class) {
            if (userBean != null && GreenDaoHelper.getInstance().readUser() != null) {
                GreenDaoHelper.getInstance().readUser().insert(userBean);
            } else {
                throw new NullPointerException("the userBean or GreenDaoHelper.readUser() is null!");
            }
        }
    }
}
