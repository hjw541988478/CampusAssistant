package cn.edu.university.zfcms.data.basic;

import cn.edu.university.zfcms.app.mvp.BaseDataSource;
import cn.edu.university.zfcms.storage.entity.Setting;

/**
 * Created by hjw on 16/4/20.
 */
public interface CommonDataSource extends BaseDataSource {
    public Setting getCurrentSetting();

    public boolean refreshLoginViewState();

    public void refreshStudentYearsAndTerms();

}
