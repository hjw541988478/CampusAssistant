package com.ywxy.ca.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ywxy.ca.R;

import java.util.Random;

public class TimetableActivity extends Activity {

    // /** 第一个无内容的格子 */
    protected TextView empty;
    // /** 星期一的格子 */
    // protected TextView monColum;
    // /** 星期二的格子 */
    // protected TextView tueColum;
    // /** 星期三的格子 */
    // protected TextView wedColum;
    // /** 星期四的格子 */
    // protected TextView thrusColum;
    // /** 星期五的格子 */
    // protected TextView friColum;
    // /** 星期六的格子 */
    // protected TextView satColum;
    // /** 星期日的格子 */
    // protected TextView sunColum;
    /**
     * 课程表body部分布局
     */
    protected RelativeLayout course_table_layout;
    /**
     * 屏幕宽度
     **/
    protected int screenWidth;
    /**
     * 课程格子平均宽度
     **/
    protected int aveWidth;
    // 五种颜色的背景
    int[] background = {R.drawable.bg_class_blue,
            R.drawable.bg_class_green, R.drawable.bg_class_red,
            R.drawable.bg_class_red, R.drawable.bg_class_yellow};
    private int gridHeight;

    private int getRandomColor() {
        int num = new Random().nextInt(5);
        return background[num];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        // 获得列头的控件
        empty = (TextView) this.findViewById(R.id.tv_head_month);
        // monColum = (TextView) this.findViewById(R.id.test_monday_course);
        // tueColum = (TextView) this.findViewById(R.id.test_tuesday_course);
        // wedColum = (TextView) this.findViewById(R.id.test_wednesday_course);
        // thrusColum = (TextView) this.findViewById(R.id.test_thursday_course);
        // friColum = (TextView) this.findViewById(R.id.test_friday_course);
        // satColum = (TextView) this.findViewById(R.id.test_saturday_course);
        // sunColum = (TextView) this.findViewById(R.id.test_sunday_course);
        course_table_layout = (RelativeLayout) this
                .findViewById(R.id.rl_class_container);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 屏幕宽度
        int width = dm.widthPixels;
        // 平均宽度
        int aveWidth = width / 8;
        // 第一个空白格子设置为25宽
        // empty.setWidth(aveWidth * 3 / 4);
        // monColum.setWidth(aveWidth * 33 / 32 + 1);
        // tueColum.setWidth(aveWidth * 33 / 32 + 1);
        // wedColum.setWidth(aveWidth * 33 / 32 + 1);
        // thrusColum.setWidth(aveWidth * 33 / 32 + 1);
        // friColum.setWidth(aveWidth * 33 / 32 + 1);
        // satColum.setWidth(aveWidth * 33 / 32 + 1);
        // sunColum.setWidth(aveWidth * 33 / 32 + 1);
        this.screenWidth = width;
        this.aveWidth = aveWidth;
        int height = dm.heightPixels;
        int gridHeight = height / 8;
        this.gridHeight = gridHeight;
        // 设置课表界面
        // 动态生成8 * maxCourseNum个textview
        for (int i = 1; i <= 8; i++) {

            for (int j = 1; j <= 8; j++) {

                TextView tx = new TextView(this);
                tx.setId((i - 1) * 8 + j);
                // 除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                if (j == 1) {
                    tx.setBackgroundDrawable(TimetableActivity.this.getResources()
                            .getDrawable(R.drawable.bg_class_head_title));
                } else {
                    tx.setBackgroundDrawable(TimetableActivity.this.getResources()
                            .getDrawable(R.drawable.bg_class_container));
                }
                // 相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        aveWidth, gridHeight);
                // 文字对齐方式
                tx.setGravity(Gravity.CENTER);
                // 字体样式
                tx.setTextAppearance(this, R.style.courseTableText);
                // 如果是第一列，需要设置课的序号（1 到 12）
                if (j == 1) {
                    tx.setText(String.valueOf(i));
                    rp.width = aveWidth;
                    // 设置他们的相对位置
                    if (i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                } else {
                    rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
                    rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
                    tx.setText("");
                }

                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }

        // 添加课程信息
        // TextView courseInfo = new TextView(this);
        // courseInfo.setText("软件工程\n@302");
        // // 该textview的高度根据其节数的跨度来设置
        // RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
        // aveWidth, (gridHeight - 5) * 2);
        // // textview的位置由课程开始节数和上课的时间（day of week）确定
        // rlp.topMargin = (1 - 1) * gridHeight;
        // rlp.leftMargin = 0;
        // // 偏移由这节课是星期几决定
        // rlp.addRule(RelativeLayout.RIGHT_OF, 1);
        // // 字体剧中
        // courseInfo.setGravity(Gravity.CENTER);
        // // 设置一种背景
        // courseInfo.setBackgroundResource(background[1]);
        // courseInfo.setTextSize(12);
        // courseInfo.setLayoutParams(rlp);
        // courseInfo.setTextColor(Color.WHITE);
        // 设置不透明度
        // courseInfo.getBackground().setAlpha(222);
        course_table_layout
                .addView(addNewClass(this, "计算机网络\n@公共414", 3, 2, 1));
        course_table_layout.addView(addNewClass(this, "大学物理(上)\n@公共212", 1, 2,
                2));
        course_table_layout.addView(addNewClass(this, "离散数学\n@公共401", 5, 2, 2));
        course_table_layout.addView(addNewClass(this, "C语言程序设计\n@公共401", 7, 2,
                2));
        course_table_layout.addView(addNewClass(this, "C语言程序设计\n@公共401", 3, 2,
                3));
        course_table_layout
                .addView(addNewClass(this, "计算机网络\n@计通610", 1, 2, 4));
        course_table_layout.addView(addNewClass(this, "离散数学\n@公共402", 3, 2, 4));
        course_table_layout.addView(addNewClass(this, "大学物理(上)\n@公共210", 5, 2,
                4));
        course_table_layout
                .addView(addNewClass(this, "思想道德修养\n@公共201", 1, 2, 5));
        course_table_layout
                .addView(addNewClass(this, "计算机网络\n@公共414", 7, 2, 5));
        course_table_layout
                .addView(addNewClass(this, "音乐鉴赏\n@公共301", 2, 3, 7));
    }

    /**
     * @param classDesc
     * @param classBeginNo
     * @param classDuration
     * @param weekNum
     * @return
     */
    private TextView addNewClass(Context ctx, String classDesc,
                                 int classBeginNo, int classDuration, int classDayOfWeek) {
        TextView courseInfo = new TextView(ctx);
        courseInfo.setText(classDesc);
        // 该textview的高度根据其节数的跨度来设置
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                aveWidth - 5, gridHeight * classDuration - 5);
        // textview的位置由课程开始节数和上课的时间（day of week）确定
        rlp.topMargin = (classBeginNo - 1) * gridHeight;
        rlp.leftMargin = (classDayOfWeek - 1) * aveWidth;
        // 偏移由这节课是星期几决定
        rlp.addRule(RelativeLayout.RIGHT_OF, (classBeginNo - 1) * 8 + 1);
        // 字体剧中
        courseInfo.setGravity(Gravity.CENTER);
        // 设置一种背景
        courseInfo.setBackgroundResource(getRandomColor());
        courseInfo.setTextSize(12);
        courseInfo.setLayoutParams(rlp);
        courseInfo.setTextColor(Color.WHITE);
        // 设置不透明度
        courseInfo.getBackground().setAlpha(222);
        return courseInfo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
