package cn.edu.university.zfcms.data.login.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.edu.university.zfcms.base.func.ErrDef;
import cn.edu.university.zfcms.biz.login.LoginContract;
import cn.edu.university.zfcms.http.HttpManager;
import cn.edu.university.zfcms.data.basic.CommonDataRepo;
import cn.edu.university.zfcms.data.basic.CommonDataSource;
import cn.edu.university.zfcms.model.User;
import cn.edu.university.zfcms.data.login.LoginDataSource;
import cn.edu.university.zfcms.parser.LoginParser;
import cn.edu.university.zfcms.util.ThreadPoolUtil;

/**
 * Created by hjw on 16/4/14.
 */
public class RemoteLoginDataSource implements LoginDataSource{
    private static final String tag = RemoteLoginDataSource.class.getSimpleName();

    private LoginContract.Parser parser;
    private CommonDataSource commonDataSource;

    public RemoteLoginDataSource() {
        parser = new LoginParser();
        commonDataSource = CommonDataRepo.getInstance();
    }

    /**
     * 登录
     */
    private String login(User user, String checkCode) {
        Map<String, String> params = buildLoginParams(user.userId, user.userPswd, checkCode);
        HttpUriRequest loginRequest = HttpManager.post(URL_LOGIN,params,new HashMap<String, String>());
        HttpResponse loginResp = HttpManager.performRequest(loginRequest);
        return HttpManager.parseStringResponse(loginResp);
    }

    /**
     * 加载验证码
     * @return
     */
    private byte[] loadCheckCode(){
        byte[] checkCodeBytes = null;
        try {
            HttpUriRequest loadCheckCodeRequest = HttpManager.get(URL_CHECKCODE_LOAD, new HashMap<String, String>());
            HttpResponse loadChkCodeResp = HttpManager.performRequest(loadCheckCodeRequest);
            if (HttpManager.isRequestSuccessful(loadChkCodeResp)) {
                InputStream entityStream = loadChkCodeResp.getEntity().getContent();
                ByteArrayOutputStream entityOutStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = entityStream.read(buffer)) != -1) {
                    entityOutStream.write(buffer,0,len);
                }
                entityOutStream.flush();
                checkCodeBytes = entityOutStream.toByteArray();
                entityStream.close();
                entityOutStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkCodeBytes;
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

    private class CheckCodeReloadTask implements Runnable {

        private GetLoginCheckCodeCallback callback;

        public CheckCodeReloadTask(GetLoginCheckCodeCallback callback){
            this.callback = callback;
        }

        @Override
        public void run() {
            byte[] checkcodeBytes = loadCheckCode();
            if (parser.isCheckCodeLoadSuccessful(checkcodeBytes)) {
                Bitmap checkcodeBitmap = BitmapFactory.decodeByteArray(checkcodeBytes,0,checkcodeBytes.length);
                callback.onGetCheckCode(checkcodeBitmap);
                Log.d(tag,"login checkcode reload successful");
            } else {
                Log.e(tag,"login checkcode reload error");
                callback.onGetCheckCodeError();
            }
        }
    }

    private class UserLoginTask implements Runnable {

        private GetLoginDataCallback callback;
        private User user;
        private String checkCode;

        public UserLoginTask(User user, String checkCode, GetLoginDataCallback callback) {
            this.checkCode = checkCode;
            this.user = user;
            this.callback = callback;
        }

        @Override
        public void run() {
            String loginContent = login(user, checkCode);
            if (parser.isLoginSuccessful(user,loginContent)){
                Log.d(tag,"user login successfully , userinfo :" + user.toString());
                callback.onGetLoginData(user);
            } else {
                Log.d(tag, "user login failed .");
                if (parser.isViewStateInvalid(loginContent)) {
                    commonDataSource.refreshLoginViewState(new CommonDataSource.RefreshViewStateCallback() {
                        @Override
                        public void onRefreshData(String... stateParam) {
                            //重试
                            run();
                        }

                        @Override
                        public void onRefreshError(String msg) {
                            callback.onGetLoginError(msg);
                        }
                    });
                } else if (parser.isCheckCodeInputMismatch(loginContent)){
                    callback.onGetLoginError(ErrDef.ERR_LOGIN_CHECKCODE);
                } else if (parser.isPswdInputMismatch(loginContent)){
                    callback.onGetLoginError(ErrDef.ERR_LOGIN_IDORPSWD);
                } else {
                    callback.onGetLoginError(ErrDef.ERR_LOGIN_ZFSOFT);
                }
            }

        }
    }

    @Override
    public void loadCheckCode(final GetLoginCheckCodeCallback callback) {
        ThreadPoolUtil.execute(new CheckCodeReloadTask(callback));
    }

    @Override
    public void login(User user, String checkCode, GetLoginDataCallback callback) {
        ThreadPoolUtil.execute(new UserLoginTask(user, checkCode, callback));
    }

    @Override
    public void signIn(User user, GetLoginDataCallback callback) {

    }

    @Override
    public void signUp(User user) {

    }

    @Override
    public void updateLoginUser(User user) {

    }


    @Override
    public void logout() {

    }
}
