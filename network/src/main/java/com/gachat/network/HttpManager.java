package com.gachat.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.facebook.stetho.Stetho;
import com.gachat.network.fastjson.FastJsonConvertFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 /**
 * 登录
 *  private static UserService mUserService  = HttpManager.getInstance().create(UserService.class);
 * public static Subscription getLogin(String mobile, String pwd, Observer<BaseBean<LoginBean>> observer){
 *       return setSubscribe(mUserService.getLoginImpl(mobile,pwd),observer);
 *}
 */

public class HttpManager {

    private static final String TAG = "HttpManager";
    private volatile static HttpManager singleton;

    public static synchronized HttpManager getInstance() {
        if (singleton == null) {
             synchronized (HttpManager.class) {
                  if (singleton == null) {
                      singleton = new HttpManager();
                  }
             }
         }
        return singleton;
    }


    private static final long DEFAULT_TIME_OUT=10;  //超时时间
    private static final long DEFAULT_WRITE_TIME_OUT=10;  //超时时间
    private static final long DEFAULT_READ_TIME_OUT=10;  //超时时间

    private static Retrofit mRetrofit;


    public static void init(Context context,String baseUrl) {
        //chrome调试
        //  http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0417/2737.html
        Stetho.newInitializerBuilder(context)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build();

        // 带持久化
        //方式一
        // CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
        // builder.cookieJar(new JavaNetCookieJar(cookieHandler));

//        方式二
//        ClearableCookieJar cookieJar = new PersistentCookieJar(
//                new SetCookieCache(), new SharedPrefsCookiePersistor(context));
//                client.cookieJar(cookieJar)


//        Gson mGson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                Log.i(TAG, "log: "+message.toString());
            }
        });
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder mOkHttpClient=new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_WRITE_TIME_OUT,TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(mHttpLoggingInterceptor)
                    .addInterceptor(new HttpHeaderInterceptor());

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient.build())
//                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addConverterFactory(FastJsonConvertFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public <T>T create(Class<T> service) {  return mRetrofit.create(service); }


    protected static <T> Subscription setSubscribe(Observable<T> observable, Observer<T> observer){
        return observable
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    /**
     * 网络请求公共头信息插入器
     */
    private static class HttpHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original=chain.request();
            Request request=original.newBuilder()
//                    .header("Allow","POST,OPTION S")
                    .header("Content-type","application/json")
                    .header("Connection","keep-alive")
                    .header("Server","gunicorn/19.7.1")
//                    .header("Vary","Accept")
//                    .header("Allow","GET, PUT, HEAD, OPTIONS")
//                    .header("Vary","Accept,Cookie")
                    .method(original.method(),original.body())
                    .build();
            return chain.proceed(request);
        }
    }

//    private class HttpHeaderInterceptor implements Interceptor{
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request original=chain.request();
//            Request request=original.newBuilder()
//                    .header("User-Agent","Adndroid,xxx")
//                    .header("Accept","application/json")
//                    .header("Content-type","application/json")
//                    .header("Vary","Accept")
//                    .method(original.method(),original.body())
//                    .build();
//            return chain.proceed(request);
//        }
//    }


    /**   公共参数  */
    private class CommonParamsInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request=chain.request();
            if (request.method().equals("GET")) {
                HttpUrl httpUrl=request.url().newBuilder()
                        .addQueryParameter("version","xxx")
                        .addQueryParameter("device","Adnroid")
                        .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()))
                        .build();
                request=request.newBuilder().url(httpUrl).build();
            }else if (request.method().equals("POST")) {
                if (request.body() instanceof FormBody) {
                    FormBody.Builder bodyBuilder=new FormBody.Builder();
                    FormBody formBody= (FormBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.addEncoded(formBody.encodedName(i),formBody.encodedValue(i));
                    }
                    formBody=bodyBuilder
                            .addEncoded("version","xxx")
                            .addEncoded("device","Adnroid")
                            .addEncoded("timestamp", String.valueOf(System.currentTimeMillis()))
                            .build();
                    request=request.newBuilder().post(formBody).build();
                }

            }
            return chain.proceed(request);
        }
    }

    /**  缓存策略  */
    private class HttpCacheInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request=chain.request();
            //无网络时，始终使用本地cache
            if (!NetworkUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response=chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                //有网络时，设置缓存过期的时间为0小时
                int maxAge=0;
                response.newBuilder()
                        .header("Cache-Control","public,max-age="+maxAge)
                        .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清楚下面无法生效
                        .build();
            }else {
                //无网络时，设置缓存过期超时时间为4周
                int maxStale=60 * 60 * 24 *28;
                response.newBuilder()
                        .header("Cache-Control","public,only-if-cached,max-stale="+maxStale)
                        .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清楚下面无法生效
                        .build();
            }

            return response;
        }
    }

}
