package cn.edu.university.zfcms.presenter.login;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.app.ErrDef;
import cn.edu.university.zfcms.app.App;
import cn.edu.university.zfcms.base.contract.LoginContract;
import cn.edu.university.zfcms.model.entity.User;
import cn.edu.university.zfcms.model.data.login.LoginDataManager;
import cn.edu.university.zfcms.base.data.LoginDataSource;

/**
 * Created by hjw on 16/4/15.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginDataManager loginDataRepo;
    private LoginContract.View loginView;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginDataRepo = LoginDataManager.getInstance(App.getInstance());
        this.loginView = loginView;
        this.loginView.setPresenter(this);
    }

    @Override
    public void start() {
        reloadCheckCode();
    }

    @Override
    public void login(User user) {
        loginByBass(user);
    }

    @Override
    public void login(User user, String checkCode) {
        loginByZfsoft(user, checkCode);
    }

    private void loginByBass(final User user) {
        loginDataRepo.signIn(user, new LoginDataSource.GetLoginDataCallback() {
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
            public void onGetLoginError(String msg) {
                reloadCheckCode();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.showZfsoftLoginView();
                    }
                });
            }
        });
    }

    private void loginByZfsoft(User user, String checkCode) {
        loginDataRepo.login(user, checkCode, new LoginDataSource.GetLoginDataCallback() {
            @Override
            public void onGetLoginData(final User user) {
                loginDataRepo.signUp(user);
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginView.showLoginSuccessfulView(user);
                    }
                }, 1500);
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
                        loginView.showErrLoadCheckCode(ErrDef.ERR_LOAD_CHECKCODE);
                    }
                });

            }
        });

    }
}
