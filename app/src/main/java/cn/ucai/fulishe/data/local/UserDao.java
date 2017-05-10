package cn.ucai.fulishe.data.local;

import android.content.Context;
import cn.ucai.fulishe.data.bean.User;



public class UserDao {
    public UserDao(Context context) {
        DBManager.getInstance().iniDB(context);
    }

    public User getUser(String username) {
        return DBManager.getInstance().getUser(username);
    }

    public boolean saveUser(User user) {
        return DBManager.getInstance().saveUser(user);
    }
}

