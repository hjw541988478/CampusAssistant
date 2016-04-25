package cn.edu.university.zfcms.util;

import com.google.gson.Gson;

/**
 * Created by hjw on 16/4/22.
 */
public class GsonUtil {

    private static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }
}
