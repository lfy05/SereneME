package dev.feiyang.sereneme.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import dev.feiyang.sereneme.R;

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

    private Paint mCirclePaint;
    private Paint mKnobPaint;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw timer
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mCirclePaint);

        // draw knob
        canvas.drawCircle(mKnobCenterX, mKnobCenterY, mKnobRadius, mKnobPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                /* Check if touching on knob */
                // if the touch is too far from the knob, ignore it
                if (Math.sqrt(Math.pow(event.getX() - mKnobCenterX, 2) + Math.pow(event.getY() - mKnobCenterY, 2)) > mKnobRadius + 20.0 )
                    return false;

            case MotionEvent.ACTION_MOVE:
                // get motion axis
                float x = event.getX();
                float y = event.getY();

                // Update angle
                if (x >= mCircleCenterX){
                    // right quadrants
                    if (y <= mCircleCenterY){
                        // up-right quadrant
                        mKnobSweepAngle = Math.atan((x - mCircleCenterX) / -(y - mCircleCenterY));
                    } else {
                        // bottom-right quadrants
                        mKnobSweepAngle = Math.PI + Math.atan((x - mCircleCenterX) / -(y - mCircleCenterY));
                    }
                } else {
                    // left quadrants
                    if (y >= mCircleCenterY){
                        // bottom-left quadrant
                        mKnobSweepAngle = Math.PI + Math.atan((x - mCircleCenterX) / -(y - mCircleCenterY));
                    } else {
                        // in this case: Math.toDegrees() return a negative value
                        mKnobSweepAngle = 2 * Math.PI - Math.abs(Math.atan((x - mCircleCenterX) / -(y - mCircleCenterY)));
                    }
                }
                updateKnobPosition(mKnobSweepAngle);
                break;
        }

        invalidate();
        return true;
    }

    /**
     * update knob position variables to match the angle. DOES NOT UPDATE UI, use invalidate() to
     * refresh UI
     * @param angle knob sweeping angle in radian
     */
    private void updateKnobPosition(double angle){
        mKnobCenterX = (int) (mCircleCenterX + mCircleRadius * Math.sin(mKnobSweepAngle));
        mKnobCenterY = (int) (mCircleCenterY - mCircleRadius * Math.cos(mKnobSweepAngle));
    }
}
