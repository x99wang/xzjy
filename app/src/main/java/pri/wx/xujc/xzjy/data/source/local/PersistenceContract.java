package pri.wx.xujc.xzjy.data.source.local;

import android.provider.BaseColumns;

public class PersistenceContract {

    private PersistenceContract() {

    }

    static abstract class UserEntry implements BaseColumns {
        static final String TABLE_NAME = "users";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_PWD = "password";
        static final String COLUMN_NAME_TOKEN = "token";
        static final String COLUMN_NAME_NJ = "nj";
        static final String COLUMN_NAME_ZY = "zy";
    }



}
