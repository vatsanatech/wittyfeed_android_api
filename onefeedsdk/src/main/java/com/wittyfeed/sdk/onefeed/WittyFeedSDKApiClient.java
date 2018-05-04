package com.wittyfeed.sdk.onefeed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by aishwarydhare on 21/10/17.
 */

public class WittyFeedSDKApiClient {

    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public static final String SDK_Version = "1.0.7";
    private static String uniqueID = null;
    private String APP_ID = "";
    private String API_KEY = "";
    private String PACKAGE_NAME = "";
    private String TAG = "WF_SDK";
    private String FCM_TOKEN = "";
    private Context context;
    private Map<String, String> user_meta;
    private Map<String, String> device_meta;
    private String device_id = "";

    public WittyFeedSDKApiClient(Activity para_activity, String app_id , String api_key, String fcm_token){
        this.context = para_activity;
        this.APP_ID = app_id;
        this.API_KEY = api_key;
        this.PACKAGE_NAME = para_activity.getPackageName().toLowerCase();
        prepare_user_meta();
        prepare_device_meta();
        this.FCM_TOKEN = fcm_token;
    }

    public WittyFeedSDKApiClient(Context para_context, String app_id , String api_key, String fcm_token){
        this.context = para_context;
        this.APP_ID = app_id;
        this.API_KEY = api_key;
        this.PACKAGE_NAME = this.context.getPackageName().toLowerCase();
        prepare_user_meta();
        prepare_device_meta();
        this.FCM_TOKEN = fcm_token;
    }

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    private void prepare_user_meta(){
        if(user_meta == null){
            user_meta = new HashMap<>();
        }

        user_meta.put("device_type", "android");
        user_meta.put("onefeed_sdk_version", SDK_Version);

        // default country / locale of user's device in ISO3 format
        String locale_country_iso3 = context.getResources().getConfiguration().locale.getISO3Country();
        user_meta.put("client_locale", locale_country_iso3);

        // default language of user's device
        String locale_language = Locale.getDefault().getLanguage();
        user_meta.put("client_locale_language", locale_language);

        // AndroidID or DeviceID
        // It’s a 64-bit number that should remain constant for the lifetime of a device
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        user_meta.put("android_id", android_id);
        user_meta.put("device_id", android_id);
        device_id = android_id;

        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float height = displayMetrics.heightPixels;
            float width = displayMetrics.widthPixels;
            user_meta.put("screen_height", "" + height);
            user_meta.put("screen_width", "" + width);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepare_device_meta(){
        if(device_meta == null){
            device_meta = new HashMap<>();
        }

        device_meta.put("device_type", "android");
        device_meta.put("onefeed_sdk_version", SDK_Version);

        //It’s a 64-bit number that should remain constant for the lifetime of a device
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_meta.put("android_id", android_id);
        device_meta.put("device_id", android_id);
        device_id = android_id;

        // UUID.randomUUID() method generates an unique identifier for a specific installation.
        String uuid = id(context);
        device_meta.put("uuid", uuid);
        Log.i(TAG, "UU ID: "+uuid);


        String serial_number = "";
        if(Build.VERSION.SDK_INT <26){
            serial_number = Build.SERIAL;
        }
        device_meta.put("serial_number", serial_number);

        // user's device Android_ID
        try {
            @SuppressLint("HardwareIds")
            String client_uuid = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            device_meta.put("client_uuid", client_uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float height = displayMetrics.heightPixels;
            float width = displayMetrics.widthPixels;
            device_meta.put("screen_height", ""+ height);
            device_meta.put("screen_width", "" + width);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUser_meta() {
        return new JSONObject(user_meta).toString();
    }

    public void setUser_meta(Map<String, String> para_user_mata) {
        if(para_user_mata.containsKey("client_gender")){
            if(para_user_mata.get("client_gender").equalsIgnoreCase("")){
                user_meta.put("client_gender", "N");
            } else {
                user_meta.put("client_gender", para_user_mata.get("client_gender"));
            }
        }

        if(para_user_mata.containsKey("client_interests")){
            if(para_user_mata.get("client_interests").equalsIgnoreCase("")){
                user_meta.put("client_interests", "");
            } else {
                user_meta.put("client_interests", para_user_mata.get("client_interests"));
            }
        }
    }

    public String getDevice_meta() {
        return new JSONObject(device_meta).toString();
    }

    String getAPP_ID() {
        return APP_ID;
    }

    String getAPI_KEY() {
        return API_KEY;
    }

    String getPACKAGE_NAME() {
        return PACKAGE_NAME;
    }

    String getFCM_TOKEN() {
        if (FCM_TOKEN == null) {
            return "";
        }
        return FCM_TOKEN;

    }

    void setFCM_TOKEN(String FCM_TOKEN) {
        this.FCM_TOKEN = FCM_TOKEN;
    }

    String getDevice_id() {
        return device_id;
    }
}
