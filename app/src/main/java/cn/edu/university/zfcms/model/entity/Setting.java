package cn.edu.university.zfcms.model.entity;

import java.util.ArrayList;
import java.util.List;

import cn.edu.university.zfcms.util.PreferenceUtil;

/**
 * Created by hjw on 16/4/19.
 */
public class Setting {
    public String userId;
    public String currentTerm;
    public String currentYear;
    public String currentWeek;

    public List<String> ownYears;
    public List<String> ownTerms;

    public Setting() {
        this.userId = PreferenceUtil.getLoginUser().userId;
        this.ownYears = new ArrayList<>();
        this.ownTerms = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Setting{" +
                "currentTerm='" + currentTerm + '\'' +
                ", currentYear='" + currentYear + '\'' +
                ", currentWeek='" + currentWeek + '\'' +
                ", ownYears=" + ownYears +
                ", ownTerms=" + ownTerms +
                '}';
    }
}
