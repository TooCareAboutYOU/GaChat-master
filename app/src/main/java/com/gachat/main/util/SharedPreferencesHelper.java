package com.gachat.main.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

public class SharedPreferencesHelper {

    private static SharedPreferences mSharedPreferences;

    private volatile static SharedPreferencesHelper singleton;

    //Application初始化
    public static void init(Context context,String name,int mode){
        mSharedPreferences=context.getSharedPreferences(name,mode);
    }

    public static synchronized SharedPreferencesHelper getInstance() {
        if (singleton == null) {
             synchronized (SharedPreferencesHelper.class) {
                  if (singleton == null) {
                      singleton = new SharedPreferencesHelper();
                  }
             }
         }
        return singleton;
    }


    public String getStringValueByKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            return mSharedPreferences.getString(key,"");
        }else {
            throw new NullPointerException("the key is null");
        }
    }

    public boolean getBooleanValueByKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            return mSharedPreferences.getBoolean(key,false);
        }else {
            throw new NullPointerException("the key is null");
        }
    }

    public float getFloatValueByKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            return mSharedPreferences.getFloat(key,0.0f);
        }else {
            throw new NullPointerException("the key is null");
        }
    }

    public int getIntValueByKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            return mSharedPreferences.getInt(key,-1);
        }else {
            throw new NullPointerException("the key is null");
        }
    }

    public long getLongValueByKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            return mSharedPreferences.getLong(key,-1);
        }else {
            throw new NullPointerException("the key is null");
        }
    }

    public Set<String> getStringSetByKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            return mSharedPreferences.getStringSet(key,null);
        }else {
            throw new NullPointerException("the key is null");
        }
    }




    public void setStringValue(String key,String value){
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor mEditor=mSharedPreferences.edit();
            mEditor.putString(key,value);
            mEditor.commit();
        }else {
            throw new NullPointerException("the key is null");
        }

    }

    public void setBooleanValue(String key,boolean value){
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor  mEditor=mSharedPreferences.edit();
            mEditor.putBoolean(key,value);
            mEditor.commit();
        }else {
            throw new NullPointerException("the key is null");
        }

    }

    public void setIntValue(String key,int value){
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor mEditor=mSharedPreferences.edit();
            mEditor.putInt(key,value);
            mEditor.commit();
        }else {
            throw new NullPointerException("the key is null");
        }

    }

    public void setFloatValue(String key,float value){
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor  mEditor=mSharedPreferences.edit();
            mEditor.putFloat(key,value);
            mEditor.commit();
        }else {
            throw new NullPointerException("the key is null");
        }
    }

    public void setLongValue(String key,long value){
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor mEditor=mSharedPreferences.edit();
            mEditor.putLong(key,value);
            mEditor.commit();
        }else {
            throw new NullPointerException("the key is null");
        }

    }

    public void setStringSetValue(String key,Set<String> value){
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor mEditor=mSharedPreferences.edit();
            mEditor.putStringSet(key,value);
            mEditor.commit();
        }else {
            throw new NullPointerException("the key is null");
        }

    }

    public void setObject(String key,Object obj){
        if (!TextUtils.isEmpty(key)) {
            ByteArrayOutputStream arrayStream=new ByteArrayOutputStream();
            ObjectOutputStream objectStream=null;
            try {
                objectStream=new ObjectOutputStream(arrayStream);
                objectStream.writeObject(obj);
                String obojectVal=new String(Base64.encode(arrayStream.toByteArray(),Base64.DEFAULT));
                SharedPreferences.Editor mEditor=mSharedPreferences.edit();
                mEditor.putString(key,obojectVal);
                mEditor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (arrayStream!=null) {
                        arrayStream.close();
                    }
                    if (objectStream!=null) {
                        objectStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            throw new NullPointerException("the key is null");
        }
    }


    public <T>T getObject(String key,Class<T> clazz){
        if (!TextUtils.isEmpty(key)) {
            if (mSharedPreferences.contains(key)) {
                String objectVal=mSharedPreferences.getString(key,null);
                byte[] buffer=Base64.decode(objectVal,Base64.DEFAULT);
                ByteArrayInputStream arrayInputStream=new ByteArrayInputStream(buffer);
                ObjectInputStream objectInputStream=null;
                try {
                    objectInputStream=new ObjectInputStream(arrayInputStream);
                    T t= (T) objectInputStream.readObject();
                    return t;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (arrayInputStream!=null) {
                            arrayInputStream.close();
                        }
                        if (objectInputStream!=null) {
                            objectInputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }else {
            throw new NullPointerException("the key is null");
        }
    }

    /**
     * 根据key和预期的value类型获取value的值
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>T getValue(String key,Class<T> clazz){
        if (!TextUtils.isEmpty(key)) {
            T t;
            try {
                t=clazz.newInstance();
                if (t instanceof  Integer){
                    return (T) Integer.valueOf(mSharedPreferences.getInt(key,0));
                }else if (t instanceof  String){
                    return (T) String.valueOf(mSharedPreferences.getString(key,""));
                }else if (t instanceof  Boolean){
                    return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key,false));
                }else if (t instanceof  Long){
                    return (T) Long.valueOf(mSharedPreferences.getLong(key,0L));
                }else if (t instanceof  Float){
                    return (T) Float.valueOf(mSharedPreferences.getFloat(key,0L));
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
                Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
            }
            Log.e("system", "无法找到" + key + "对应的值");
            return null;
        }
        throw new NullPointerException("the key is null");
    }



    public void removeByKey(String key){
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor mEditor=mSharedPreferences.edit();
            mEditor=mSharedPreferences.edit();
            mEditor.remove(key);
            mEditor.commit();
        }else {
            throw new NullPointerException("the key is null");
        }

    }

    public void clear(){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor=mSharedPreferences.edit();
        mEditor.clear();
        mEditor.commit();
    }
}
