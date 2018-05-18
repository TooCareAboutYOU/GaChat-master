package com.gachat.main.api;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.CheckSmsCodeBean;
import com.gachat.main.beans.DollBannerBean;
import com.gachat.main.beans.DollListBean;
import com.gachat.main.beans.FeedBackBean;
import com.gachat.main.beans.GiftListBean;
import com.gachat.main.beans.LoginBean;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.beans.NewsBean;
import com.gachat.main.beans.RechargeListBean;
import com.gachat.main.beans.RegisterBean;
import com.gachat.main.beans.SmsCodeBean;
import com.gachat.main.beans.UserBean;
import com.gachat.main.mvp.models.UpdateUserData;
import com.gachat.main.util.SharedPreferencesHelper;
import com.gachat.network.HttpManager;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.Subscription;


public class UserAPI extends HttpManager {

    public static final String TAG = "UserAPI";

    private static UserService mUserService  = HttpManager.getInstance().create(UserService.class);

    public interface UserService{

        @FormUrlEncoded
        @POST("/captcha/register")
        Observable<BaseBean<SmsCodeBean>> getSmsCodeImpl(@Field("mobile") String mobile);

        @FormUrlEncoded
        @POST("/captcha/reset")
        Observable<BaseBean<SmsCodeBean>> getSmsCodeResetPwdImpl(@Field("mobile") String mobile);

        @FormUrlEncoded
        @POST("/password/reset")
        Observable<BaseBean<CheckSmsCodeBean>> resetPwdImpl(@Field("mobile") String mobile,
                                                                @Field("captcha") String captcha,
                                                                @Field("password") String password);

        @FormUrlEncoded
        @POST("/captcha/verify")
        Observable<BaseBean<CheckSmsCodeBean>> checkSmsCodeImpl(@Field("mobile") String mobile,
                                                                @Field("captcha") String captcha,
                                                                @Field("password") String password);

        @FormUrlEncoded
        @POST("/users")
        Observable<BaseBean<RegisterBean>> getRegisterImpl(@Field("mobile") String mobile,
                                                         @Field("password") String password,
                                                         @Field("gender") String gender,
                                                         @Field("age") int age,
                                                         @Field("character") int character,  //性格
                                                         @Field("username") String username);
        @FormUrlEncoded
        @POST("/login")
        Observable<BaseBean<LoginBean>> getLoginImpl(@Field("mobile") String mobile, @Field("password") String password);


        @GET("/users/{uid}")
        Observable<BaseBean<UserBean>> getUserData(@Header("Authorization") String authorization, @Path("uid") int uid);

        //获取成功 只有diamond字段
        @GET("/users/{uid}/diamonds")
        Observable<BaseBean<UserBean>> getUserDiamonds(@Header("Authorization") String authorization, @Path("uid") int uid);

        @FormUrlEncoded
        @POST("/feedbacks")
        Observable<BaseBean<FeedBackBean>> sendFeedBacks(@Header("Authorization") String authorization ,@Field("content") String content,@Field("mobile") String mobile);


        @GET("/users/{uid}/assets")
        Observable<BaseBean<GiftListBean>> getUserGiftlist(@Header("Authorization") String authorization ,@Path("uid") int uid, @Query("query") String query);

        @FormUrlEncoded
        @POST("/rooms/{room_id}/reports")
        Observable<BaseBean<MessageBean>> sendRepair(@Header("Authorization") String authorization,@Path("room_id") int room_id,@Field("reason") int reason);

        @GET("/banners")
        Observable<BaseBean<DollBannerBean>> getDollBanners();

        @GET("/rooms")
        Observable<BaseBean<DollListBean>> getDollList(@Query("page") int page);

        @GET("/users/{uid}/gifts")
        Observable<BaseBean<NewsBean>> getMyNews(@Header("Authorization") String authorization ,@Path("uid") int uid);

        @GET("/charge/goods")
        Observable<BaseBean<RechargeListBean>> getRechargePriceList();

        @FormUrlEncoded
        @POST("/express")
        Observable<BaseBean<MessageBean>> sendGoods(
                @Header("Authorization") String authorization,
                @Field("username") String username,
                @Field("mobile") String mobile,
                @Field("address") String address,
                @Field("user_asset") String user_asset);

        @FormUrlEncoded
        @POST("/gifts")
        Observable<BaseBean<MessageBean>> sendGiftToUser(@Header("Authorization") String authorization,
                                                    @Field("user_to") long toUserId,
                                                    @Field("user_from_asset") int dollId);

        @FormUrlEncoded
        @POST("/reports")
        Observable<BaseBean<MessageBean>> sendReports(@Header("Authorization") String authorization,
                                                      @Field("user_to") long toUserId,
                                                      @Field("reason") int reason);
    }

    /******************************************************  优美的分割线  *****************************************************************/

    //登录
    public static Subscription getLogin(String mobile, String pwd, Observer<BaseBean<LoginBean>> observer){
        return setSubscribe(mUserService.getLoginImpl(mobile,pwd),observer);
    }

    //获取验证码
    public static Subscription getSmsCode(String mobile, Observer<BaseBean<SmsCodeBean>> observer){
        return setSubscribe(mUserService.getSmsCodeImpl(mobile),observer);
    }

    //验证验证码
    public static Subscription checkSmsCode(String mobile,String capcha,String password,Observer<BaseBean<CheckSmsCodeBean>> observer){
        return setSubscribe(mUserService.checkSmsCodeImpl(mobile,capcha,password),observer);
    }

    //注册
    public static Subscription getRegister(String mobile,String password,String gender,int age,int character,String username,Observer<BaseBean<RegisterBean>> observer){
        return setSubscribe(mUserService.getRegisterImpl(mobile, password, gender, age, character, username),observer);
    }

    //重置密码获取验证码
    public static Subscription getSmsCodeResetPwd(String mobile,Observer<BaseBean<SmsCodeBean>> observer){
        return setSubscribe(mUserService.getSmsCodeResetPwdImpl(mobile),observer);
    }

    //重置密码
    public static Subscription resetPassword(String mobile,String capcha,String password,Observer<BaseBean<CheckSmsCodeBean>> observer){
        return setSubscribe(mUserService.resetPwdImpl(mobile,capcha,password),observer);
    }

    private static String token(){
        return SharedPreferencesHelper.getInstance().getStringValueByKey(UpdateUserData.TOKEN);
//        return Objects.requireNonNull(DaoQuery.queryUserbean()).getToken();
    }
    private static int uid() {
        return SharedPreferencesHelper.getInstance().getIntValueByKey(UpdateUserData.UID);
//        return Objects.requireNonNull(DaoQuery.queryUserbean()).getUid();
//    }
    }

    private static boolean isLogin() {  return SharedPreferencesHelper.getInstance().getBooleanValueByKey(UpdateUserData.IS_LOGIN);  }
    //获取用户信息
    public static Subscription getUserData(Observer<BaseBean<UserBean>> observer){
//        if (DaoQuery.queryUserlistSize() > 0) {
        if(isLogin()){
            Log.i("UpdateUserData", "访问网络更新用户信息 getUserData: ");
            return setSubscribe(mUserService.getUserData(token(), uid()),observer);
        }
        return null;
    }

    //反馈意见
    public static Subscription sendFeedBacks(String content,String mobile,Observer<BaseBean<FeedBackBean>> observer){
//        if (DaoQuery.queryUserlistSize() > 0) {
        if(isLogin()){
            return setSubscribe(mUserService.sendFeedBacks(token(),content, mobile),observer);
        }
        return null;
    }

    //查询礼物列表 getUserGiftlist
    public static Subscription getUserGiftlist(QueryType type,Observer<BaseBean<GiftListBean>> observer){
//        if (DaoQuery.queryUserlistSize() > 0) {
        if(isLogin()){
            String querytype;
            switch (type) {
                case ALL: {  querytype="all";  break;  }
                case HAVE:{  querytype="have";  break;  }
                case LOSE:{  querytype="lose";  break;  }
                case NONE:{  querytype="";  break;  }
                default: { querytype=""; break; }
            }
            return setSubscribe(mUserService.getUserGiftlist(token(),uid(),querytype),observer);
        }
        return null;
    }

    //报修
    public static Subscription sendRepair(int roomId,int reason,Observer<BaseBean<MessageBean>> observer){
        return setSubscribe(mUserService.sendRepair(token(),roomId,reason),observer);
    }
    // 获取用户钻石数
    public static Subscription getUserDiamonds(Observer<BaseBean<UserBean>> observer){
        Log.i(TAG, "getUserDiamonds: "+token()+"\n"+uid());
        return setSubscribe(mUserService.getUserDiamonds(token(),uid()),observer);
    }

    //获取抓娃娃 banner数据列表
    public static Subscription getDollBanners(Observer<BaseBean<DollBannerBean>> observer){
        return setSubscribe(mUserService.getDollBanners(),observer);
    }

    //获取抓娃娃 banner底部数据列表
    public static Subscription getDollList(int page,Observer<BaseBean<DollListBean>> observer){
        return setSubscribe(mUserService.getDollList(page),observer);
    }

    //我的消息 getMyNews
    public static Subscription getMyNews(Observer<BaseBean<NewsBean>> observer){
        return setSubscribe(mUserService.getMyNews(token(),uid()),observer);
    }

    //获取充值列表
    public static Subscription getRechargePriceList(Observer<BaseBean<RechargeListBean>> observer){
        return setSubscribe(mUserService.getRechargePriceList(),observer);
    }

    //物品提现
    public static Subscription sendGoods(String username,String mobile,String address ,String user_asset,Observer<BaseBean<MessageBean>> observer){
        return setSubscribe(mUserService.sendGoods(token(),username,mobile,address,user_asset),observer);
    }

    public static Subscription sendGiftToUser(long toUserId,int dollId,Observer<BaseBean<MessageBean>> observer){
        return setSubscribe(mUserService.sendGiftToUser(token(),toUserId,dollId),observer);
    }

    //举报
    public static Subscription sendReports(long toUserId,int reasonId,Observer<BaseBean<MessageBean>> observer){
        return setSubscribe(mUserService.sendReports(token(),toUserId,reasonId),observer);
    }
}
