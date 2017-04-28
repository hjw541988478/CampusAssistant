package cn.edu.university.zfcms.model.network.http.api;

import java.util.Map;

import cn.edu.university.zfcms.app.Constants;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by garvin on 025 2017/4/25.
 */

public interface ElectricService {

    @Headers({
            "Host: 218.75.197.120:8021",
            "Connection: keep-alive",
            "Cache-Control: no-cache",
            "Origin: http://218.75.197.120:8021",
            "X-MicrosoftAjax: Delta=true",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Accept: */*",
            "DNT: 1",
            "Referer: http://218.75.197.120:8021/XSCK/Login_Students.aspx",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: zh-CN,zh;q=0.8,en;q=0.6"
    })
    @FormUrlEncoded
    @POST(Constants.PATH_ELECTRIC_INQUIRY)
    Single<String> loadElectricInquiryResult(@FieldMap Map<String,String> requestParam);

    @GET(Constants.PATH_ELECTRIC_INQUIRY)
    Single<String> loadElectricCheckCode();

    @GET(Constants.PATH_ELECTRIC_INQUIRY_CHECK_CODE)
    Single<ResponseBody> loadElectricCheckCodeBytes();
}
