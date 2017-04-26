package cn.edu.university.zfcms.data.login;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.base.func.BaseDataSource;
import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/15.
 */
public interface LoginDataSource extends BaseDataSource {

    interface GetLoginDataCallback{
        void onGetLoginData(User user);
        void onGetLoginError(String msg);
    }

    interface GetLoginCheckCodeCallback {
        void onGetCheckCode(Bitmap bitmap);
        void onGetCheckCodeError();
    }

    User getLoginUser();
    void loadCheckCode(GetLoginCheckCodeCallback callback);

    void login(User user, String checkCode, GetLoginDataCallback callback);

    void signIn(User user, GetLoginDataCallback callback);

    void signUp(User user);

    void updateLoginUser(User user);

    void logout();
}
