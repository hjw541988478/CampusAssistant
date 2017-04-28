package cn.edu.university.zfcms.base;

/**
 * Created by Administrator on 013 2017/2/13.
 */

public interface BaseView{
    void showErrorMsg(String msg);

    void useNightMode(boolean isNight);

    //=======  State  =======
    void stateError();

    void stateLoading();

    void stateMain();
}

