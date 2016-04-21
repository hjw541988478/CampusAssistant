package cn.edu.university.zfcms.data.login.local;

import android.content.Context;
import android.util.Log;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.edu.university.zfcms.data.login.LoginDataSource;
import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/20.
 */
public class BassLoginDataSource implements LoginDataSource {
    private static final String tag = BassLoginDataSource.class.getSimpleName();

    private Context context;

    private BassLoginDataSource(Context context) {
        this.context = context;
    }

    private static BassLoginDataSource loginDataSource;

    public static BassLoginDataSource getInstance(Context context) {
        if (loginDataSource == null) {
            loginDataSource = new BassLoginDataSource(context);
        }
        return loginDataSource;
    }

    @Override
    public User getLoginUser() {
        return BmobUser.getCurrentUser(context, User.class);
    }

    @Override
    public void loadCheckCode(GetLoginCheckCodeCallback callback) {
        // remote
    }

    @Override
    public void login(User user, String checkCode, GetLoginDataCallback callback) {
        // remote
    }

    @Override
    public void signIn(final User user, final GetLoginDataCallback callback) {
        user.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d(tag, user.getUsername() + " signIn successfully");
                callback.onGetLoginData(user);
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(tag, user.getUsername() + " signIn error " + s);
                callback.onGetLoginError(s);
            }
        });
    }

    @Override
    public void signUp(final User user) {
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d(tag, user.getUsername() + " signUp successfully");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(tag, user.getUsername() + " signUp error " + s);
            }
        });
    }

    @Override
    public void updateLoginUser(final User user) {
        user.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.d(tag, user.getUsername() + " update successfully");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(tag, user.getUsername() + " update error " + s);
            }
        });
    }

    @Override
    public void logout() {

    }
}
