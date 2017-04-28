package cn.edu.university.zfcms.model.data.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.contract.LoginContract;
import cn.edu.university.zfcms.model.data.common.CommonDataManager;
import cn.edu.university.zfcms.base.data.CommonDataSource;
import cn.edu.university.zfcms.base.data.LoginDataSource;
import cn.edu.university.zfcms.model.parser.LoginParser;
import cn.edu.university.zfcms.model.entity.User;
import cn.edu.university.zfcms.model.network.BizServiceManager;
import io.reactivex.Single;
import io.reactivex.internal.operators.single.SingleDefer;

/**
 * Created by hjw on 16/4/14.
 */
public class RemoteLoginDataSource implements LoginDataSource{
    private static final String tag = RemoteLoginDataSource.class.getSimpleName();

    private LoginContract.Parser parser;
    private CommonDataSource commonDataSource;

    @BindString(R.string.err_login_checkcode)
    String errLoginCheckCode;
    @BindString(R.string.err_login_idorpswd)
    String errLoginIdOrPswd;
    @BindString(R.string.err_login_zfsoft)
    String errLoginZfCms;
    @BindString(R.string.err_login_viewstate)
    String errLoginViewState;

    public RemoteLoginDataSource() {
        parser = new LoginParser();
        commonDataSource = CommonDataManager.getInstance();
    }

    /**
     * 登录
     * 1. 登录成功直接返回
     * 2. 刷新ViewState参数
     * 3. 重试登录
     */
    @Override
    public Single<User> login(User user, String checkCode) {
        return BizServiceManager.getInstance().getLoginService()
                    .login(buildLoginParams(user.userId, user.userPswd, checkCode))
                    .flatMap(loginResp -> {
                        if (parser.isLoginSuccessful(user, loginResp)) {
                            Log.d(tag, "user login successfully , userinfo :" + user.toString());
                            return Single.just(user);
                        } else {
                            Log.d(tag, "user login failed .");
                            if (parser.isViewStateInvalid(loginResp)) {
                                if (commonDataSource.refreshLoginViewState()) {
                                    return login(user, checkCode);
                                } else {
                                    return Single.error(new Throwable(errLoginViewState));
                                }

                            } else if (parser.isCheckCodeInputMismatch(loginResp)) {
                                return SingleDefer.error(new Throwable(errLoginCheckCode));
                            } else if (parser.isPswdInputMismatch(loginResp)) {
                                return SingleDefer.error(new Throwable(errLoginIdOrPswd));
                            } else {
                                return SingleDefer.error(new Throwable(errLoginZfCms));
                            }
                        }
                    });
//                .retryWhen(errors -> errors.
//                        errors.zipWith(Observable.range(1, 2), (n, i) -> i)
//                              .flatMap(retryCount -> Single.timer((long) Math.pow(5, retryCount), TimeUnit.SECONDS))
    }

    /**
     * 加载验证码
     * @return
     */
    @Override
    public Single<Bitmap> loadCheckCode(){
        return  BizServiceManager.getInstance().getLoginService()
                .loadLoginCheckCode()
                .map(responseBody -> BitmapFactory.decodeStream(responseBody.byteStream()));
    }

    private Map<String,String> buildLoginParams(String userName, String userPswd, String checkCode){
        Map<String,String> params = new HashMap<>();
        params.put("txtUserName",userName);
        params.put("TextBox2",userPswd);
        params.put("txtSecretCode",checkCode);
        params.put("RadioButtonList1","学生");
        params.put("Button1","");
        params.put("lbLanguage","");
        params.put("hidPdrs","");
        params.put("hidsc","");
        return params;
    }

    @Override
    public User getLoginUser() {
        return new User();
    }

    @Override
    public Single<User> signUp(User user) {
        return Single.just(new User());
    }

    @Override
    public void updateLoginUser(User user) {

    }


    @Override
    public void logout() {

    }
}
