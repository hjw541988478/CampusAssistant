package cn.edu.university.zfcms.base.func;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by hjw on 16/4/20.
 */
public abstract class BaseParser {
    private static final String PREFIX_ERR_VIEW_STATE_TITLE = "ERROR";

    public String parseViewStateParam(String viewstateHtml) {
        String stateVal = "";
        Document stateHeaderDoc = Jsoup.parse(viewstateHtml);
        Elements elements = stateHeaderDoc.getElementsByTag("input");
        for (Element element : elements) {
            if ("__VIEWSTATE".equals(element.attr("name"))) {
                stateVal = element.val();
                break;
            }
        }
        return stateVal;
    }

    /**
     * 判断是否 ViewState 失效
     * <p>
     * <div id="title_m">ERROR - 出错啦！</div>
     *
     * @param response
     * @return
     */
    public boolean isViewStateInvalid(String response) {
        Element newerStateElement = Jsoup.parse(response).getElementById("title_m");
        if (newerStateElement != null && newerStateElement.hasText()) {
            return newerStateElement.text().startsWith(PREFIX_ERR_VIEW_STATE_TITLE);
        }
        return false;
    }
}
