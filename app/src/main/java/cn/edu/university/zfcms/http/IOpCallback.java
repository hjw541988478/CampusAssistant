package cn.edu.university.zfcms.http;

/**
 * Created by hjw on 16/4/15.
 */
public interface IOpCallback {
    void onResp();
    void onError(String msg);
}
