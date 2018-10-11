package pri.wx.xujc.xzjy.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import pri.wx.xujc.xzjy.data.model.*;
import pri.wx.xujc.xzjy.data.source.DataSource;
import pri.wx.xujc.xzjy.data.source.local.PersistenceContract.UserEntry;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocalDataSource implements DataSource {

    @Nullable
    private static LocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    private LocalDataSource(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
    }

    @NonNull
    private User getUser(@NonNull Cursor c) {
        String id = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_ID));
        String name = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_NAME));
        String password = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_PWD));
        String token = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_TOKEN));

        return new User(id, name, password, token);
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Single<String> getWelcomeImage() {
        return null;
    }

    @Override
    public void saveWelcomeImage(String url) { }

    public Single<User> getUser() {
        String[] projection = {
                UserEntry.COLUMN_NAME_ID,
                UserEntry.COLUMN_NAME_NAME,
                UserEntry.COLUMN_NAME_PWD,
                UserEntry.COLUMN_NAME_TOKEN,
        };

        String sql = String.format("SELECT %s FROM %s",
                TextUtils.join(",", projection),
                UserEntry.TABLE_NAME);

        return mDatabaseHelper.createQuery(UserEntry.TABLE_NAME, sql)
                .mapToList(this::getUser)
                .map(list -> {
                    if(list.isEmpty())
                        return new User();
                    else return list.get(0);
                })
                .onErrorReturn(user -> new User())
                .firstOrError()
                ;
    }

    @Override
    public Single<StuInfoEntity> getStuInfo() {
        return null;
    }

    @Override
    public Single<TokenModel> getToken(String sno, String pwd) {
        return null;
    }

    @Override
    public void saveUser(@NonNull User user) {
        checkNotNull(user);
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME_ID, user.getId());
        values.put(UserEntry.COLUMN_NAME_NAME, user.getName());
        values.put(UserEntry.COLUMN_NAME_PWD, user.getPassword());
        values.put(UserEntry.COLUMN_NAME_TOKEN, user.getToken());
        mDatabaseHelper.insert(UserEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
        Completable.complete();
    }

    @Override
    public Single<User> refreshToken() {
        return null;
    }

    @Override
    public Single<List<CourseClass>> getSchedule(String tmId) {
        return null;
    }

    @Override
    public Single<List<Term>> getTerm() {
        return null;
    }

    @Override
    public Single<String> getWeek() {
        return null;
    }

}


