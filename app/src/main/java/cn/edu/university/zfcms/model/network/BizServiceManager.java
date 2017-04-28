package cn.edu.university.zfcms.model.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import cn.edu.university.zfcms.app.Constants;
import cn.edu.university.zfcms.app.App;
import cn.edu.university.zfcms.model.network.http.api.CourseService;
import cn.edu.university.zfcms.model.network.http.api.ElectricService;
import cn.edu.university.zfcms.model.network.http.api.LoginService;
import cn.edu.university.zfcms.util.PreferenceUtil;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BizServiceManager {
    private static final int DEFAULT_TIMEOUT = 15;
    private static BizServiceManager ourInstance = new BizServiceManager();
    private Retrofit electricRetrofit, zfCmsRetrofit;

    private ElectricService electricService;
    private LoginService zfCmsLoginService;
    private CourseService zfCmsCourseService;

    private BizServiceManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                // HTTP请求LOG信息
                .addInterceptor(interceptor)
                // Stetho调试
                .addInterceptor(new StethoInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        zfCmsRetrofit = new Retrofit.Builder()
                .client(client.newBuilder()
                        // 添加统一的Header
                        .addInterceptor(chain -> {
                            Request originalRequest = chain.request();
                            Request.Builder authorised = originalRequest.newBuilder()
                                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                                    .header("Accept-Encoding","gzip, deflate")
                                    .header("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                                    .header("Cache-Control","max-age=0")
                                    .header("DNT","1")
                                    .header("Connection","keep-alive")
                                    .header("Upgrade-Insecure-Requests","1")
                                    .header("Host","210.34.213.88")
                                    .header("Origin", Constants.BASE_ZF_CMS_HOST)
                                    .header("Referer", "http://210.34.213.88/default2.aspx")
                                    .header("User-Agent", Constants.USER_AGENT_DEFAULT);
                            if ("POST".equals(originalRequest.method())) {
                                authorised.header("__VIEWSTATE", PreferenceUtil.getLoginViewStateParam());
                            }
                            return chain.proceed(authorised.build());
                        })
                        .cookieJar(new PersistentCookieJar(new SetCookieCache()
                                , new SharedPrefsCookiePersistor(App.getInstance()))).build())
                .baseUrl(Constants.BASE_ZF_CMS_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        zfCmsLoginService = zfCmsRetrofit.create(LoginService.class);
        zfCmsCourseService = zfCmsRetrofit.create(CourseService.class);

        electricRetrofit = new Retrofit.Builder()
                .client(client.newBuilder()
                        // 添加统一的Header
                        .addInterceptor(chain -> {
                                    Request originalRequest = chain.request();
                                    Request authorised = originalRequest.newBuilder()
                                            .header("User-Agent", Constants.USER_AGENT_DEFAULT)
                                            .build();
                                    return chain.proceed(authorised);
                                }
                        )
                        .cookieJar(new PersistentCookieJar(new SetCookieCache()
                                , new SharedPrefsCookiePersistor(App.getInstance()))).build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.BASE_ELECTRIC_HOST)
                .build();
        electricService = electricRetrofit.create(ElectricService.class);
    }

    public static BizServiceManager getInstance() {
        return ourInstance;
    }

    public ElectricService getElectricService() {
        return electricService;
    }

    public LoginService getLoginService() {
        return zfCmsLoginService;
    }

    public CourseService getCourseService() {
        return  zfCmsCourseService;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public static class ResponseFunc<T> implements Function<Response<T>, T> {

        @Override
        public T apply(Response<T> response) throws Exception {
            if (response.code != 0) {
                throw new ApiException(response.msg);
            }
            return response.data;
        }
    }

    public static class Response<T> implements Serializable {
        public int code;
        public String msg;

        public T data;
    }

    private static class ApiException extends RuntimeException {
        ApiException(String resultCode) {
            super(resultCode);
        }
    }


}
