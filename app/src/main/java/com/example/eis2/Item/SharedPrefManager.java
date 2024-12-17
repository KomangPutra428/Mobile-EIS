package com.example.eis2.Item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    //Deklarasi Variable, untuk kontek activity
    private Context mContext;

    //Nama File untuk SharedPreferenxe
    private static final String SHARED_PREF_NAME = "FCMTokenMessenger";

    //Deklarasi Variabel untuk Instance
    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mSPM;

    //Key untuk mengambil Value/Token pada SharedPreference
    private static String TOKEN_KEY_ACCESS = "token";

    private SharedPrefManager(Context context){
        mContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mSPM == null){
            mSPM = new SharedPrefManager(context);
        }
        return mSPM;
    }

    void setToken(String token){
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN_KEY_ACCESS, token);
        editor.apply();
    }

    public String getToken(){
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(TOKEN_KEY_ACCESS, null);
    }
}
