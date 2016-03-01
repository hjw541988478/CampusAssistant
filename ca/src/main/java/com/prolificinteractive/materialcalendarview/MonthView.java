package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.SUNDAY;

/**
 * Display a month of {@linkplain DayView}s and seven {@linkplain WeekDayView}s.
 */
public class MonthView extends GridLayout implements View.OnClickListener {
    private final ArrayList<WeekDayView> weekDayViews = new ArrayList<WeekDayView>();
    private final ArrayList<DayView> monthDayViews = new ArrayList<DayView>();
    private final Calendar calendarOfRecord = CalendarUtils.getInstance();
    private final Calendar tempWorkingCalendar = CalendarUtils.getInstance();
    private HashMap<String, String> calendarMap = new HashMap<String, String>();
    private Callbacks callbacks;
    private int firstDayOfWeek = SUNDAY;
    private CalendarDay selection = null;
    private CalendarDay minDate = null;
    private CalendarDay maxDate = null;
    private boolean showOtherDates = false;
    private Context ctx;
    private int color_selected = Color.parseColor("#949494");

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setColumnCount(8);
        setRowCount(7);

        setClipChildren(false);
        setClipToPadding(false);

        ctx = context;
        calendarMap.put(("201532"), "报到注册");
        calendarMap.put(("201533"), "报到注册");
        calendarMap.put(("201534"), "正式上课");
        calendarMap.put(("201545"), "清明节");
        calendarMap.put(("201551"), "劳动节");
        calendarMap.put(("2015620"), "端午节");
        calendarMap.put(("201579"), "暑假开始");
        calendarMap.put(("2015827"), "暑假结束");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int children = getChildCount();
        for (int i = 0; i < children; i++) {
            View child = getChildAt(i);
            if (child instanceof WeekDayView) {
                weekDayViews.add((WeekDayView) child);
            } else if (child instanceof DayView) {
                monthDayViews.add((DayView) child);
                child.setOnClickListener(this);
            }
        }
        setFirstDayOfWeek(firstDayOfWeek);
        setSelectedDate(new CalendarDay());
    }

    public void setWeekDayTextAppearance(int taId) {
        for (WeekDayView weekDayView : weekDayViews) {
            weekDayView.setTextAppearance(getContext(), taId);
        }
    }

    public void setDateTextAppearance(int taId) {
        for (DayView dayView : monthDayViews) {
            dayView.setTextAppearance(getContext(), taId);
        }
    }

    public boolean getShowOtherDates() {
        return showOtherDates;
    }

    public void setShowOtherDates(boolean show) {
        this.showOtherDates = show;
        updateUi();
    }

    public void setSelectionColor(int color) {
        for (DayView dayView : monthDayViews) {
            dayView.setSelectionColor(color);
        }
    }

    private Calendar resetAndGetWorkingCalendar() {
        CalendarUtils.copyDateTo(calendarOfRecord, tempWorkingCalendar);
        int dow = CalendarUtils.getDayOfWeek(tempWorkingCalendar);
        int delta = firstDayOfWeek - dow;
        // If the delta is positive, we want to remove a week
        boolean removeRow = showOtherDates ? delta >= 0 : delta > 0;
        if (removeRow) {
            delta -= 7;
        }
        tempWorkingCalendar.add(DATE, delta);
        return tempWorkingCalendar;
    }

    public void setFirstDayOfWeek(int dayOfWeek) {
        this.firstDayOfWeek = dayOfWeek;

        Calendar calendar = resetAndGetWorkingCalendar();
        calendar.set(DAY_OF_WEEK, dayOfWeek);
        for (int i = 0; i < weekDayViews.size(); i++) {
            WeekDayView dayView = weekDayViews.get(i);
            if (i == 0)
                dayView.setDayOfWeek(true, calendar);
            else {
                dayView.setDayOfWeek(false, calendar);
                calendar.add(DATE, 1);
            }
        }
    }

    public void setMinimumDate(CalendarDay minDate) {
        this.minDate = minDate;
        updateUi();
    }

    public void setMaximumDate(CalendarDay maxDate) {
        this.maxDate = maxDate;
        updateUi();
    }

    public void setDate(CalendarDay month) {
        month.copyTo(calendarOfRecord);
        CalendarUtils.setToFirstDay(calendarOfRecord);
        updateUi();
    }

    public void setSelectedDate(CalendarDay cal) {
        selection = cal;
        updateUi();
    }

    private void updateUi() {
        int ourMonth = CalendarUtils.getMonth(calendarOfRecord);
        Calendar calendar = resetAndGetWorkingCalendar();
        for (int i = 0; i < monthDayViews.size(); i++) {
            DayView dayView = monthDayViews.get(i);
            CalendarDay day = new CalendarDay(calendar);
            if (i % 8 == 0) {
                dayView.setDay(true, day);
            } else {
                CalendarDay today = new CalendarDay(CalendarUtils.getInstance());
                if (day.toString().equals(today.toString())) {
                    dayView.setBackgroundDrawable(DayView.generateBackground(
                            Color.parseColor("#039BE5"), 200));
                    dayView.setChecked(true);
                    // dayView.setEnabled(false);
                } else if (calendarMap.containsKey(day.toString())) {
                    dayView.setBackgroundDrawable(DayView.generateBackground(
                            Color.parseColor("#F2F2F2"), 200));
                    dayView.setTextColor(Color.BLACK);
                    dayView.setChecked(true);
                    // dayView.setEnabled(false);
                } else {
                    dayView.setChecked(day.equals(selection));
                }
                dayView.setupSelection(showOtherDates,
                        day.isInRange(minDate, maxDate),
                        day.getMonth() == ourMonth);
                dayView.setDay(false, day);
                calendar.add(DATE, 1);
            }
        }
        postInvalidate();
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof DayView) {
            for (DayView other : monthDayViews) {
                other.setChecked(false);
            }
            DayView dayView = (DayView) v;
            dayView.setChecked(true);

            CalendarDay date = dayView.getDate();
            if (date.equals(selection)) {
                return;
            }
            selection = date;

            if (callbacks != null) {
                callbacks.onDateChanged(dayView.getDate());
            }
        }
    }

    public interface Callbacks {

        void onDateChanged(CalendarDay date);
    }
}
