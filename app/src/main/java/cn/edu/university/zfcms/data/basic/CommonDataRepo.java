package cn.edu.university.zfcms.data.basic;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.HashMap;

import cn.bmob.v3.listener.SaveListener;
import cn.edu.university.zfcms.http.HttpManager;
import cn.edu.university.zfcms.model.Setting;
import cn.edu.university.zfcms.parser.CommonParser;
import cn.edu.university.zfcms.util.PreferenceUtil;

/**
 * Created by hjw on 16/4/20.
 */
public class CommonDataRepo implements CommonDataSource {
    private static final String tag = CommonDataRepo.class.getSimpleName();

    private CommonParser parser;

    private static CommonDataRepo INSTANCE;

    private Setting mCurrentSetting;

    private CommonDataRepo() {
        parser = new CommonParser();
        mCurrentSetting = PreferenceUtil.getSettingConfig();
    }

    public static CommonDataRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommonDataRepo();
        }
        return INSTANCE;
    }
    /**
     * 获取新的Login __VIEWSTATE
     *
     * @return
     */
    private String getNewerLoginStateParam() {
        HttpUriRequest request = HttpManager.get(URL_LOGIN, new HashMap<String, String>());
        HttpResponse response = HttpManager.performRequest(request);
        return HttpManager.parseStringResponse(response);
    }

    private String getNewerMainStateParam(String userId) {
        HttpUriRequest request = HttpManager.get(String.format(URL_INDEX_PAGE, userId), new HashMap<String, String>());
        HttpResponse response = HttpManager.performRequest(request);
        return HttpManager.parseStringResponse(response);
    }

    @Override
    public Setting getCurrentSetting() {
        return mCurrentSetting;
    }

    @Override
    public void refreshLoginViewState(CommonDataSource.RefreshViewStateCallback callback) {
        String rawHtml = getNewerLoginStateParam();
        String newerViewState = parser.parseViewStateParam(rawHtml);
        if (!newerViewState.isEmpty()) {
            Log.d(tag, "login viewstate param load successfully.");
            PreferenceUtil.saveLoginViewStateParam(newerViewState);
            callback.onRefreshData(newerViewState);
        } else {
            callback.onRefreshError("get login viewstate param error,please check manually");
            Log.e(tag, "get login viewstate param error,please check manually :\n" + rawHtml);
        }
    }

    @Override
    public void refreshMainViewState(String userid, RefreshViewStateCallback callback) {
        String rawHtml = getNewerMainStateParam(userid);
        String newerViewState = parser.parseViewStateParam(rawHtml);
        if (!newerViewState.isEmpty()) {
            Log.d(tag, "main viewstate param load successfully.");
            callback.onRefreshData(newerViewState);
        } else {
            callback.onRefreshError("get main viewstate param error,please check manually");
            Log.e(tag, "get main viewstate param error,please check manually :\n" + rawHtml);
        }
    }

    @Override
    public void refreshStudentYearsAndTerms() {
        HttpUriRequest request = HttpManager.get(String.format(
                URL_PERSONAL_COURSES_QUERY, PreferenceUtil.getLoginUser().userId,
                PreferenceUtil.getLoginUser().userRealName), new HashMap<String, String>());
        HttpResponse response = HttpManager.performRequest(request);
        String rawHtml = HttpManager.parseStringResponse(response);
        parser.parseCollegeYears(rawHtml, getCurrentSetting());
        parser.parseCollegeTerms(rawHtml, getCurrentSetting());
        PreferenceUtil.set(getCurrentSetting(), Setting.class);
    }

}
