package pri.wx.xujc.xzjy.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Student.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";


    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + PersistenceContract.UserEntry.TABLE_NAME + " (" +
                    PersistenceContract.UserEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.UserEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.UserEntry.COLUMN_NAME_PWD + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.UserEntry.COLUMN_NAME_TOKEN + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.UserEntry.COLUMN_NAME_NJ + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.UserEntry.COLUMN_NAME_ZY + TEXT_TYPE +
                    " )";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }


}