package com.gachat.main.ui.user;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.DefaultHandler;

/**
 * https://github.com/TooCareAboutYOU/WebViewPager/blob/master/app/src/main/java/com/webviewpager/main/MainActivity.java
UpdateKey.API_TOKEN = "写上你fir.im账号的API_TOKEN";
UpdateKey.APP_ID = "写上APP的应用ID";
//下载方式:
//UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;通过Dialog来进行下载
//UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;通过通知栏来进行下载(默认)
UpdateFunGO.init(this);

触发手动更新
UpdateFunGO.manualStart(this);
 */
public class AboutUsActivity extends BaseActivity {

    private static final String TAG = "AboutUsActivity";

    private BridgeWebView mWebview;

    private WebSettings mWebSettings;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        mWebview=findViewById(R.id.webview);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initOperate(Bundle savedInstanceState) {
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    void init(){
        mWebSettings=mWebview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setNeedInitialFocus(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
//        mWebSettings.setDefaultFontSize(20);
        mWebview.setDefaultHandler(new DefaultHandler());

        mWebview.setWebViewClient(new BridgeWebViewClient(mWebview));
        mWebview.setWebChromeClient(new MyWebChromeClient());
        mWebview.loadUrl("http://demo.e3webrtc.com:9001/");

        mWebview.registerHandler("ObjCEcho", (data, function) ->{
            Log.i(TAG, "registerHandler: ");
            String str="html返回数据: "+data;
            Log.i(TAG, "init: "+str);
            function.onCallBack(str+"java经过处理后截取了一部分："+str.substring(0,5));
        });

        findViewById(R.id.btn_touch).setOnClickListener(v -> {
            mWebview.callHandler("JSEcho", "I am from Android!!!", data -> {
                Log.i(TAG, "callHandler: ");
                String str="html返回数据: "+data;
                Log.i(TAG, "onCallBack: "+str);
            });
        });
    }

    private class BridgeWebViewClient extends com.github.lzyzsd.jsbridge.BridgeWebViewClient{

        BridgeWebViewClient(BridgeWebView webView) {
            super(webView);
            Log.i(TAG, "BridgeWebViewClient: ");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "shouldOverrideUrlLoading: "+url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG, "onPageStarted: "+url+"\t\t");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i(TAG, "onPageFinished: "+url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i(TAG, "onReceivedError: "+errorCode+"\t\t"+description+"\t\t"+failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }


    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            super.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            super.onReceivedClientCertRequest(view, request);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
            super.onReceivedLoginRequest(view, realm, account, args);
        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            return super.onRenderProcessGone(view, detail);
        }

        @Override
        public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
            super.onSafeBrowsingHit(view, request, threatType, callback);
        }
    }

    private class MyWebChromeClient extends WebChromeClient{

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i(TAG, "onProgressChanged: "+newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i(TAG, "onReceivedTitle: "+title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            Log.i(TAG, "onReceivedIcon: ");
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(view, url, precomposed);
            Log.i(TAG, "onReceivedTouchIconUrl: "+url+"\t\t"+precomposed);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            Log.i(TAG, "onShowCustomView: ");
        }

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            super.onShowCustomView(view, requestedOrientation, callback);
            Log.i(TAG, "onShowCustomView: ");
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            Log.i(TAG, "onHideCustomView: ");
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            Log.i(TAG, "onCreateWindow: "+isDialog+"\t\t"+isUserGesture+"\t\t"+resultMsg);
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
            Log.i(TAG, "onRequestFocus: ");
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
            Log.i(TAG, "onCloseWindow: ");
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.i(TAG, "onJsAlert: "+url+"\t\t"+message+"\t\t"+result.toString());
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.i(TAG, "onJsConfirm: "+url+"\t\t"+message+"\t\t"+result.toString());
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.i(TAG, "onJsPrompt: "+url+"\t\t"+message+"\t\t"+defaultValue+"\t\t"+result.toString());
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            Log.i(TAG, "onJsBeforeUnload: "+url+"\t\t"+message+"\t\t"+result.toString());
            return super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
            super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
            Log.i(TAG, "onExceededDatabaseQuota: "+databaseIdentifier+"\t\t"+quota+"\t\t"+estimatedDatabaseSize+"\t\t"+totalQuota+"\t\t"+quotaUpdater.toString());
        }

        @Override
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            Log.i(TAG, "onReachedMaxAppCacheSize: "+requiredStorage+"\t\t"+quota+"\t\t"+quotaUpdater.toString());
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            Log.i(TAG, "onGeolocationPermissionsShowPrompt: "+origin+"\t\t"+callback.toString());
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
            Log.i(TAG, "onGeolocationPermissionsHidePrompt: ");
        }

        @Override
        public void onPermissionRequest(PermissionRequest request) {
            super.onPermissionRequest(request);
            Log.i(TAG, "onPermissionRequest: ");
        }

        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            super.onPermissionRequestCanceled(request);
            Log.i(TAG, "onPermissionRequestCanceled: ");
        }

        @Override
        public boolean onJsTimeout() {
            Log.i(TAG, "onJsTimeout: ");
            return super.onJsTimeout();
        }

        @Override
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            super.onConsoleMessage(message, lineNumber, sourceID);
            Log.i(TAG, "onConsoleMessage: "+message+"\t\t"+lineNumber+"\t\t"+sourceID);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.i(TAG, "onConsoleMessage: "+consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }

        @Nullable
        @Override
        public Bitmap getDefaultVideoPoster() {
            Log.i(TAG, "getDefaultVideoPoster: ");
            return super.getDefaultVideoPoster();
        }

        @Nullable
        @Override
        public View getVideoLoadingProgressView() {
            Log.i(TAG, "getVideoLoadingProgressView: ");
            return super.getVideoLoadingProgressView();
        }

        @Override
        public void getVisitedHistory(ValueCallback<String[]> callback) {
            super.getVisitedHistory(callback);
            Log.i(TAG, "getVisitedHistory: ");
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            Log.i(TAG, "onShowFileChooser: ");
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    }



}
