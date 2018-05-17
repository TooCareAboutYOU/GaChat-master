package com.gachat.network.fastjson;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by admin on 2018/3/13.
 */

public class FastJsonResponseConvert<T> implements Converter<ResponseBody,T> {

    private Type mType=null;


    public FastJsonResponseConvert(Type type) {
        this.mType=type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource bufferedSource= Okio.buffer(value.source());
        String  s=bufferedSource.readUtf8();
        bufferedSource.close();
        return JSON.parseObject(s,mType);
    }
}
