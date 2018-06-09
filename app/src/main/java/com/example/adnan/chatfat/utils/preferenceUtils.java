package com.example.adnan.chatfat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class preferenceUtils {

    private static final String PREFERENCE_KEY_USER_ID = "userid";
    private static final String PREFERENCE_KEY_NICKNAME = "nickname";
    private static final String PREFERENCE_KEY_PROFILE_URL = "profileUrl";
    private static final String PREFERENCE_KEY_CONNECTION_STATUS = "connectionStatus";
    private static final String PREFERENCE_KEY_CHANNEL_DISTINCT = "channelDistinct";

    private static Context mContext;

    private preferenceUtils(){
    }

    public static void init(Context context){
        mContext = context;
    }

    private static SharedPreferences getSharedPreference(){
        return mContext.getSharedPreferences("sendbird", Context.MODE_PRIVATE);
    }

    public static void setUserId(String userId){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(PREFERENCE_KEY_USER_ID, userId).apply();
    }
    public static String getUserId(){
        return getSharedPreference().getString(PREFERENCE_KEY_USER_ID, "");
    }

    public static void setNickName(String nickName){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(PREFERENCE_KEY_NICKNAME, nickName).apply();
    }
    public static String getNickName(){
        return getSharedPreference().getString(PREFERENCE_KEY_NICKNAME, "");
    }

    public static void setProfileURL(String url){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(PREFERENCE_KEY_PROFILE_URL, url).apply();
    }
    public static String getProfileUrl(){
        return getSharedPreference().getString(PREFERENCE_KEY_PROFILE_URL, "");
    }

    public static void setConnectionStatus(boolean status){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(PREFERENCE_KEY_CONNECTION_STATUS, status).apply();
    }
    public static boolean getConnectionStatus(){
        return getSharedPreference().getBoolean(PREFERENCE_KEY_CONNECTION_STATUS, false);
    }

    public static void setChannelDistinct(Boolean distinct){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(PREFERENCE_KEY_CHANNEL_DISTINCT, distinct).apply();
    }
    public static Boolean getChannelDistinct(){
        return getSharedPreference().getBoolean(PREFERENCE_KEY_CHANNEL_DISTINCT, true);
    }
}
