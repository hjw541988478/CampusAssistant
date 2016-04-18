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

import cn.edu.university.zfcms.login.LoginContract;
import cn.edu.university.zfcms.http.HttpManager;
import cn.edu.university.zfcms.data.model.User;
import cn.edu.university.zfcms.data.login.LoginDataSource;
import cn.edu.university.zfcms.http.IOpCallback;
import cn.edu.university.zfcms.parser.LoginParser;
import cn.edu.university.zfcms.util.SpUtil;
import cn.edu.university.zfcms.util.ThreadPoolUtil;

/**
 * Created by hjw on 16/4/14.
 */
public class RemoteLoginDataSource implements LoginDataSource{
    private static final String tag = RemoteLoginDataSource.class.getSimpleName();

    public static final String URL_LOGIN = "http://210.34.213.88/default2.aspx";

    public static final String URL_LOAD_CHECKCODE = "http://210.34.213.88/CheckCode.aspx";

    private LoginContract.Parser parser;

    private static RemoteLoginDataSource INSTANCE;

    private RemoteLoginDataSource(){
        parser = new LoginParser();
    }

    public static RemoteLoginDataSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new RemoteLoginDataSource();
        }
        return INSTANCE;
    }


    /**
     * 获取新的__VIEWSTATE
     * @return
     */
    private String getNewerStateHeader(){
        HttpUriRequest request = HttpManager.get(URL_LOGIN, new HashMap<String, String>());
        HttpResponse response = HttpManager.performRequest(request);
        return HttpManager.parseStringResponse(response);
    }

    /**
     * 登录
     */
    private String login(User user){
        Map<String,String> params = buildLoginParams(user.userId,user.userPswd,user.checkCode);
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
            HttpUriRequest loadCheckCodeRequest = HttpManager.get(URL_LOAD_CHECKCODE,new HashMap<String, String>());
            HttpResponse loadChkCodeResp = HttpManager.performRequest(loadCheckCodeRequest);
            if (HttpManager.isRequestSuccessful(loadChkCodeResp.getStatusLine().getStatusCode())) {
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
            Log.d(tag,"login checkcode reloading ...");
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

    private class NewerStateHeaderTask implements Runnable {

        IOpCallback callback;

        public NewerStateHeaderTask(IOpCallback callback){
            this.callback = callback;
        }

        @Override
        public void run() {
            Log.d(tag,"login new state header loading ...");
            String rawHtml = getNewerStateHeader();
            String newerHeader = parser.parseViewStateParam(rawHtml);
            if (!newerHeader.isEmpty()) {
                SpUtil.saveNewerStateHeader(newerHeader);
                callback.onResp();
            } else {
                callback.onError("get newer state header error,please check manually");
                Log.e(tag,"get newer state header error,please check manually :\n" + rawHtml);
            }
        }
    }


    private class UserLoginTask implements Runnable {

        private GetLoginDataCallback callback;
        private User user;

        public UserLoginTask(User user,GetLoginDataCallback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        public void run() {
            String loginContent = login(user);
            if (parser.isLoginSuccessful(user,loginContent)){
                Log.d(tag,"user login successfully , userinfo :" + user.toString());
                callback.onGetLoginData(user);
            } else {
                Log.d(tag, "user login failed .");
                if (parser.isInvalidViewStatePageShow(loginContent)) {
                    ThreadPoolUtil.execute(new NewerStateHeaderTask(new IOpCallback() {
                        @Override
                        public void onResp() {
                            Log.d(tag,"reload viewstate header successfully.");
                            // 获取成功 重新登录
                            ThreadPoolUtil.execute(new UserLoginTask(user, callback));
                        }

                        @Override
                        public void onError(String msg) {
                            callback.onGetLoginError(msg);
                        }
                    }));
                } else if (parser.isCheckCodeInputMismatch(loginContent)){
                    callback.onGetLoginError("验证码不正确");
                } else if (parser.isPswdInputMismatch(loginContent)){
                    callback.onGetLoginError("学号或密码不正确");
                } else {
                    callback.onGetLoginError("教务系统出现故障请稍候重试.");
                }
            }

        }
    }

    @Override
    public void loadCheckCode(final GetLoginCheckCodeCallback callback) {
        ThreadPoolUtil.execute(new CheckCodeReloadTask(callback));
    }

    @Override
    public void login(User user, final GetLoginDataCallback callback) {
        ThreadPoolUtil.execute(new UserLoginTask(user,callback));
    }

    @Override
    public void logout() {

    }
}
