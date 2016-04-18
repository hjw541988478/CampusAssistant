package cn.edu.university.zfcms.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.university.zfcms.biz.courses.CoursesContract;
import cn.edu.university.zfcms.model.BasicCourse;
import cn.edu.university.zfcms.model.Course;
import cn.edu.university.zfcms.data.course.remote.RemoteRawCourse;
import cn.edu.university.zfcms.model.User;
import cn.edu.university.zfcms.util.SpUtil;
import cn.edu.university.zfcms.util.Util;

/**
 * Created by hjw on 16/4/16.
 */
public class CoursesParser implements CoursesContract.Parser{

    private static final String CODE_COURSES_LOAD_SUCCESS = "学期学生个人课程表";
    private static final int FEATURE_GENEREAL_COURSE = 4;

    private static final String PATTERN_COURSE_TAG = "(\\S+\\s*){4,5}";
    private static final String PATTERN_COURSE_WHEN_TO_START = "周[日一二三四五六]\\s?第(([1-9]|10|11),)*([1-9]|10|11)节*\\{第([1-9]|([1-2][0-9]))\\-([1-9]|([1-2][0-9]))周*(\\|[单双]周)?\\}";
    private static final String PATTERN_DAY_OF_WEEK = "周[日一二三四五六]";
    private static final String PATTERN_SECTIONS_OF_DAY = "(([1-9]|10|11),)*|([1-9]|10|11)节";
    private static final String PATTERN_WEEKS = "\\{第([1-9]|([1-2][0-9]))\\-([1-9]|([1-2][0-9]))周*(\\|[单双]周)?\\}";

    // 解析课程上课时间的细节内容
    private void parseCourseWhenToStartText(RemoteRawCourse remoteRawCourse, String courseWhenText) {
        Matcher courseWhenMatcher = Pattern.compile(PATTERN_COURSE_WHEN_TO_START).matcher(courseWhenText);
        if (courseWhenMatcher.matches()) {
            parseDayOfWhen(remoteRawCourse,courseWhenText);
            parseDurationSections(remoteRawCourse,courseWhenText);
            parseWeeks(remoteRawCourse,courseWhenText);
        }
    }

    // 解析周几
    private void parseDayOfWhen(RemoteRawCourse remoteRawCourse, String text) {
        Matcher  matcher = Pattern.compile(PATTERN_DAY_OF_WEEK).matcher(text);
        while (matcher.find()) {
            remoteRawCourse.courseDayOfWeek = matcher.group();
            break;
        }
    }

    // 解析上课节数 周五第3,4,5节{第1-1周|单周}
    private void parseDurationSections(RemoteRawCourse remoteRawCourse, String text) {
        int firstIndex = text.indexOf("第");
        int lastIndex = text.indexOf("节");
        String courseSections = text.substring(firstIndex + 1,lastIndex);
        if (courseSections.length() == 1) {
            remoteRawCourse.courseWhichSectionStart = Integer.parseInt(courseSections);
            remoteRawCourse.courseSectionsDuration = 0;
        } else {
            String[] courseSpiltSections = courseSections.split(",");
            remoteRawCourse.courseWhichSectionStart = Integer.parseInt(courseSpiltSections[0]);
            int lastCourseSection = Integer.parseInt(courseSpiltSections[courseSpiltSections.length - 1]);
            remoteRawCourse.courseSectionsDuration = lastCourseSection - remoteRawCourse.courseWhichSectionStart < 1 ? 1 : lastCourseSection;
        }
    }

    private void refreshCourseWeeks(RemoteRawCourse remoteRawCourse, int startWeekNo, int endWeekNo , boolean isContinus, boolean isOddWeekNo) {
        int curWeekNo ;
        for (int index = startWeekNo ; index <= endWeekNo ; index ++) {
            if (isContinus) {
                curWeekNo = index;
            } else {
                if (isOddWeekNo) {
                    if (index %2 == 0) {
                        continue;
                    } else {
                        curWeekNo = index;
                    }
                } else {
                    if (index %2 == 0) {
                        curWeekNo = index;
                    } else {
                        continue;
                    }
                }
            }
            remoteRawCourse.courseTargetStartWeeks.add(curWeekNo);
        }
    }

    private void parseWeeks(RemoteRawCourse remoteRawCourse, String text) {
        Matcher matcher = Pattern.compile(PATTERN_WEEKS).matcher(text);
        while (matcher.find()) {
            int startPos = matcher.group().indexOf("第");
            int endPos = matcher.group().indexOf("周");
            String[] startWeeks = matcher.group().substring(startPos + 1 , endPos).split("-");
            if (startWeeks.length == 2) {
                int startWeekNo = Integer.parseInt(startWeeks[0]);
                int endWeekNo = Integer.parseInt(startWeeks[1]);
                if (isOddCourse(text)) {
                    refreshCourseWeeks(remoteRawCourse,startWeekNo,endWeekNo,false,true);
                } else if (isBiweeklyCourse(text)) {
                    refreshCourseWeeks(remoteRawCourse,startWeekNo,endWeekNo,false,false);
                } else {
                    refreshCourseWeeks(remoteRawCourse,startWeekNo,endWeekNo, true, false);
                }
            }
            break;
        }
    }

    // 是否为单双周的课程
    // e.g 周三第1,2节{第2-13周|单周}
    private boolean isOddCourse(String text) {
        return text.contains("单");
    }

    private boolean isBiweeklyCourse(String text) {
        return text.contains("双");
    }

    @Override
    public boolean isPersonalCoursesPage(String rawHtml) {
        return isFetchCoursesHtmlSuccessful(rawHtml);
    }

    // 请求加载课表内容成功
    private boolean isFetchCoursesHtmlSuccessful(String htmlContent){
        boolean isFectchSuccess = false;
        Document loadCoursesDoc = Jsoup.parse(htmlContent);
        Element loadFlagElement =  loadCoursesDoc.getElementById("Label1");
        if (loadFlagElement != null && loadFlagElement.hasText()) {
            isFectchSuccess = CODE_COURSES_LOAD_SUCCESS.equals(loadFlagElement.text().trim());
            Element collegeElement = loadCoursesDoc.getElementById("Label7");
            Element majorElement = loadCoursesDoc.getElementById("Label8");
            Element classElement = loadCoursesDoc.getElementById("Label9");
            User loginUser = SpUtil.getLoginUser();
            loginUser.userCollege = collegeElement != null && collegeElement.hasText()
                    ? Util.parseUserFieldTextHtml(collegeElement.text(),3,0) : "";
            loginUser.userMajor = majorElement != null && majorElement.hasText()
                    ? Util.parseUserFieldTextHtml(majorElement.text(),3,0) : "";
            loginUser.userClass = classElement != null && classElement.hasText()
                    ? Util.parseUserFieldTextHtml(classElement.text(),4,0) : "";
            SpUtil.saveLoginUser(loginUser);
        }
        return isFectchSuccess;
    }

    @Override
    public List<Course> parseCourses(String rawCoursesHtml) {
        List<Course> remoteRawCourses = new ArrayList<>();
        Document coursesDoc = Jsoup.parse(rawCoursesHtml);
        Elements coursesWhichClassOfDayElements = coursesDoc.select("#Table1 tr td");
        if (coursesWhichClassOfDayElements != null) {
            for (int classSection = 0; classSection < coursesWhichClassOfDayElements.size(); classSection++) {
                Element courseTagElement = coursesWhichClassOfDayElements.get(classSection);
                Matcher matcher = Pattern.compile(PATTERN_COURSE_TAG).matcher(courseTagElement.text());
                while (matcher.find()) {
                    String[] matchedTexts = matcher.group().split("\\s+");
                    if (matchedTexts.length >= FEATURE_GENEREAL_COURSE) {
                        RemoteRawCourse remoteRawCourse = new RemoteRawCourse();
                        BasicCourse basicCourse = new BasicCourse();
                        basicCourse.setCourseName(matchedTexts[0]);
                        basicCourse.setWhereTeach(matchedTexts[3]);
                        basicCourse.setWhoTeach(matchedTexts[2]);
                        remoteRawCourse.courseBasicInfo = basicCourse;
                        remoteRawCourse.rawCourseWhenToTeach = matchedTexts[1];
                        parseCourseWhenToStartText(remoteRawCourse,matchedTexts[1]);
                        if (matchedTexts.length >= 5) {
                            remoteRawCourse.rawCourseAdjustedNo = matchedTexts[4];
                        }
                        remoteRawCourses.add(remoteRawCourse);
                    }
                }
            }
        }
        return remoteRawCourses;
    }
}
