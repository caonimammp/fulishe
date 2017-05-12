package cn.ucai.fulishe.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulishe.data.bean.User;
import cn.ucai.fulishe.data.utils.L;


public class DBManager {
    private static DBOpenHelper sHelper;
    private static DBManager dbManger = new DBManager();

    public static DBManager getInstance() {
        return dbManger;
    }

    public  void iniDB(Context context) {
        sHelper = DBOpenHelper.getInstance(context);
    }

    public boolean saveUser(User user) {
        SQLiteDatabase database = sHelper.getWritableDatabase();
        if (database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.USER_COLUMN_NAME,user.getMuserName());
            values.put(DBOpenHelper.USER_COLUMN_NICK,user.getMuserNick());
            long insert = database.replace(DBOpenHelper.USER_TABALE_NAME, null, values);
            L.e("main","DBManager.saveUser.insert:"+insert);
            return insert > 0 ? true : false;
        }
        return false;
    }


    public synchronized User getUser(String username) {
        SQLiteDatabase database = sHelper.getWritableDatabase();
        User user = new User();
        if (database.isOpen()) {
            String sql = "select * from " + sHelper.USER_TABALE_NAME + " where " + sHelper.USER_COLUMN_NAME + "=?";
            Cursor cursor = database.rawQuery(sql, new String[]{username});
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(sHelper.USER_COLUMN_AVATAR));
                String nick = cursor.getString(cursor.getColumnIndex(sHelper.USER_COLUMN_NICK));
                String path = cursor.getString(cursor.getColumnIndex((sHelper.USER_COLUMN_AVATAR_PATH)));
                L.e("main","path="+path);
                user = new User(username, nick, id, path, null, 0, null);
            }
        }
        return user;
    }
}
