package cn.edu.university.zfcms.model.data.common;

import android.util.Log;

import cn.edu.university.zfcms.base.data.CommonDataSource;
import cn.edu.university.zfcms.model.parser.CommonParser;
import cn.edu.university.zfcms.model.entity.Setting;
import cn.edu.university.zfcms.model.network.BizServiceManager;
import cn.edu.university.zfcms.util.PreferenceUtil;

/**
 * Created by hjw on 16/4/20.
 */
public class CommonDataManager implements CommonDataSource {
    private static final String tag = CommonDataManager.class.getSimpleName();

    private CommonParser parser;

    private static CommonDataManager INSTANCE;

    private Setting mCurrentSetting;

    private CommonDataManager() {
        parser = new CommonParser();
        mCurrentSetting = PreferenceUtil.getSettingConfig();
    }

    public static CommonDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommonDataManager();
        }
        return INSTANCE;
    }

    @Override
    public Setting getCurrentSetting() {
        return mCurrentSetting;
    }

    /**
     * 获取新的Login __VIEWSTATE
     */
    @Override
    public boolean refreshLoginViewState() {
            String newerViewState = BizServiceManager.getInstance().getLoginService()
                    .refreshLoginFormState()
                    .map(s-> parser.parseViewStateParam(s)).blockingGet();
            if (!newerViewState.isEmpty()) {
                Log.d(tag, "login viewstate param load successfully.");
                PreferenceUtil.saveLoginViewStateParam(newerViewState);
                return true;
            }
            Log.e(tag, "get login viewstate param error,please check manually.");
            return false;

    }

    @Override
    public void refreshStudentYearsAndTerms() {
        BizServiceManager.getInstance().getCourseService()
                .queryPersonalCourses(PreferenceUtil.getLoginUser().userId,
                        PreferenceUtil.getLoginUser().userRealName)
                    .doOnSuccess(s -> {
                        parser.parseCollegeYears(s, getCurrentSetting());
                        parser.parseCollegeTerms(s, getCurrentSetting());
                        PreferenceUtil.set(getCurrentSetting(), Setting.class);
                    }).subscribe();
    }

}
