package com.gachat.generator.helper;

import android.content.Context;
import android.text.TextUtils;

import com.gachat.generator.gen.DaoMaster;
import com.gachat.generator.gen.DaoSession;
import com.gachat.generator.gen.UserBeanDao;

import org.greenrobot.greendao.database.Database;


public class GreenDaoHelper {

    private static final String TAG = "GreenDaoHelper";

    private static Context sContext;
    private static String dbName;
    private static String dbPassword;

    private static DaoMaster.DevOpenHelper sHelper;
//    private static SQLiteDatabase db;
    private static DaoMaster sDaoMaster;
    private static Database sDatabase;
    private static DaoSession sDaoSession;


    private volatile static GreenDaoHelper singleton;
    private GreenDaoHelper (){
        if (sHelper == null) {
            sHelper=new DaoMaster.DevOpenHelper(sContext,dbName,null);
        }
    }
    public static synchronized GreenDaoHelper getInstance() {
        if (singleton == null) {
             synchronized (GreenDaoHelper.class) {
                  if (singleton == null) {
                      singleton = new GreenDaoHelper();
                  }
             }
         }
        return singleton;
    }

    public static void init(Context context,String name,String pwd){
        sContext=context;
        dbName=name;
        dbPassword=pwd;
        if (!TextUtils.isEmpty(dbName)) {
            sHelper=new DaoMaster.DevOpenHelper(context,dbName,null);
        }
    }


    /****************************************** User  *******************************************************/

    public UserBeanDao writeUser(){
//        sDatabase=sHelper.getEncryptedWritableDb(dbPassword);
        sDatabase=sHelper.getWritableDb();

        sDaoMaster=new DaoMaster(sDatabase);
        sDaoSession=sDaoMaster.newSession();
        if (sDaoSession.getUserBeanDao() != null) {
            return sDaoSession.getUserBeanDao();
        }
        return null;
    }

    public UserBeanDao readUser(){
//        sDatabase=sHelper.getEncryptedReadableDb(dbPassword);
        sDatabase=sHelper.getReadableDb();

        sDaoMaster=new DaoMaster(sDatabase);
        sDaoSession=sDaoMaster.newSession();
        if (sDaoSession.getUserBeanDao() != null) {
            return sDaoSession.getUserBeanDao();
        }
        return null;
    }


    public static void close(){
        if (sDatabase != null) {
            sDatabase.close();
        }
    }
}
