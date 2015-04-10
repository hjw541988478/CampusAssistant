package com.ywxy.ca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class TeamMemberGridView extends GridView {

	public TeamMemberGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TeamMemberGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TeamMemberGridView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpec;

		if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
			heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
		} else {
			heightSpec = heightMeasureSpec;
		}
		super.onMeasure(widthMeasureSpec, heightSpec);
	}

}
