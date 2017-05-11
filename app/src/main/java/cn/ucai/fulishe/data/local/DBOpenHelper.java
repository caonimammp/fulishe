package cn.ucai.fulishe.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHelper extends SQLiteOpenHelper{
    private final static int versionNo = 1;

    public static final String USER_TABALE_NAME = "t_fulishe_user";
    public static final String USER_COLUMN_NAME = "m_user_name";
    public static final String USER_COLUMN_NICK = "m_user_nick";
    public static final String USER_COLUMN_AVATAR = "m_user_avatar_id";
    public static final String USER_COLUMN_AVATAR_PATH = "m_user_avatar_path";
    public static final String USER_COLUMN_AVATAR_SUFFIX = "m_user_avatar_suffix";
    public static final String USER_COLUMN_AVATAR_TYPE = "m_user_avatar_type";
    public static final String USER_COLUMN_AVATAR_UPDATE_TIME = "m_user_avatar_update_time";

    private static final String FULICENTER_USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABALE_NAME + " ("
                    + USER_COLUMN_NAME + " TEXT PRIMARY KEY,"
                    + USER_COLUMN_NICK + " TEXT,"
                    + USER_COLUMN_AVATAR + " INTEGER,"
                    + USER_COLUMN_AVATAR_PATH + " TEXT,"
                    + USER_COLUMN_AVATAR_SUFFIX + " TEXT,"
                    + USER_COLUMN_AVATAR_TYPE + " INTEGER,"
                    + USER_COLUMN_AVATAR_UPDATE_TIME + " TEXT);";

    private static  DBOpenHelper mHelper;

    public static DBOpenHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new DBOpenHelper(context);
        }
        return mHelper;
    }
    private DBOpenHelper(Context context) {
        super(context, getDataBaseName(), null, versionNo);
    }

    private static String getDataBaseName() {
        return "cn.ucai.fulishe.db";
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FULICENTER_USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
