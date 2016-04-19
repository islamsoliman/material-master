package com.rey.material.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;

import com.rey.material.R;
import com.rey.material.drawable.BlankDrawable;
import com.rey.material.util.LocaleUtil;
import com.rey.material.util.ThemeUtil;
import com.rey.material.util.TypefaceUtil;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by MS on 24-Jul-15.
 */
public class MonthPicker extends ListView {

    private MonthAdapter mAdapter;

    private int mTextSize;
    private int mItemHeight;
    private int mSelectionColor;
    private int mAnimDuration;
    private Interpolator mInInterpolator;
    private Interpolator mOutInterpolator;
    private Typeface mTypeface;

    private int mItemRealHeight = -1;
    private int mPadding;
    private int mPositionShift;
    private int mDistanceShift;

    private Paint mPaint;
    private String[] months;

    public interface OnMonthChangedListener {

        void onMonthChanged(int oldValue, int newValue);

    }

    private OnMonthChangedListener mOnMonthChangedListener;

    private static final int[][] STATES = new int[][]{
            new int[]{-android.R.attr.state_checked},
            new int[]{android.R.attr.state_checked},
    };

    private int[] mTextColors = new int[2];

    public MonthPicker(Context context) {
        super(context);
        if (LocaleUtil.getLocale().getLanguage().equalsIgnoreCase("ar")) {
            months = getContext().getResources().getStringArray(R.array.months);

        } else {
            months = DateFormatSymbols.getInstance(LocaleUtil.getLocale()).getMonths();
        }

        init(context, null, 0, 0);
    }

    public MonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (LocaleUtil.getLocale().getLanguage().equalsIgnoreCase("ar")) {
            months = getContext().getResources().getStringArray(R.array.months);

        } else {
            months = DateFormatSymbols.getInstance(LocaleUtil.getLocale()).getMonths();
        }

        init(context, attrs, 0, 0);
    }

    public MonthPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (LocaleUtil.getLocale().getLanguage().equalsIgnoreCase("ar")) {
            months = getContext().getResources().getStringArray(R.array.months);

        } else {
            months = DateFormatSymbols.getInstance(LocaleUtil.getLocale()).getMonths();
        }

        init(context, attrs, defStyleAttr, 0);
    }

    public MonthPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (LocaleUtil.getLocale().getLanguage().equalsIgnoreCase("ar")) {
            months = getContext().getResources().getStringArray(R.array.months);

        } else {
            months = DateFormatSymbols.getInstance(LocaleUtil.getLocale()).getMonths();
        }

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mAdapter = new MonthAdapter();

        setAdapter(mAdapter);
        setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
        setSelector(BlankDrawable.getInstance());
        setDividerHeight(0);
        setCacheColorHint(Color.TRANSPARENT);
        setClipToPadding(false);

        mPadding = ThemeUtil.dpToPx(context, 4);

        applyStyle(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyStyle(int resId) {
        applyStyle(getContext(), null, 0, resId);
    }

    protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YearPicker, defStyleAttr, defStyleRes);
        mTextSize = a.getDimensionPixelSize(R.styleable.YearPicker_dp_yearTextSize, context.getResources().getDimensionPixelOffset(R.dimen.abc_text_size_title_material));
        int month = a.getInteger(R.styleable.YearPicker_dp_year, mAdapter.getMonth());
        //   int yearMin = a.getInteger(R.styleable.YearPicker_dp_yearMin, mAdapter.getMinYear());
        // int yearMax = a.getInteger(R.styleable.YearPicker_dp_yearMax, mAdapter.getMaxYear());
        mItemHeight = a.getDimensionPixelSize(R.styleable.YearPicker_dp_yearItemHeight, ThemeUtil.dpToPx(context, 48));
        mTextColors[0] = a.getColor(R.styleable.YearPicker_dp_textColor, 0xFF000000);
        mTextColors[1] = a.getColor(R.styleable.YearPicker_dp_textHighlightColor, 0xFFFFFFFF);
        mSelectionColor = a.getColor(R.styleable.YearPicker_dp_selectionColor, ThemeUtil.colorPrimary(context, 0xFF000000));
        mAnimDuration = a.getInteger(R.styleable.YearPicker_dp_animDuration, context.getResources().getInteger(android.R.integer.config_mediumAnimTime));
        int resId = a.getResourceId(R.styleable.YearPicker_dp_inInterpolator, 0);
        if (resId != 0)
            mInInterpolator = AnimationUtils.loadInterpolator(context, resId);
        else
            mInInterpolator = new DecelerateInterpolator();
        resId = a.getResourceId(R.styleable.YearPicker_dp_outInterpolator, 0);
        if (resId != 0)
            mOutInterpolator = AnimationUtils.loadInterpolator(context, resId);
        else
            mOutInterpolator = new DecelerateInterpolator();
        String familyName = a.getString(R.styleable.YearPicker_dp_fontFamily);
        int style = a.getInteger(R.styleable.YearPicker_dp_textStyle, Typeface.NORMAL);

        mTypeface = TypefaceUtil.load(context, familyName, style);

        a.recycle();

//        if(yearMax < yearMin)
//            yearMax = Integer.MAX_VALUE;

        if (month < 0) {
            Calendar cal = Calendar.getInstance(LocaleUtil.getLocale());
            month = cal.get(Calendar.MONTH);
        }


        ///////////////////// remeber line 150
        //year = Math.max(yearMin, Math.min(yearMax, year));


        setMonth(month);
        mAdapter.notifyDataSetChanged();
    }


    public void goTo(int month) {
        int position = month - mPositionShift;
        int offset = mDistanceShift;
        if (position < 0) {
            position = 0;
            offset = 0;
        }
        postSetSelectionFromTop(position, offset);
    }

    public void postSetSelectionFromTop(final int position, final int offset) {
        post(new Runnable() {
            @Override
            public void run() {
                setSelectionFromTop(position, offset);
                requestLayout();
            }
        });
    }

    public void setMonth(int month) {
        if (mAdapter.getMonth() == month)
            return;

        mAdapter.setMonth(month);
        goTo(month);
    }

    public int getMonth() {
        return mAdapter.getMonth();
    }

    public void setOnMonthChangedListener(OnMonthChangedListener listener) {
        mOnMonthChangedListener = listener;
    }

    private void measureItemHeight() {
        if (mItemRealHeight > 0)
            return;

        mPaint.setTextSize(mTextSize);
        mItemRealHeight = Math.max(Math.round(mPaint.measureText("9999999999999", 0, 4)) + mPadding * 2, mItemHeight);
    }

    public int getTextSize() {
        return mTextSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureItemHeight();

        if (heightMode != MeasureSpec.EXACTLY) {
            if (heightMode == MeasureSpec.AT_MOST) {
                int num = Math.min(mAdapter.getCount(), heightSize / mItemRealHeight);
                if (num >= 3)
                    heightSize = mItemRealHeight * (num % 2 == 0 ? num - 1 : num);
            } else
                heightSize = mItemRealHeight * mAdapter.getCount();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float shift = (h / (float) mItemRealHeight - 1) / 2;
        mPositionShift = (int) Math.floor(shift);
        mPositionShift = shift > mPositionShift ? mPositionShift + 1 : mPositionShift;
        mDistanceShift = (int) ((shift - mPositionShift) * mItemRealHeight) - getPaddingTop();
        goTo(mAdapter.getMonth());
    }

    private class MonthAdapter extends BaseAdapter implements OnClickListener {

        //    private int mMinYear = 1990;
        //  private int mMaxYear = Integer.MAX_VALUE - 1;
        private int mCurMonth = -1;

        public MonthAdapter() {
        }


        @Override
        public int getCount() {
            return months.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public void setMonth(int month) {
            if (mCurMonth != month) {
                int old = mCurMonth;
                mCurMonth = month;

                CircleCheckedTextView child = (CircleCheckedTextView) MonthPicker.this.getChildAt(old - MonthPicker.this.getFirstVisiblePosition());
                if (child != null)
                    child.setChecked(false);

                child = (CircleCheckedTextView) MonthPicker.this.getChildAt(mCurMonth - MonthPicker.this.getFirstVisiblePosition());
                if (child != null)
                    child.setChecked(true);

                if (mOnMonthChangedListener != null)
                    mOnMonthChangedListener.onMonthChanged(old, mCurMonth);
            }
        }

        public int getMonth() {
            return mCurMonth;
        }

        @Override
        public void onClick(View v) {
            setMonth((Integer) v.getTag());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CircleCheckedTextView v = (CircleCheckedTextView) convertView;
            if (v == null) {
                v = new CircleCheckedTextView(getContext());
                v.setGravity(Gravity.CENTER);
                v.setMinHeight(mItemRealHeight);
                v.setMaxHeight(mItemRealHeight);
                v.setAnimDuration(mAnimDuration);
                v.setInterpolator(mInInterpolator, mOutInterpolator);
                v.setBackgroundColor(mSelectionColor);
                v.setTypeface(mTypeface);
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                v.setTextColor(new ColorStateList(STATES, mTextColors));
                v.setOnClickListener(this);
            }

            int month = (Integer) getItem(position);
            v.setTag(month);
           /* if (LocaleUtil.getLocale().getLanguage().equalsIgnoreCase("ar")) {
                v.setText(String.valueOf(TypefaceUtil.getArNum(month)));
            } else {
                v.setText(String.valueOf(month));
            }*/
            v.setText(months[position]);
            v.setCheckedImmediately(month == mCurMonth);
            return v;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

//        ss.yearMin = mAdapter.getMinYear();
//        ss.yearMax = mAdapter.getMaxYear();
        ss.month = mAdapter.getMonth();

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //setYearRange(ss.yearMin, ss.yearMax);
        setMonth(ss.month);
    }

    static class SavedState extends BaseSavedState {
        //        int yearMin;
//        int yearMax;
        int month;

        /**
         * Constructor called from {@link Switch#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
//            yearMin = in.readInt();
//            yearMax = in.readInt();
//
            month = in.readInt();
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
//            out.writeValue(yearMin);
//            out.writeValue(yearMax);
            out.writeValue(month);
        }

        @Override
        public String toString() {
            return "YearPicker.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " year=" + month + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
