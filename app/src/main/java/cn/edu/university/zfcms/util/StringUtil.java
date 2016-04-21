package cn.edu.university.zfcms.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hjw on 16/4/16.
 */
public class StringUtil {
    public static String parseUserFieldTextHtml(String htmlContent, int prefixKeys, int suffixKeys) {
        if (htmlContent == null)
            return "";
        if (prefixKeys < 0 || prefixKeys > htmlContent.length()
                || suffixKeys < 0 || suffixKeys > htmlContent.length()) {
            return htmlContent;
        }

        String sliceHtmlText = "";
        sliceHtmlText = htmlContent.substring(prefixKeys, htmlContent.length() - suffixKeys);
        return sliceHtmlText;
    }


    public static List<String> stringToList(String weeksStr) {
        List<String> weekList = new ArrayList<>();
        if (weeksStr == null || weekList.isEmpty()) {
            return weekList;
        }
        String[] weeksArray = weeksStr.split(",");
        if (weeksArray.length != 0) {
            weekList = Arrays.asList(weeksArray);
        } else {
            weekList.add(weeksStr);
        }
        return weekList;
    }

    public static String listToString(List<String> weeks) {
        if (weeks == null || weeks.isEmpty()) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (String week : weeks) {
            result.append(week).append(",");
        }
        if (result.length() != 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
