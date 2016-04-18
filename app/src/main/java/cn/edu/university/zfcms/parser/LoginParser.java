package cn.edu.university.zfcms.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.university.zfcms.data.model.User;
import cn.edu.university.zfcms.login.LoginContract;
import cn.edu.university.zfcms.util.Util;

/**
 * Created by hjw on 16/4/16.
 */
public class LoginParser implements LoginContract.Parser {

    private static final String PREFIX_ERR_VIEW_STATE_TITLE = "ERROR";
    private static final String ERR_LOGIN_TEXT_CHECKCODE = "验证码不正确！！";
    private static final String ERR_LOGIN_TEXT_PSWD = "密码不正确！！";

    @Override
    public boolean isLoginSuccessful(User loginFormUser,String loginUserHtml) {
        boolean isLoginSuccessFul = Jsoup.parse(loginUserHtml).getElementById("xhxm") != null;
        if (isLoginSuccessFul) {
            parseLoiginUser(loginFormUser,loginUserHtml);
        }
        return isLoginSuccessFul;
    }

    private void parseLoiginUser(User loginFormUser,String loginUserHtml) {
        Document userDoc = Jsoup.parse(loginUserHtml);
        Element userNameElement = userDoc.getElementById("xhxm");
        if (userNameElement != null && userNameElement.hasText()) {
            loginFormUser.userName = Util.parseUserFieldTextHtml(userNameElement.text(), 0, 2);
        }
    }

    @Override
    public String parseViewStateParam(String viewstateHtml) {
        String stateVal = "";
        Document stateHeaderDoc = Jsoup.parse(viewstateHtml);
        Elements elements = stateHeaderDoc.getElementsByTag("input");
        for (Element element : elements ){
            if ("__VIEWSTATE".equals(element.attr("name"))){
                stateVal = element.val();
                break;
            }
        }
        return stateVal;
    }

    @Override
    public boolean isCheckCodeLoadSuccessful(byte[] checkcodeBytes){
        return checkcodeBytes != null;
    }

    /**
     * 判断是否 ViewState 失效
     *
     * <div id="title_m">ERROR - 出错啦！</div>
     * @param response
     * @return
     */
    @Override
    public boolean isInvalidViewStatePageShow(String response){
        Element newerStateElement = Jsoup.parse(response).getElementById("title_m");
        if (newerStateElement != null && newerStateElement.hasText()){
            return newerStateElement.text().startsWith(PREFIX_ERR_VIEW_STATE_TITLE);
        }
        return false;
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
