package cn.edu.university.zfcms.util;

/**
 * Created by hjw on 16/4/16.
 */
public class Util {
    public static String parseUserFieldTextHtml(String htmlContent, int prefixKeys,int suffixKeys) {
        if ( htmlContent == null)
            return "";
        if (prefixKeys < 0 || prefixKeys > htmlContent.length()
                || suffixKeys < 0 || suffixKeys > htmlContent.length()) {
            return htmlContent;
        }

        String sliceHtmlText = "";
        sliceHtmlText = htmlContent.substring(prefixKeys,htmlContent.length() - suffixKeys);
        return sliceHtmlText;
    }
}
