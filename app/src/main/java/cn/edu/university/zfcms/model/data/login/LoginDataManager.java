package cn.edu.university.zfcms.model.data.login;

import android.content.Context;

import cn.edu.university.zfcms.base.data.LoginDataSource;
import cn.edu.university.zfcms.model.entity.User;

/**
 * Created by hjw on 16/4/15.
 */
public class LoginDataManager implements LoginDataSource {
    private User currentUser = null;

    private static LoginDataManager INSTANCE = null;

    //    private final LoginDataSource localLoginSource ;
    private final LoginDataSource remoteLoginSource;
    private final LoginDataSource bmobLoginSource;

    private LoginDataManager(Context context) {
//        this.localLoginSource = new LocalLoginDataSource();
        this.remoteLoginSource = new RemoteLoginDataSource();
        this.bmobLoginSource = LocalLoginDataSource.getInstance(context);
    }

    public static LoginDataManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LoginDataManager(context);
        }
        return INSTANCE;
    }

    @Override
    public User getLoginUser() {
        if (currentUser == null) {
            currentUser = bmobLoginSource.getLoginUser();
        }
        return currentUser;
    }

    @Override
    public void loadCheckCode(GetLoginCheckCodeCallback callback) {
        remoteLoginSource.loadCheckCode(callback);
    }

    @Override
    public void login(User user, String checkCode, GetLoginDataCallback callback) {
        remoteLoginSource.login(user, checkCode, callback);
    }

    @Override
    public void signIn(User user, GetLoginDataCallback callback) {
        bmobLoginSource.signIn(user, callback);
    }

    @Override
    public void signUp(User user) {
        bmobLoginSource.signUp(user);
    }

    @Override
    public void updateLoginUser(User user) {
        bmobLoginSource.updateLoginUser(user);
    }

    private void refreshCache() {
        currentUser = bmobLoginSource.getLoginUser();
    }

    @Override
    public void logout() {
        this.currentUser = null;
    }
}
