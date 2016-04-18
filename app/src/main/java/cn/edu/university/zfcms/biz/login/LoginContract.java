package cn.edu.university.zfcms.biz.login;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.base.mvp.BasePresenter;
import cn.edu.university.zfcms.base.mvp.BaseView;
import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/15.
 */
public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showLoginSuccessfulView(User user);
        void showLoginErrorView(String msg);

        void resetLoginForm();

        void showCheckCodeView(Bitmap bitmap);
        void showErrLoadCheckCode(String msg);

        void showFocusView(android.view.View focusView);
        void showLoadingCheckCodeView(boolean isShow);
        void showLogingIndicatorView(boolean isShow);

    }

    interface Presenter extends BasePresenter {

        void login(User user);
        void reloadCheckCode();
    }

    interface Parser {
        boolean isLoginSuccessful(User loginUser,String loginUserHtml);

        boolean isCheckCodeLoadSuccessful(byte[] checkcodeBytes);
        boolean isCheckCodeInputMismatch(String htmlResp);
        boolean isPswdInputMismatch(String html);

        String parseViewStateParam(String viewstateHtml);
        boolean isInvalidViewStatePageShow(String html);
    }
}
