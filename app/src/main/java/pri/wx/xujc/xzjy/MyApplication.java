package pri.wx.xujc.xzjy;

import android.app.Application;
import pri.wx.xujc.xzjy.util.SharedPreferencesUtils;

import java.util.Date;

public class MyApplication extends Application {

    private static MyApplication INSTANCE;

    private String token;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        token = (String) SharedPreferencesUtils.getParam(this,"token","");
    }

    public static MyApplication getInstance() {
        return INSTANCE;
    }

    public String getToken() {
        SharedPreferencesUtils.setParam(this,"date",System.currentTimeMillis());
        return token;
    }

    public void setToken(String token) {
        SharedPreferencesUtils.setParam(this,"token",token);
        this.token = token;
    }

    public static long getLastDate() {
        Long defaultDate = 0l;
        return (Long) SharedPreferencesUtils.getParam(INSTANCE, "date", defaultDate);
    }

}
