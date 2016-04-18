package cn.edu.university.zfcms.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hjw on 16/4/15.
 */
public class ThreadPoolUtil {
    private static final ExecutorService mExecutor = Executors.newFixedThreadPool(4);

    public static void execute(Runnable targetTask){
        mExecutor.execute(targetTask);
    }
}
