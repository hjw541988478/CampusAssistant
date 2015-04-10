package com.ywxy.ca.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.ywxy.ca.R;

public class FloatLabelEditText extends FrameLayout {

	private static final String SAVE_STATE_KEY_EDIT_TEXT = "saveStateEditText";
	private static final String SAVE_STATE_KEY_LABEL = "saveStateLabel";
	private static final String SAVE_STATE_PARENT = "saveStateParent";
	private static final String SAVE_STATE_TAG = "saveStateTag";

	/**
	 * Reference to the EditText
	 */
	private EditText mEditText;

	/**
	 * When init is complete, child views can no longer be added
	 */
	private boolean mInitComplete = false;

	/**
	 * Reference to the TextView used as the label
	 */
	private TextView mLabel;

	/**
	 * LabelAnimator that animates the appearance and disappearance of the label
	 * TextView
	 */
	private LabelAnimator mLabelAnimator = new DefaultLabelAnimator();

	/**
	 * True if the TextView label is showing (alpha 1f)
	 */
	private boolean mLabelShowing;

	/**
	 * Holds saved state if any is waiting to be restored
	 */
	private Bundle mSavedState;

	/**
	 * Interface for providing custom animations to the label TextView.
	 */
	public interface LabelAnimator {

		/**
		 * Called when the label should become visible
		 * 
		 * @param label
		 *            TextView to animate to visible
		 */
		public void onDisplayLabel(View label);

		/**
		 * Called when the label should become invisible
		 * 
		 * @param label
		 *            TextView to animate to invisible
		 */
		public void onHideLabel(View label);
	}

	public FloatLabelEditText(Context context) {
		this(context, null, 0);
	}

	public FloatLabelEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FloatLabelEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	@Override
	public void addView(View child) {
		if (mInitComplete) {
			throw new UnsupportedOperationException(
					"You cannot add child views to a FloatLabel");
		} else {
			super.addView(child);
		}
	}

	@Override
	public void addView(View child, int index) {
		if (mInitComplete) {
			throw new UnsupportedOperationException(
					"You cannot add child views to a FloatLabel");
		} else {
			super.addView(child, index);
		}
	}

	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		if (mInitComplete) {
			throw new UnsupportedOperationException(
					"You cannot add child views to a FloatLabel");
		} else {
			super.addView(child, index, params);
		}
	}

	@Override
	public void addView(View child, int width, int height) {
		if (mInitComplete) {
			throw new UnsupportedOperationException(
					"You cannot add child views to a FloatLabel");
		} else {
			super.addView(child, width, height);
		}
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		if (mInitComplete) {
			throw new UnsupportedOperationException(
					"You cannot add child views to a FloatLabel");
		} else {
			super.addView(child, params);
		}
	}

	/**
	 * Returns the EditText portion of this View
	 * 
	 * @return the EditText portion of this View
	 */
	public EditText getEditText() {
		return mEditText;
	}

	/**
	 * Sets the text to be displayed above the EditText if the EditText is
	 * nonempty or as the EditText hint if it is empty
	 * 
	 * @param resid
	 *            int String resource ID
	 */
	public void setLabel(int resid) {
		setLabel(getContext().getString(resid));
	}

	/**
	 * Sets the text to be displayed above the EditText if the EditText is
	 * nonempty or as the EditText hint if it is empty
	 * 
	 * @param hint
	 *            CharSequence to set as the label
	 */
	public void setLabel(CharSequence hint) {
		mEditText.setHint(hint);
		mLabel.setText(hint);
	}

	/**
	 * Specifies a new LabelAnimator to handle calls to show/hide the label
	 * 
	 * @param labelAnimator
	 *            LabelAnimator to use; null causes use of the default
	 *            LabelAnimator
	 */
	public void setLabelAnimator(LabelAnimator labelAnimator) {
		if (labelAnimator == null) {
			mLabelAnimator = new DefaultLabelAnimator();
		} else {
			mLabelAnimator = labelAnimator;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		final int childLeft = getPaddingLeft();
		final int childRight = right - left - getPaddingRight();

		int childTop = getPaddingTop();
		final int childBottom = bottom - top - getPaddingBottom();

		layoutChild(mLabel, childLeft, childTop, childRight, childBottom);
		layoutChild(mEditText, childLeft,
				childTop + mLabel.getMeasuredHeight(), childRight, childBottom);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void layoutChild(View child, int parentLeft, int parentTop,
			int parentRight, int parentBottom) {
		if (child.getVisibility() != GONE) {
			final LayoutParams lp = (LayoutParams) child.getLayoutParams();

			final int width = child.getMeasuredWidth();
			final int height = child.getMeasuredHeight();

			int childLeft;
			final int childTop = parentTop + lp.topMargin;

			int gravity = lp.gravity;
			if (gravity == -1) {
				gravity = Gravity.TOP | Gravity.START;
			}

			final int layoutDirection;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				layoutDirection = LAYOUT_DIRECTION_LTR;
			} else {
				layoutDirection = getLayoutDirection();
			}

			final int absoluteGravity = GravityCompat.getAbsoluteGravity(
					gravity, layoutDirection);

			switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
			case Gravity.CENTER_HORIZONTAL:
				childLeft = parentLeft + (parentRight - parentLeft - width) / 2
						+ lp.leftMargin - lp.rightMargin;
				break;
			case Gravity.RIGHT:
				childLeft = parentRight - width - lp.rightMargin;
				break;
			case Gravity.LEFT:
			default:
				childLeft = parentLeft + lp.leftMargin;
			}

			child.layout(childLeft, childTop, childLeft + width, childTop
					+ height);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Restore any state that's been pending before measuring
		if (mSavedState != null) {
			Parcelable childState = mSavedState
					.getParcelable(SAVE_STATE_KEY_EDIT_TEXT);
			mEditText.onRestoreInstanceState(childState);
			childState = mSavedState.getParcelable(SAVE_STATE_KEY_LABEL);
			mLabel.onRestoreInstanceState(childState);
			mSavedState = null;
		}
		measureChild(mEditText, widthMeasureSpec, heightMeasureSpec);
		measureChild(mLabel, widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			final Bundle savedState = (Bundle) state;
			if (savedState.getBoolean(SAVE_STATE_TAG, false)) {
				// Save our state for later since children will have theirs
				// restored after this
				// and having more than one FloatLabel in an Activity or
				// Fragment means you have
				// multiple views of the same ID
				mSavedState = savedState;
				super.onRestoreInstanceState(savedState
						.getParcelable(SAVE_STATE_PARENT));
				return;
			}
		}

		super.onRestoreInstanceState(state);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final Bundle saveState = new Bundle();
		saveState.putParcelable(SAVE_STATE_KEY_EDIT_TEXT,
				mEditText.onSaveInstanceState());
		saveState.putParcelable(SAVE_STATE_KEY_LABEL,
				mLabel.onSaveInstanceState());
		saveState.putBoolean(SAVE_STATE_TAG, true);
		saveState.putParcelable(SAVE_STATE_PARENT, superState);

		return saveState;
	}

	private int measureHeight(int heightMeasureSpec) {
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);

		int result = 0;
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = mEditText.getMeasuredHeight() + mLabel.getMeasuredHeight();
			result += getPaddingTop() + getPaddingBottom();
			result = Math.max(result, getSuggestedMinimumHeight());

			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	private int measureWidth(int widthMeasureSpec) {
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		int result = 0;
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = Math.max(mEditText.getMeasuredWidth(),
					mLabel.getMeasuredWidth());
			result = Math.max(result, getSuggestedMinimumWidth());
			result += getPaddingLeft() + getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		// Load custom attributes
		final int layout;
		final CharSequence text;
		final CharSequence hint;
		final ColorStateList hintColor;
		final int floatLabelColor;
		final int inputType;

		if (attrs == null) {
			layout = R.layout.float_label;
			text = null;
			hint = null;
			hintColor = null;
			floatLabelColor = 0;
			inputType = 0;
		} else {
			final TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.FloatLabelEditText, defStyle, 0);

			layout = a.getResourceId(
					R.styleable.FloatLabelEditText_android_layout,
					R.layout.float_label);
			text = a.getText(R.styleable.FloatLabelEditText_android_text);
			hint = a.getText(R.styleable.FloatLabelEditText_android_hint);
			hintColor = a
					.getColorStateList(R.styleable.FloatLabelEditText_android_textColorHint);
			floatLabelColor = a.getColor(
					R.styleable.FloatLabelEditText_floatLabelColor, 0);
			inputType = a.getInt(
					R.styleable.FloatLabelEditText_android_inputType,
					InputType.TYPE_CLASS_TEXT);
			a.recycle();
		}

		inflate(context, layout, this);
		mEditText = (EditText) findViewById(R.id.edit_text);
		if (mEditText == null) {
			throw new RuntimeException(
					"Your layout must have an EditText whose ID is @id/edit_text");
		}

		if (!TextUtils.isEmpty(hint)) {
			mEditText.setHint(hint);
		}

		if (!TextUtils.isEmpty(text)) {
			mEditText.setText(text);
		}

		if (hintColor != null) {
			mEditText.setHintTextColor(hintColor);
		}
		if (inputType != 0) {
			mEditText.setInputType(inputType);
		}

		mLabel = (TextView) findViewById(R.id.float_label);
		if (mLabel == null) {
			throw new RuntimeException(
					"Your layout must have a TextView whose ID is @id/float_label");
		}
		mLabel.setText(mEditText.getHint());
		if (floatLabelColor != 0)
			mLabel.setTextColor(floatLabelColor);

		// Listen to EditText to know when it is empty or nonempty
		mEditText.addTextChangedListener(new EditTextWatcher());

		// Check current state of EditText
		if (TextUtils.isEmpty(mEditText.getText())) {
			ViewHelper.setAlpha(mLabel, 0);
			mLabelShowing = false;
		} else {
			mLabel.setVisibility(View.VISIBLE);
			mLabelShowing = true;
		}

		// Mark init as complete to prevent accidentally breaking the view by
		// adding children
		mInitComplete = true;
	}

	// Set the error Message
	public void setError(String message) {
		mEditText.setError(message);
	}

	// getting the text
	public String getText() {
		return mEditText.getText().toString();
	}

	// Set the MultiLine Text
	public void setMultiLine() {
		mEditText.setSingleLine(false);
	}

	private static class DefaultLabelAnimator implements LabelAnimator {

		@Override
		public void onDisplayLabel(View label) {
			final float offset = label.getHeight() / 2;
			final float currentY = ViewHelper.getY(label);
			if (currentY != offset) {
				ViewHelper.setY(label, offset);
			}
			ViewPropertyAnimator.animate(label).alpha(1).y(0);
		}

		@Override
		public void onHideLabel(View label) {
			final float offset = label.getHeight() / 2;
			final float currentY = ViewHelper.getY(label);
			if (currentY != 0) {
				ViewHelper.setY(label, 0);
			}
			ViewPropertyAnimator.animate(label).alpha(0).y(offset);
		}
	}

	private class EditTextWatcher implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			// Ignored
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// Ignored
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (TextUtils.isEmpty(s)) {
				// Text is empty; TextView label should be invisible
				if (mLabelShowing) {
					mLabelAnimator.onHideLabel(mLabel);
					mLabelShowing = false;
				}
			} else if (!mLabelShowing) {
				// Text is nonempty; TextView label should be visible
				mLabelShowing = true;
				mLabelAnimator.onDisplayLabel(mLabel);
			}
		}
	}
}
