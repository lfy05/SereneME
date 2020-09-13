package dev.feiyang.common.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;

import dev.feiyang.common.R;


public class RoundTimer extends View {
    private AttributeSet mAttrs;
    private TypedArray mXMLAttrs;

    private boolean mIsTimerOn = false;

    private int mCircleCenterX;
    private int mCircleCenterY;
    private int mCircleRadius;

    private int mKnobCenterX;
    private int mKnobCenterY;
    private int mKnobRadius;
    private double mKnobSweepAngle;

    private Paint mDigitTimerPaint;
    private int mDigitTimerStartX;
    private int mDigitTimerStartY;
    private String mDigitTimer = "";
    private DecimalFormat mDigitSecondFormat;
    private DecimalFormat mDigitMinuteFormat;

    private RectF mArcRect;

    private Paint mCirclePaint;
    private Paint mKnobPaint;
    private Paint mSweepArcPaint;

    private CountDownTimer mCountDownTimer;
    public interface CountDownCompleteListener{
        public void onCountDownComplete(double minutes);
    }
    public interface ActionUpListener{
        public void onActionUp(double knobSweepAngle);
    }
    public interface ActionDownListener{
        public void onActionDown(double knobSweepAngle);
    }
    private CountDownCompleteListener mCountDownCompleteListener;
    private ActionUpListener mActionUpListener;
    private ActionDownListener mActionDownListener;

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
        mKnobSweepAngle = mXMLAttrs.getInt(R.styleable.RoundTimer_defaultPosition, 0);

        mArcRect = new RectF();

        mSweepArcPaint = new Paint();
        mSweepArcPaint.setStyle(Paint.Style.STROKE);
        mSweepArcPaint.setColor(mXMLAttrs.getColor(R.styleable.RoundTimer_lineFillColor,
                ContextCompat.getColor(getContext(), R.color.colorPrimary)));
        mSweepArcPaint.setStrokeWidth(
                mXMLAttrs.getFloat(R.styleable.RoundTimer_lineWidth, (float) 8.0));

        mDigitTimerPaint = new Paint();
        mDigitTimerPaint.setColor(mXMLAttrs.getColor(R.styleable.RoundTimer_lineFillColor,
                ContextCompat.getColor(getContext(), R.color.colorPrimary)));
        mDigitTimerPaint.setTextAlign(Paint.Align.CENTER);
        mDigitTimerPaint.setTextSize(mXMLAttrs.getInteger(R.styleable.RoundTimer_digitTimeFontSize, 70));
        mDigitTimerStartY = mXMLAttrs.getInt(R.styleable.RoundTimer_digitTimerYFromTop, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // initialize circle parameters
        this.mCircleCenterX = getWidth() / 2;
        this.mCircleCenterY = getHeight() / 2;
        this.mCircleRadius = (int) (Math.min(this.mCircleCenterX, this.mCircleCenterY) * 0.9);

        // calculate initial knob position
        mKnobCenterX = mCircleCenterX;
        mKnobCenterY = mCircleCenterY - mCircleRadius;
        mKnobRadius = mCircleRadius / 12;
        updateKnobPosition(mKnobSweepAngle);

        // calculate arc bounds
        mArcRect.set(mCircleCenterX - mCircleRadius,
                mCircleCenterY - mCircleRadius,
                mCircleCenterX + mCircleRadius,
                mCircleCenterY + mCircleRadius);

        // calculate digit timer bounds
        mDigitTimerStartX = getWidth() / 2;
        if (mDigitTimerStartY == 0)
            mDigitTimerStartY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw timer
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mCirclePaint);

        // draw knob
        canvas.drawCircle(mKnobCenterX, mKnobCenterY, mKnobRadius, mKnobPaint);

        // draw arc
        canvas.drawArc(mArcRect, 270, (float) Math.toDegrees(mKnobSweepAngle), false, mSweepArcPaint);

        // draw digit timer
        canvas.drawText(mDigitTimer, mDigitTimerStartX, mDigitTimerStartY, mDigitTimerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mIsTimerOn)
            return false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                /* Check if touching on knob */
                // if the touch is too far from the knob, ignore it
                if (Math.sqrt(Math.pow(event.getX() - mKnobCenterX, 2) + Math.pow(event.getY() - mKnobCenterY, 2)) > mKnobRadius + 40.0 )
                    return false;

                if (mActionDownListener != null){
                    mActionDownListener.onActionDown(mKnobSweepAngle);
                    return true;
                }

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
                updateDigitTimer((long) (3600000 * (mKnobSweepAngle / (Math.PI * 2))));
                break;
        }
        invalidate();
        return true;
    }

    /**
     * Start the timer.
     * If minutes is above 0, countdown, otherwise, behave like a stop watch
     * @param minutes
     */
    public void startTimer(final double minutes){
        this.mIsTimerOn = true;
        final double mills = minutes * 60000;
        this.mCountDownTimer = new CountDownTimer((long) mills, 100) {
            @Override
            public void onTick(long l) {
                updateDigitTimer(l);
                updateKnobPosition(Math.PI * 2 * ((float) l / mills));
            }

            @Override
            public void onFinish() {
                mIsTimerOn = false;
                mCountDownCompleteListener.onCountDownComplete(minutes);
            }
        }.start();
    }

    /**
     * Update timer to display time
     * @param mills time in mills
     */
    private void updateDigitTimer(long mills){
        if (mills <= 60000){
            mDigitTimer = String.format("00 : %02d", (int) mills / 1000);
        } else {
            mDigitTimer = String.format("%02d : %02d", (int) (mills / 60000), (int)((mills % 60000) / 1000));
        }
    }

    public void syncDigitTimer(){
        updateDigitTimer((long) (3600000 * (mKnobSweepAngle / (Math.PI * 2))));
    }

    /**
     * update knob position variables to match the angle, and match digital timer with the angle.
     * @param angle knob sweeping angle in radian
     */
    public void updateKnobPosition(double angle){
        // update knob
        if (angle < 0) return;
        mKnobSweepAngle = angle % (Math.PI * 2);
        mKnobCenterX = (int) (mCircleCenterX + mCircleRadius * Math.sin(mKnobSweepAngle));
        mKnobCenterY = (int) (mCircleCenterY - mCircleRadius * Math.cos(mKnobSweepAngle));
        invalidate();
    }

    /**
     * Set callback when timer finishes
     * @param mCountDownCompleteListener
     */
    public void setCountDownCompleteListener(CountDownCompleteListener mCountDownCompleteListener) {
        this.mCountDownCompleteListener = mCountDownCompleteListener;
    }

    public void setActionUpListener(ActionUpListener mActionUpListener) {
        this.mActionUpListener = mActionUpListener;
    }

    public void setActionDownListener(ActionDownListener mActionDownListener) {
        this.mActionDownListener = mActionDownListener;
    }

    /**
     * Set digital timer text. ONLY WORKS when timer is not on
     * @param mDigitTimer text to be displayed at digital timer position
     */
    public void setDigitTimerText(String mDigitTimer) {
        this.mDigitTimer = mDigitTimer;
        invalidate();
    }

    public double getKnobSweepAngle() {
        return mKnobSweepAngle;
    }
}


