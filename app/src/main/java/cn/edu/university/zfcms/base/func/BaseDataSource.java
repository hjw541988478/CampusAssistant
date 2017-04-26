package cn.edu.university.zfcms.base.func;

/**
 * Created by hjw on 16/4/20.
 */
public interface BaseDataSource {
    public static final String URL_HOST = Config.BASE_HOST;

    //主页 xs_main.aspx?xh=1407122139
    public static final String URL_INDEX_PAGE = URL_HOST + "/xs_main.aspx?xh=%s";
    //个人课表查询 xskbcx.aspx?xh=1407122139&xm=张三&gnmkdm=N121603
    public static final String URL_PERSONAL_COURSES_QUERY = URL_HOST + "/xskbcx.aspx?xh=%s&xm=%s&gnmkdm=N121603";
    //个人成绩查询 xscjcx_dq.aspx?xh=1407122139&xm=张三&gnmkdm=N121605
    public static final String URL_PERSONAL_RESULTS_QUERY = URL_HOST + "/xscjcx_dq.aspx?xh=%s&xm=%s&gnmkdm=N121605";
    //等级考试查询 xsdjkscx.aspx?xh=1407122139&xm=应金鑫&gnmkdm=N121606
    public static final String URL_PERSONAL_GRADES_QUERY = URL_HOST + "/xsdjkscx.aspx?xh=%s&xm=%s&gnmkdm=N121606";

    public static final String URL_LOGIN = URL_HOST + "/default2.aspx";
    public static final String URL_CHECKCODE_LOAD = URL_HOST + "/CheckCode.aspx";

}
