package pri.wx.xujc.xzjy;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;
import pri.wx.xujc.xzjy.util.SharedPreferencesUtils;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication.Tag";


    private static final String TERM = "20181";

    private static MyApplication INSTANCE;

    private String token;

    private String week;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        token = (String) SharedPreferencesUtils.getParam(this,"token","");
        week = (String) SharedPreferencesUtils.getParam(this,"week","5");
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

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        SharedPreferencesUtils.setParam(this,"week",week);
        this.week = week;
    }

    public static long getLastDate() {
        Long defaultDate = 0l;
        return (Long) SharedPreferencesUtils.getParam(INSTANCE, "date", defaultDate);
    }

    private static int getVersionCode() {
        int versionCode = 1;
        try {
            versionCode = INSTANCE.getPackageManager().getPackageInfo(INSTANCE.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "版本号获取失败");
            e.printStackTrace();
        }
        return versionCode;
    }

    private static String getVersionName() {
        String versionName = "0.1";
        try {
            versionName = INSTANCE.getPackageManager().getPackageInfo(INSTANCE.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "版本名获取失败");
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getTerm() {
        return TERM;
    }

    public static boolean isNewVersion(int versionCode, String versionName) {
        return versionCode != getVersionCode() || !versionName.equals(getVersionName());
    }

}
