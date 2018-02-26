package huatech.plugin.sso;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;

import java.util.List;


public class SsowxbPlugin extends CordovaPlugin {

    private CallbackContext callbackContext;
    private static final int GET_CODE_REQUEST = 1;
    private ContentResolver contentResolver = null;
    private ContentValues contentValues = null;
    private String mybproviderUri = "content://com.sxca.miyaobao.mybprovider/token";
    private String sys_flag = "WebDemo_MobileCertST_OAuth";
    private String auth_sys = "OA办公";
    private String gatewayIp = "218.26.169.142";
    private String redirectUrl = "myb://certauth"; //重定向地址

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;

        if (action.equals("hasInstalledMiYaoBao")) {


            if (hasApplication()) {
                this.callbackContext.success("ok");

            } else {
                this.callbackContext.error("not install miyaobao");
            }
            return true;
        } else if (action.equals("getToken")) {
            String token = getAccessToken();
            if (!TextUtils.isEmpty(token)) {
                this.callbackContext.success(token);
            } else {
                this.callbackContext.error("can't get token");
            }
            return true;
        } else if (action.equals("saveToken")) {
            String token = args.getString(0);
            if (saveAccessToken(token)) {
                this.callbackContext.success("ok");
            } else {
                this.callbackContext.error("can't save token");
            }
            return true;
        } else if (action.equals("delToken")) {

            String oldToken = getAccessToken();
            if (oldToken != null) {
                if (deleteAcctessToken() > 0) {
                    this.callbackContext.success("ok");
                } else {
                    this.callbackContext.error("del token fail..");
                }
            } else {
                this.callbackContext.error("del token fail..");
            }

            return true;
        } else if (action.equals("getCode")) {
            Intent intent = new Intent();
            intent.setData(Uri.parse("myb://certauth"));
            Bundle bundle = new Bundle();
            bundle.putString("systemFlag", sys_flag);
            bundle.putString("authSys", auth_sys);
            bundle.putString("authType", "GATEWAY_OAUTH");
            bundle.putString("gatewayIp", gatewayIp);
            bundle.putString("busDes", "授权登录");
            bundle.putString("redirectUrl", redirectUrl);
            intent.putExtras(bundle);

            // cordova.setActivityResultCallback(this);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
            cordova.startActivityForResult(SsowxbPlugin.this, intent, GET_CODE_REQUEST);
            return true;
        }

        return false;
    }


    private String getAccessToken() {
        if (contentResolver == null) {
            contentResolver = cordova.getActivity().getContentResolver();
        }
        Cursor cursor = contentResolver.query(Uri.parse(mybproviderUri), null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("token"));
            }
        }
        return null;
    }

    private boolean hasApplication() {
        PackageManager manager = this.cordova.getActivity().getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("myb://certauth"));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

    //删除令牌的方法
    private int deleteAcctessToken() {
        return contentResolver.delete(Uri.parse(mybproviderUri), null, null);
    }

    //保存令牌的方法
    private boolean saveAccessToken(String accessToken) {
        String oldToken = getAccessToken();
        if (oldToken != null) {
            deleteAcctessToken();
        }
        contentValues = new ContentValues();
        contentValues.put("token", accessToken);
        Uri uri = contentResolver.insert(Uri.parse(mybproviderUri), contentValues);
        return uri != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_CODE_REQUEST:
                if (data != null && data.getExtras() != null) {
                    String code = data.getStringExtra("message");
                    callbackContext.success(code);
                } else {
                    String err = "can't get code";
                    callbackContext.error(err);
                }
                break;
            default:
                break;
        }
    }
}
