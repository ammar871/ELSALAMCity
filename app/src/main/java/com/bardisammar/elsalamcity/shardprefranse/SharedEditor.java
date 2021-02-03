package com.bardisammar.elsalamcity.shardprefranse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bardisammar.elsalamcity.auth.LoginActivity;
import com.bardisammar.elsalamcity.auth.pojo.Users;
import com.bardisammar.elsalamcity.authNumber.LoginNumberActivity;
import com.bardisammar.elsalamcity.commen.Commen;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SharedEditor {
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;
    private static final String FILE_NAME = "coursatApp";
    public static final String KEY_IS_ENTERED = "entred";
    public static final String KEY_USER_PHONE = "phone";
    public static final String KEY_LOCATION = "address";

    public SharedEditor(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void saveData(String muser) {

        mEditor.putString(KEY_USER_PHONE, muser);


        mEditor.commit();
    }
    public void saveDataEnterd(boolean entred) {

        mEditor.putBoolean(KEY_IS_ENTERED, entred);
        mEditor.commit();
    }
    public HashMap<String, String> loadData() {
        HashMap<String, String> userData = new HashMap<>();

        userData.put(KEY_USER_PHONE, mSharedPreferences.getString(KEY_USER_PHONE, ""));

        return userData;
    }

    public HashMap<String, Boolean> loadDataEnter() {
        HashMap<String, Boolean> userData = new HashMap<>();

        userData.put(KEY_IS_ENTERED, mSharedPreferences.getBoolean(KEY_IS_ENTERED, false));

        return userData;
    }
    public void logOut() {
        mEditor.clear();
        mEditor.commit();
        Intent mIntent = new Intent(mContext, LoginNumberActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(mIntent);
        Commen.print("logout","ok");
    }


}
