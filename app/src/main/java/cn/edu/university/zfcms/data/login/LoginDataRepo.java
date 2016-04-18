package cn.edu.university.zfcms.data.login;

import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/15.
 */
public class LoginDataRepo implements LoginDataSource {
    private User currentUser = null;

    private static LoginDataRepo INSTANCE = null;
    private final LoginDataSource loginDataLocalSource ;
    private final LoginDataSource loginDataRemoteSource ;

    private LoginDataRepo(LoginDataSource localSource,LoginDataSource remoteSource){
        this.loginDataLocalSource = localSource;
        this.loginDataRemoteSource = remoteSource;
    }

    public static LoginDataRepo getInstance(LoginDataSource localSource,LoginDataSource remoteSource){
        if (INSTANCE == null) {
            INSTANCE = new LoginDataRepo(localSource,remoteSource);
        }
        return INSTANCE;
    }

    @Override
    public User getLoginUser() {
        if (currentUser == null) {
            currentUser = loginDataLocalSource.getLoginUser() ;
        } else {
            User localUser = loginDataLocalSource.getLoginUser();
            if (!localUser.equals(currentUser)){
                currentUser = localUser;
            }
        }
        return currentUser;
    }

    private void refreshCache(){
        currentUser = loginDataLocalSource.getLoginUser();
    }

    @Override
    public void loadCheckCode(GetLoginCheckCodeCallback callback) {
        loginDataRemoteSource.loadCheckCode(callback);
    }

    @Override
    public void login(User user, final GetLoginDataCallback callback) {
        loginDataRemoteSource.login(user, new GetLoginDataCallback() {
            @Override
            public void onGetLoginData(User user) {
                currentUser = new User(user);
                loginDataLocalSource.login(currentUser,null);
                callback.onGetLoginData(currentUser);
            }

            @Override
            public void onGetLoginError(String msg) {
                callback.onGetLoginError(msg);
            }
        });
    }

    @Override
    public void logout() {
        this.currentUser = null;
        loginDataLocalSource.logout();
    }
}
