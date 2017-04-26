package cn.edu.university.zfcms.biz.courses;

import cn.edu.university.zfcms.app.Constant;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 025 2017/4/25.
 */

public interface CourseService {
    @GET(Constant.PATH_ZF_CMS_PERSONAL_COURSES_QUERY)
    Single<String> queryPersonalCourses(@Path("userId") String userId, @Path("userRealName") String userRealName);

}
