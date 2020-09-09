package dev.feiyang.common.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import dev.feiyang.common.R;


public class RoundTimer extends View {
    private AttributeSet mAttrs;
    private TypedArray mXMLAttrs;

    private int mCircleCenterX;
    private int mCircleCenterY;
    private int mCircleRadius;

    private int mKnobCenterX;
    private int mKnobCenterY;
    private int mKnobRadius;
    private double mKnobSweepAngle;
    private int mKnobSweepedCycle;

    private RectF mArcRect;

    private Paint mCirclePaint;
    private Paint mKnobPaint;
    private Paint mSweepArcPaint;

    // default constructors
    public RoundTimer(Context context) {
        super(context);

        init(null);
    }

    public RoundTimer(Context context, @Nullable AttributeSet mAttrs) {
        super(context, mAttrs);
        this.mAttrs = mAttrs;
        init(mAttrs);
    }

    public RoundTimer(Context context, @Nullable AttributeSet mAttrs, int defStyleAttr) {
        super(context, mAttrs, defStyleAttr);
        this.mAttrs = mAttrs;
        init(mAttrs);
    }

    public RoundTimer(Context context, @Nullable AttributeSet mAttrs, int defStyleAttr, int defStyleRes) {
        super(context, mAttrs, defStyleAttr, defStyleRes);
        this.mAttrs = mAttrs;
        init(mAttrs);
    }

    /**
     * Initializes view based on provided attributes
     * @param set attribute set
     */
    private void init(@Nullable AttributeSet set){
        mXMLAttrs = getContext().getTheme().obtainStyledAttributes(
                mAttrs,
                R.styleable.RoundTimer,
                0, 0);

        // initialize Paint objects
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(
                mXMLAttrs.getFloat(R.styleable.RoundTimer_lineWidth, (float) 8.0));
        mCirclePaint.setColor(mXMLAttrs.getColor(R.styleable.RoundTimer_lineFrameColor,
                ContextCompat.getColor(getContext(), R.color.colorPrimary)));

        mKnobPaint = new Paint();
        mKnobPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mKnobPaint.setStrokeWidth(
                mXMLAttrs.getFloat(R.styleable.RoundTimer_lineWidth, (float) 8.0));
        mKnobPaint.setColor(mXMLAttrs.getColor(R.styleable.RoundTimer_lineFillColor,
                ContextCompat.getColor(getContext(), R.color.colorPrimary)));

        mArcRect = new RectF();

        mSweepArcPaint = new Paint();
        mSweepArcPaint.setStyle(Paint.Style.STROKE);
        mSweepArcPaint.setColor(mXMLAttrs.getColor(R.styleable.RoundTimer_lineFillColor,
                ContextCompat.getColor(getContext(), R.color.colorPrimary)));
        mSweepArcPaint.setStrokeWidth(
                mXMLAttrs.getFloat(R.styleable.RoundTimer_lineWidth, (float) 8.0));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // initialize circle parameters
        this.mCircleCenterX = getWidth() / 2;
        this.mCircleCenterY = getHeight() / 2;
        this.mCircleRadius = Math.min(getWidth(), getHeight()) / 4;

        // calculate initial knob position
        mKnobCenterX = mCircleCenterX;
        mKnobCenterY = mCircleCenterY - mCircleRadius;
        mKnobRadius = mCircleRadius / 12;

        mArcRect.set(mCircleCenterX - mCircleRadius,
                mCircleCenterY - mCircleRadius,
                mCircleCenterX + mCircleRadius,
                mCircleCenterY + mCircleRadius);
    }

    /**
     * update knob position variables to match the angle. DOES NOT UPDATE UI, use invalidate() to
     * refresh UI
     * @param angle knob sweeping angle in radian
     */
    private void updateKnobPosition(double angle){
        // update knob
        mKnobCenterX = (int) (mCircleCenterX + mCircleRadius * Math.sin(mKnobSweepAngle));
        mKnobCenterY = (int) (mCircleCenterY - mCircleRadius * Math.cos(mKnobSweepAngle));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw timer
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mCirclePaint);

        // draw knob
        canvas.drawCircle(mKnobCenterX, mKnobCenterY, mKnobRadius, mKnobPaint);

        // draw arc
        Log.d("SereneME Round Timer: ", "mKnobSweepAngle: " + mKnobSweepAngle);
        if (mKnobSweepedCycle < 1)
            canvas.drawArc(mArcRect, 270, (float) Math.toDegrees(mKnobSweepAngle), false, mSweepArcPaint);
        else
            canvas.drawArc(mArcRect, 270, 360, false, mSweepArcPaint);
    }

}
