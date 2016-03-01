package com.ywxy.ca.util;

import android.content.Context;
import android.widget.Toast;

public class ViewUtil {
    private static long lastClickTime;

    public static void toastText(Context context, String text, boolean flag) {
        Toast.makeText(context, text, flag == true ? 1 : 0).show();
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
