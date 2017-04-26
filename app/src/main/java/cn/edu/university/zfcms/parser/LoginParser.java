package cn.edu.university.zfcms.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.edu.university.zfcms.model.User;
import cn.edu.university.zfcms.biz.login.LoginContract;
import cn.edu.university.zfcms.util.StringUtil;

/**
 * Created by hjw on 16/4/16.
 */
public class LoginParser extends LoginContract.Parser {

    private static final String ERR_LOGIN_TEXT_CHECKCODE = "验证码不正确！！";
    private static final String ERR_LOGIN_TEXT_PSWD = "密码不正确！！";

    private void parseLoiginUser(User loginFormUser,String loginUserHtml) {
        Document userDoc = Jsoup.parse(loginUserHtml);
        Element userNameElement = userDoc.getElementById("xhxm");
        if (userNameElement != null && userNameElement.hasText()) {
            loginFormUser.userRealName = StringUtil.parseUserFieldTextHtml(userNameElement.text(), 0, 2);
        }
    }

    @Override
    public boolean isLoginSuccessful(User loginFormUser, String loginUserHtml) {
        boolean isLoginSuccessFul = Jsoup.parse(loginUserHtml).getElementById("xhxm") != null;
        if (isLoginSuccessFul) {
            parseLoiginUser(loginFormUser, loginUserHtml);
        }
        return isLoginSuccessFul;
    }

    @Override
    public boolean isCheckCodeLoadSuccessful(byte[] checkCodeBytes) {
        return checkCodeBytes != null;
    }

    @Override
    public boolean isCheckCodeInputMismatch(String htmlResp){
        return htmlResp.contains(ERR_LOGIN_TEXT_CHECKCODE);
    }

    @Override
    public boolean isPswdInputMismatch(String html) {
        return html.contains(ERR_LOGIN_TEXT_PSWD);
    }

}
