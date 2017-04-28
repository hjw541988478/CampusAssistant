package cn.edu.university.zfcms.model.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import cn.edu.university.zfcms.base.BaseParser;
import cn.edu.university.zfcms.model.entity.Setting;

/**
 * Created by hjw on 16/4/22.
 */
public class CommonParser extends BaseParser {
    //<select name="xnd" onchange="__doPostBack('xnd','')" language="javascript" id="xnd">
    //    <option value="2015-2016">2015-2016</option>
    //    <option selected="selected" value="2014-2015">2014-2015</option>
    //</select>
    public void parseCollegeYears(String rawHtml, Setting setting) {
        Element doc = Jsoup.parse(rawHtml).getElementById("xnd");
        for (Node yearNode : doc.childNodes()) {
            if (yearNode.hasAttr("value")) {
                setting.ownYears.add(yearNode.attr("value"));
                if (yearNode.hasAttr("selected")) {
                    setting.currentYear = yearNode.attr("selected");
                }
            }
        }

    }

    //<select name="xqd" onchange="__doPostBack('xqd','')" language="javascript" id="xqd">
    //    <option value="1">1</option>
    //    <option selected="selected" value="2">2</option>
    //    <option value="3">3</option>
    // </select>
    public void parseCollegeTerms(String rawHtml, Setting setting) {
        Element doc = Jsoup.parse(rawHtml).getElementById("xqd");
        for (Node yearNode : doc.childNodes()) {
            if (yearNode.hasAttr("value")) {
                setting.ownTerms.add(yearNode.attr("value"));
                if (yearNode.hasAttr("selected")) {
                    setting.currentTerm = yearNode.attr("selected");
                }
            }
        }
    }
}
