package cn.edu.university.zfcms.base.contract;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.base.BaseParser;
import cn.edu.university.zfcms.base.BasePresenter;
import cn.edu.university.zfcms.base.BaseView;
import cn.edu.university.zfcms.model.entity.User;

/**
 * Login协议
 */
public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showLoginSuccessfulView(User user);
        void showLoginErrorView(String msg);

        void resetLoginForm();

        void showBaasLoginView();

        void showZfsoftLoginView();

        boolean isBassLoginType();

        void showCheckCodeView(Bitmap bitmap);
        void showErrLoadCheckCode(String msg);

        void showFocusView(android.view.View focusView);
        void showLoadingCheckCodeView(boolean isShow);
        void showLogingIndicatorView(boolean isShow);

    }

    interface Presenter extends BasePresenter {
        void login(User user);

        void login(User user, String checkCode);
        void reloadCheckCode();
    }

    abstract class Parser extends BaseParser {
        public abstract boolean isLoginSuccessful(User loginUser, String loginUserHtml);

        public abstract boolean isCheckCodeLoadSuccessful(byte[] checkCodeBytes);

        public abstract boolean isCheckCodeInputMismatch(String htmlResp);

        public abstract boolean isPswdInputMismatch(String html);
    }
}
