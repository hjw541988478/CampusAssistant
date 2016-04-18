package cn.edu.university.zfcms.data.login.local;

import cn.edu.university.zfcms.model.User;
import cn.edu.university.zfcms.data.login.LoginDataSource;
import cn.edu.university.zfcms.util.SpUtil;

/**
 * Created by hjw on 16/4/15.
 */
public class LocalLoginDataSource implements LoginDataSource {

    private static LocalLoginDataSource INSTANCE;

    //SP 层存储 同时对密码字段进行加密
    private void encrytpPswd(String pswd){

    }

    private void decryptPswd(String pswd){

    }

    private LocalLoginDataSource(){}

    public static LocalLoginDataSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new LocalLoginDataSource();
        }
        return INSTANCE;
    }

    @Override
    public User getLoginUser() {
        User user = SpUtil.getLoginUser();
        if (user != null) {
            decryptPswd(user.userPswd);
        }
        return user;
    }

    @Override
    public void loadCheckCode(GetLoginCheckCodeCallback callback) {

    }

    @Override
    public void login(User user,GetLoginDataCallback callback) {
        encrytpPswd(user.userPswd);
        SpUtil.saveLoginUser(user);
    }

    @Override
    public void logout() {
        SpUtil.clearLoginUser();
    }
}
