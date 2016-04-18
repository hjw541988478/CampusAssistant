package cn.edu.university.zfcms.data.course.remote;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.university.zfcms.biz.courses.CoursesContract;
import cn.edu.university.zfcms.model.Course;
import cn.edu.university.zfcms.http.HttpManager;
import cn.edu.university.zfcms.model.User;
import cn.edu.university.zfcms.data.course.CourseDataSource;
import cn.edu.university.zfcms.parser.CoursesParser;
import cn.edu.university.zfcms.util.SpUtil;
import cn.edu.university.zfcms.util.ThreadPoolUtil;

/**
 * Created by hjw on 16/4/15.
 */
public class RemoteCourseDataSource implements CourseDataSource {

    private static final String tag = RemoteCourseDataSource.class.getSimpleName();

    private static final String URL_COURSE_LOAD = "http://210.34.213.88/xskbcx.aspx?xh=%s&xm=%s&gnmkdm=N121603";

    private static RemoteCourseDataSource INSTANCE ;

    private CoursesContract.Parser parser;

    private RemoteCourseDataSource(){
        parser = new CoursesParser();
    }

    public static RemoteCourseDataSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new RemoteCourseDataSource();
        }
        return INSTANCE;
    }

    private String loadAllCourses(User user){
        Map<String,String> headers = new HashMap<>();
        headers.put("Referer",String.format("http://210.34.213.88/xs_main.aspx?xh=%s",user.userId));
        HttpUriRequest courseLoadRequest = HttpManager.get(String.format(URL_COURSE_LOAD,user.userId,user.userName),headers);
        HttpResponse courseLoadResp = HttpManager.performRequest(courseLoadRequest);
        return HttpManager.parseStringResponse(courseLoadResp);
    }

    // 解析课表
    private List<Course> parseHtmlCourses(String htmlCourses) {
        return parser.parseCourses(htmlCourses);
    }

    private class LoadAllCoursesTask implements Runnable {

        LoadCoursesCallback callback;

        public LoadAllCoursesTask(LoadCoursesCallback callback){
            this.callback = callback;
        }


        @Override
        public void run() {
            String courseHtml = loadAllCourses(SpUtil.getLoginUser());
            Log.d(tag,"loading all courses raw html :" + courseHtml);
            if (parser.isPersonalCoursesPage(courseHtml)) {
                callback.onCoursesLoaded(parseHtmlCourses(courseHtml));
            } else {
                callback.onDataError();
            }
        }
    }

    @Override
    public void loadRawCourses(LoadCoursesCallback callback) {
        ThreadPoolUtil.execute(new LoadAllCoursesTask(callback));
    }

    // DB 层去处理
    @Override
    public void getLocalWeekTimetableCourses(int currentWeekNo, LoadCoursesCallback callback) {

    }

    @Override
    public void getLocalTimetableCourse(String courseId,LoadCourseCallback callback) {

    }

    @Override
    public void saveTimetableCourses(List<Course> courses) {

    }

    @Override
    public void saveTimetableCourse(Course remoteRawCourse) {

    }

    @Override
    public void updateTimetableCourse(Course remoteRawCourse) {

    }

    @Override
    public void deleteTimetableCourse(String courseId) {

    }

    @Override
    public void deleteAllCourses() {

    }
}
