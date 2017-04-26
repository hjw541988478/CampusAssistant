package cn.edu.university.zfcms.data.login;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.app.mvp.BaseDataSource;
import cn.edu.university.zfcms.storage.entity.User;
import io.reactivex.Single;

/**
 * Created by hjw on 16/4/15.
 */
public interface LoginDataSource extends BaseDataSource {

    User getLoginUser();

    Single<Bitmap> loadCheckCode();

    Single<User> login(User user, String checkCode);

    Single<User> signUp(User user);

    void updateLoginUser(User user);

    void logout();
}
