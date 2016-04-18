package cn.edu.university.zfcms.login;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.data.model.User;
import cn.edu.university.zfcms.data.login.LoginDataRepo;
import cn.edu.university.zfcms.data.login.LoginDataSource;
import cn.edu.university.zfcms.data.login.local.LocalLoginDataSource;
import cn.edu.university.zfcms.data.login.remote.RemoteLoginDataSource;

/**
 * Created by hjw on 16/4/15.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginDataRepo loginDataRepo;
    private LoginContract.View loginView;

    public LoginPresenter(LoginContract.View loginedView){
        this.loginDataRepo = LoginDataRepo.getInstance(LocalLoginDataSource.getInstance()
                ,RemoteLoginDataSource.getInstance());
        this.loginView = loginedView;
        this.loginView.setPresenter(this);
    }

    @Override
    public void start() {
        reloadCheckCode();
    }

    @Override
    public void login(User user) {
        loginDataRepo.login(user, new LoginDataSource.GetLoginDataCallback() {
            @Override
            public void onGetLoginData(final User user) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.showLoginSuccessfulView(user);
                    }
                });

            }

            @Override
            public void onGetLoginError(final String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.showLoginErrorView(msg);
                    }
                });
            }
        });
    }

    @Override
    public void reloadCheckCode() {
        loginDataRepo.loadCheckCode(new LoginDataSource.GetLoginCheckCodeCallback() {
            @Override
            public void onGetCheckCode(final Bitmap bitmap) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.showCheckCodeView(bitmap);
                    }
                });

            }

            @Override
            public void onGetCheckCodeError() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.showErrLoadCheckCode("验证码加载失败");
                    }
                });

            }
        });

    }
}
