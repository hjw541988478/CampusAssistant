package cn.edu.university.zfcms.data.course.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.edu.university.zfcms.model.Course;

/**
 * Created by hjw on 16/4/15.
 */
public class RemoteRawCourse  extends Course {
    public List<Integer> courseTargetStartWeeks = new ArrayList<>();

    public String rawCourseWhenToTeach;
    public String rawCourseAdjustedNo;

    public RemoteRawCourse() {
        this.timeCourseId = UUID.randomUUID().toString();
    }
}
