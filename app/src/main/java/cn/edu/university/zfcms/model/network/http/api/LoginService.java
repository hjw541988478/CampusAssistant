package cn.edu.university.zfcms.model.network.http.api;

import java.util.Map;

import cn.edu.university.zfcms.app.Constants;
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
    @POST(Constants.PATH_ZF_CMS_LOGIN)
    @FormUrlEncoded
    Single<String> login(@FieldMap Map<String,String> requestParam);

    @GET(Constants.PATH_ZF_CMS_CHECK_CODE)
    Single<ResponseBody> loadLoginCheckCode();

    @GET(Constants.PATH_ZF_CMS_LOGIN)
    Single<String> refreshLoginFormState();
}
