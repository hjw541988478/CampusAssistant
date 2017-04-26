package cn.edu.university.zfcms.biz.login;

import java.util.Map;

import cn.edu.university.zfcms.app.Constant;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Administrator on 025 2017/4/25.
 */

public interface LoginService {
    @POST(Constant.PATH_ZF_CMS_LOGIN)
    @FormUrlEncoded
    Single<String> login(@FieldMap Map<String,String> requestParam);

    @GET(Constant.PATH_ZF_CMS_CHECK_CODE)
    Single<ResponseBody> loadLoginCheckCode();

    @GET(Constant.PATH_ZF_CMS_LOGIN)
    Single<String> refreshLoginFormState();
}
