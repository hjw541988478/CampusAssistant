package cn.edu.university.zfcms.base.data;

import cn.edu.university.zfcms.base.BaseDataSource;
import cn.edu.university.zfcms.model.entity.Setting;

/**
 * Created by hjw on 16/4/20.
 */
public interface CommonDataSource extends BaseDataSource {
    public Setting getCurrentSetting();

    public boolean refreshLoginViewState();

    public void refreshStudentYearsAndTerms();

}
