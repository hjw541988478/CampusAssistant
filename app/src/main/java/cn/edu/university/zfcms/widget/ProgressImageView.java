package cn.edu.university.zfcms.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by garvin on 17/4/28.
 */
public class ProgressImageView extends AppCompatImageView {


    public ProgressImageView(Context context) {
        super(context);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        setVisibility(View.VISIBLE);
        Animatable animatable = (Animatable) getDrawable();
        if (!animatable.isRunning()) {
            animatable.start();
        }
    }

    public void stop() {
        Animatable animatable = (Animatable) getDrawable();
        if (animatable.isRunning()) {
            animatable.stop();
        }
        setVisibility(View.GONE);
    }
}
