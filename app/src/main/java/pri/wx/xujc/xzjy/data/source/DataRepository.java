package pri.wx.xujc.xzjy.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import io.reactivex.Single;
import pri.wx.xujc.xzjy.MyApplication;
import pri.wx.xujc.xzjy.data.model.StuInfoEntity;
import pri.wx.xujc.xzjy.data.model.TokenModel;
import pri.wx.xujc.xzjy.data.model.User;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataRepository implements DataSource {

    private static final String TAG = "Data";

    @Nullable
    private static DataRepository INSTANCE = null;

    @NonNull
    private final DataSource mRemoteDataSource;

    @NonNull
    private final DataSource mLocalDataSource;

    @NonNull
    private Map<String,String> mCached;

    private DataRepository(@NonNull DataSource mLocalDataSource,
                           @NonNull DataSource mRemoteDataSource) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
        mCached = new LinkedHashMap<>();
    }

    public static DataRepository getInstance(@NonNull DataSource tasksLocalDataSource,
                                              @NonNull DataSource tasksRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(tasksLocalDataSource, tasksRemoteDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(DataSource, DataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Single<String> getWelcomeImage() {
        return mRemoteDataSource.getWelcomeImage()
                .doOnSuccess(url -> {
                    mCached.put("welcome",url);
                    Log.i(TAG, "Result URL:" + url);
                });
    }

    @Override
    public void saveWelcomeImage(String url) {
        mLocalDataSource.saveWelcomeImage(url);
        //mRemoteDataSource.saveWelcomeImage(url);
    }

    @Override
    public Single<User> getUser() {
        return mLocalDataSource.getUser();
    }

    @Override
    public Single<StuInfoEntity> getStuInfo() {
        return mRemoteDataSource.getStuInfo();
    }

    @Override
    public Single<TokenModel> getToken(String sno, String pwd) {
        return mRemoteDataSource.getToken(sno,pwd)
                .doOnSuccess(token -> {
                    if(null != token) {
                        saveUser(new User(sno,"",pwd,token.getToken()));
                        MyApplication.getInstance().setToken(token.getToken());
                    }
                });
    }

    @Override
    public void saveUser(User user) {
        mLocalDataSource.saveUser(user);
        mCached.put("token", user.getToken());
    }

    @Override
    public Single<User> refreshToken() {
        return mRemoteDataSource.refreshToken();
    }
}
