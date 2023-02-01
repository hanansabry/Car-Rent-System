package com.android.carrentsystem.datasource;

import android.content.SharedPreferences;

import com.android.carrentsystem.utils.Constants;

import javax.inject.Inject;

public class SharedPreferencesDataSource {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    @Inject
    public SharedPreferencesDataSource(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public void setAgencyId(String userId) {
        editor.putString(Constants.USER_ID, userId);
        editor.apply();
    }

    public String getAgencyId() {
        return sharedPreferences.getString(Constants.USER_ID, null);
    }


}
