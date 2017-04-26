package cn.huangjiawen.dev_template.storage.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BizServiceManager {
    private static BizServiceManager ourInstance = new BizServiceManager();

    public static BizServiceManager getInstance() {
        return ourInstance;
    }

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private BizServiceManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new StethoInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
//        loginService = retrofit.create(LoginService.class);
    }

//    public LoginService getLoginService() {
//        return loginService;
//    }

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
