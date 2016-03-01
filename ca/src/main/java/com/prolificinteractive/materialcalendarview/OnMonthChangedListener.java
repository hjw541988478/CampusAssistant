package com.prolificinteractive.materialcalendarview;

/**
 * The callback used to indicate the user changes the date
 */
public interface OnMonthChangedListener {

    /**
     * Called upon change of the selected day
     *
     * @param widget the view associated with this listener
     * @param date   the date picked
     */
    void onMonthChanged(int month);
}
