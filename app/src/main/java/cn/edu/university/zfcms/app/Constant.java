package cn.edu.university.zfcms.app;

import cn.edu.university.zfcms.R;

/**
 * Created by hjw on 16/4/18.
 */
public class Constant {
    public static final String USER_AGENT_DEFAULT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36";

    public static final String DB_NAME = "ZfsoftCampusAssit.db";
    public static final String SP_NAME = "ZfsoftCampusAssitPreference";


    public static final String BASE_ZF_CMS_HOST = "http://210.34.213.88/";
    //主页 xs_main.aspx?xh=1407122139
    public static final String PATH_ZF_CMS_INDEX_PAGE = "xs_main.aspx?xh=%s";
    //个人课表查询 xskbcx.aspx?xh=1407122139&xm=张三&gnmkdm=N121603
    public static final String PATH_ZF_CMS_PERSONAL_COURSES_QUERY = "xskbcx.aspx?xh={userId}&xm={userRealName}&gnmkdm=N121603";
    //个人成绩查询 xscjcx_dq.aspx?xh=1407122139&xm=张三&gnmkdm=N121605
    public static final String PATH_ZF_CMS_PERSONAL_RESULTS_QUERY = "xscjcx_dq.aspx?xh=%s&xm=%s&gnmkdm=N121605";
    //等级考试查询 xsdjkscx.aspx?xh=1407122139&xm=应金鑫&gnmkdm=N121606
    public static final String PATH_ZF_CMS_PERSONAL_GRADES_QUERY = "xsdjkscx.aspx?xh=%s&xm=%s&gnmkdm=N121606";
    public static final String PATH_ZF_CMS_LOGIN = "default2.aspx";
    public static final String PATH_ZF_CMS_CHECK_CODE = "CheckCode.aspx";


    public static final String BASE_ELECTRIC_HOST = "http://218.75.197.120:8021/";
    public static final String PATH_ELECTRIC_INQUIRY = "XSCK/Login_Students.aspx";
    public static final String PATH_ELECTRIC_INQUIRY_CHECK_CODE = "CheckImage.aspx";

    public static int[] menuIds = new int[]{
            R.id.navigation_courses,
            R.id.navigation_exams,
            R.id.navigation_results,
            R.id.navigation_tool,
            R.id.navigation_feedback,
            R.id.navigation_setting,
            R.id.navigation_about
    };
}
