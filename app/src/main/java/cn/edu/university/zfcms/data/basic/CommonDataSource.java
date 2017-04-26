package cn.edu.university.zfcms.data.basic;

import cn.edu.university.zfcms.base.func.BaseDataSource;
import cn.edu.university.zfcms.model.Setting;

/**
 * Created by hjw on 16/4/20.
 */
public interface CommonDataSource extends BaseDataSource {
    public interface RefreshViewStateCallback {
        void onRefreshData(String... stateParam);

        void onRefreshError(String msg);
    }

    public interface IOpCallback {
        void onResp(String... msg);

        void onError(String errMsg);
    }

    public Setting getCurrentSetting();

    public void refreshLoginViewState(RefreshViewStateCallback callback);

    public void refreshMainViewState(String userId, RefreshViewStateCallback callback);

    public void refreshStudentYearsAndTerms();

}
