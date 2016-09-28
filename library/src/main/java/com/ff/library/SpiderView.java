package com.ff.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by feifan on 2016/9/27.
 * Contacts me:404619986@qq.com
 */

public class SpiderView extends View {
    private enum Location {
        NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    private int mAngleCount;//蛛网图角数
    private int mHierarchyCount;//蛛网图层级数
    private float mMaxValue;//设置蛛网最大值
    private int mLineColor;//设置蛛网颜色
    private float mLineWidth;//设置蛛网线宽
    private Paint mLinePaint;
    private boolean applyFillColor;
    private int mFillColor;
    private Paint mFillPaint;
    private int mOverlayColor;//设置填充图形颜色
    private int mOverlayAlpha;//设置填充图形透明度(0-255)
    private Paint mOverlayPaint;
    private boolean isDrawOverlayStroke;//填充图形是否绘制描边
    private int mOverlayStrokeColor;//填充图形描边颜色
    private Paint mStrokePaint;
    private boolean isDrawPoint;//是否绘制点
    private int mTextColor;//文字颜色
    private float mTextSize;//文字大小
    private Paint mTextPaint;

    private String[] mTitles;
    private float[] mValues;
    private float mRadius;
    private float mRadian;
    private float mOffsetRadian;
    private float mCenterX;
    private float mCenterY;
    private Path mPath;

    public SpiderView(Context context) {
        this(context, null);
    }

    public SpiderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpiderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaints();
        initParams();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpiderView);

        mAngleCount = typedArray.getInt(R.styleable.SpiderView_angleCount, 6);
        mHierarchyCount = typedArray.getInt(R.styleable.SpiderView_hierarchyCount, 6);
        mMaxValue = typedArray.getFloat(R.styleable.SpiderView_maxValue, 100);
        mLineColor = typedArray.getColor(R.styleable.SpiderView_lineColor, Color.BLACK);
        mLineWidth = typedArray.getDimension(R.styleable.SpiderView_lineWidth, 5f);
        applyFillColor = typedArray.getBoolean(R.styleable.SpiderView_lineColor, true);
        mFillColor = typedArray.getColor(R.styleable.SpiderView_fillColor, Color.BLUE);
        mOverlayColor = typedArray.getColor(R.styleable.SpiderView_overlayColor, Color.YELLOW);
        mOverlayAlpha = typedArray.getInt(R.styleable.SpiderView_overlayAlpha, 155);
        isDrawOverlayStroke = typedArray.getBoolean(R.styleable.SpiderView_drawOverlayStroke, false);
        mOverlayStrokeColor = typedArray.getColor(R.styleable.SpiderView_overlayStrokeColor, Color.CYAN);
        isDrawPoint = typedArray.getBoolean(R.styleable.SpiderView_drawPoint, false);
        mTextColor = typedArray.getColor(R.styleable.SpiderView_textColor, Color.BLACK);
        mTextSize = typedArray.getDimension(R.styleable.SpiderView_textSize, dpToPx(10f));

        typedArray.recycle();
    }

    private void initPaints() {
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setColor(mLineColor);
        if (mLineWidth > 0) {
            mLinePaint.setStrokeWidth(mLineWidth);
        }

        if (applyFillColor) {
            mFillPaint = new Paint();
            mFillPaint.setAntiAlias(true);
            mFillPaint.setDither(true);
            mFillPaint.setStyle(Paint.Style.FILL);
            mFillPaint.setColor(mFillColor);
        }

        mOverlayPaint = new Paint();
        mOverlayPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mOverlayPaint.setColor(mOverlayColor);
        if (mOverlayAlpha < 0 || mOverlayAlpha > 255) {
            mOverlayAlpha = 155;
        }
        mOverlayPaint.setAlpha(mOverlayAlpha);

        if (isDrawOverlayStroke) {
            mStrokePaint = new Paint();
            mStrokePaint.setStyle(Paint.Style.STROKE);
            mStrokePaint.setAntiAlias(true);
            mStrokePaint.setDither(true);
            if (mLineWidth > 0) {
                mStrokePaint.setStrokeWidth(mLineWidth);
            }
            mStrokePaint.setColor(mOverlayStrokeColor);
        }

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    private void initParams() {
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(233, 233);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 233);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(233, heightSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mAngleCount >= 3 && mHierarchyCount > 0) {
            mRadius = Math.min(w, h) / 2 * 0.8f;
            mRadian = (float) (Math.PI * 2 / mAngleCount);
            mOffsetRadian = mAngleCount % 2 == 0 ? mRadian / 2 : 0;
            mCenterX = w / 2;
            mCenterY = h / 2;
            mFillPaint.setAlpha(255 / mAngleCount);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawNet(canvas);
        drawOverlay(canvas);
        drawText(canvas);
    }

    private void drawNet(Canvas canvas) {
        float radiusOffset = mRadius / mHierarchyCount;
        for (int w = 0; w < mHierarchyCount; w++) {
            drawHierarchy(canvas, radiusOffset * (w + 1));
        }

        drawAxes(canvas);
    }

    private void drawHierarchy(Canvas canvas, float radius) {
        mPath.reset();

        float nextRadian;
        float nextX;
        float nextY;
        for (int i = 0; i < mAngleCount; i++) {
            nextRadian = mOffsetRadian + mRadian * i;
            nextX = (float) (mCenterX + radius * Math.sin(nextRadian));
            nextY = (float) (mCenterY - radius * Math.cos(nextRadian));

            if (i == 0) {
                mPath.moveTo(nextX, nextY);
            } else {
                mPath.lineTo(nextX, nextY);
            }
        }

        mPath.close();
        canvas.drawPath(mPath, mLinePaint);
        if (applyFillColor) {
            canvas.drawPath(mPath, mFillPaint);
        }
    }

    private void drawAxes(Canvas canvas) {
        float nextRadian;
        float nextX;
        float nextY;
        for (int i = 0; i < mAngleCount; i++) {
            mPath.reset();
            mPath.moveTo(mCenterX, mCenterY);
            nextRadian = mOffsetRadian + mRadian * i;
            nextX = (float) (mCenterX + mRadius * Math.sin(nextRadian));
            nextY = (float) (mCenterY - mRadius * Math.cos(nextRadian));
            mPath.lineTo(nextX, nextY);
            canvas.drawPath(mPath, mLinePaint);
        }
    }

    private void drawOverlay(Canvas canvas) {
        if (mValues == null || mValues.length == 0) {
            return;
        }
        mPath.reset();

        float nextRadian;
        float nextX;
        float nextY;
        float radio;
        for (int i = 0; i < mAngleCount; i++) {
            if (i >= mValues.length) {
                mPath.lineTo(mCenterX, mCenterY);
                break;
            }
            if (mValues[i] < 0) {
                mValues[i] = 0;
            }
            if (mValues[i] > mMaxValue) {
                mValues[i] = mMaxValue;
            }
            radio = mValues[i] / mMaxValue;
            nextRadian = mOffsetRadian + mRadian * i;
            nextX = (float) (mCenterX + mRadius * Math.sin(nextRadian) * radio);
            nextY = (float) (mCenterY - mRadius * Math.cos(nextRadian) * radio);

            if (i == 0) {
                mPath.moveTo(nextX, nextY);
            } else {
                mPath.lineTo(nextX, nextY);
            }
            if (isDrawPoint) {
                canvas.drawCircle(nextX, nextY, mLineWidth * 2, mOverlayPaint);
            }
        }

        mPath.close();
        canvas.drawPath(mPath, mOverlayPaint);
        if (isDrawOverlayStroke) {
            canvas.drawPath(mPath, mStrokePaint);
        }
    }

    private void drawText(Canvas canvas) {
        if (mTitles == null || mTitles.length == 0) {
            return;
        }
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        final float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        float nextRadian;
        float nextX;
        float nextY;
        float textDistance;
        for (int i = 0; i < mAngleCount && i < mTitles.length; i++) {
            if (TextUtils.isEmpty(mTitles[i])) {
                continue;
            }
            nextRadian = mOffsetRadian + mRadian * i;
            nextX = (float) (mCenterX + mRadius * Math.sin(nextRadian));
            nextY = (float) (mCenterY - mRadius * Math.cos(nextRadian));
            textDistance = mTextPaint.measureText(mTitles[i]);

            Location location = calculateLocation(nextRadian);
            if (location != null) {
                switch (location) {
                    case NORTH:
                        canvas.drawText(mTitles[i], nextX, nextY - fontHeight / 3, mTextPaint);
                        break;
                    case NORTHEAST:
                        canvas.drawText(mTitles[i], nextX + fontHeight / 3, nextY - fontHeight / 3, mTextPaint);
                        break;
                    case EAST:
                        canvas.drawText(mTitles[i], nextX + fontHeight / 3, nextY + fontHeight / 3, mTextPaint);
                        break;
                    case SOUTHEAST:
                        canvas.drawText(mTitles[i], nextX + fontHeight / 3, nextY + fontHeight / 3, mTextPaint);
                        break;
                    case SOUTH:
                        canvas.drawText(mTitles[i], nextX, nextY + fontHeight / 3, mTextPaint);
                        break;
                    case SOUTHWEST:
                        canvas.drawText(mTitles[i], nextX - textDistance - fontHeight / 3, nextY + fontHeight / 3, mTextPaint);
                        break;
                    case WEST:
                        canvas.drawText(mTitles[i], nextX - textDistance - fontHeight / 3, nextY + fontHeight / 3, mTextPaint);
                        break;
                    case NORTHWEST:
                        canvas.drawText(mTitles[i], nextX - textDistance - fontHeight / 3, nextY - fontHeight / 3, mTextPaint);
                        break;
                }
            }
        }
    }

    private Location calculateLocation(float radian) {
        final float pi = (float) Math.PI;
        final float halfPi = (float) (Math.PI / 2);
        final float oneHalfPi = (float) (Math.PI * 3 / 2);
        final float twoPi = (float) (Math.PI * 2);
        if (radian % pi == 0) {
            return Location.NORTH;
        } else if (radian > 0 && radian < halfPi) {
            return Location.NORTHEAST;
        } else if (radian == halfPi) {
            return Location.EAST;
        } else if (radian > halfPi && radian < pi) {
            return Location.SOUTHEAST;
        } else if (radian == pi) {
            return Location.SOUTH;
        } else if (radian > pi && radian < oneHalfPi) {
            return Location.SOUTHWEST;
        } else if (radian == oneHalfPi) {
            return Location.WEST;
        } else if (radian > oneHalfPi && radian < twoPi) {
            return Location.NORTHWEST;
        }
        return null;
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void setAngleCount(int angleCount) {
        if (angleCount <= 3) {
            throw new IllegalArgumentException("AngleCount should be over 3!");
        }
        mAngleCount = angleCount;
        postInvalidate();
    }

    public void setMaxValue(float maxValue) {
        if (maxValue < 0) {
            throw new IllegalArgumentException("MaxValue should be over 0!");
        }
        mMaxValue = maxValue;
        postInvalidate();
    }

    public void setValues(float[] values) {
        if (values == null || values.length <= 0) {
            throw new IllegalArgumentException("Values cannot be null or empty!");
        }
        mValues = values;
        postInvalidate();
    }

    public void setValues(float maxValue, float[] values) {
        setMaxValue(maxValue);
        setValues(values);
    }

    public void setTitles(String[] titles) {
        if (titles == null || titles.length <= 0) {
            throw new IllegalArgumentException("Titles cannot be null or empty!");
        }
        mTitles = titles;
        postInvalidate();
    }
}
