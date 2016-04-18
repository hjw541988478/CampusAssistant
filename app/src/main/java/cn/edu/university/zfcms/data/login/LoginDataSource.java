package cn.edu.university.zfcms.data.login;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/15.
 */
public interface LoginDataSource {
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
    void login(User user,GetLoginDataCallback callback);
    void logout();
}
