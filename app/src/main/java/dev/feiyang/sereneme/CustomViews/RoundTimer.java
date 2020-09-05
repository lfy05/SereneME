package dev.feiyang.sereneme.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import dev.feiyang.sereneme.R;

public class RoundTimer extends View {
    private AttributeSet attrs;
    private TypedArray XMLAttrs;

    private int circleCenterX;
    private int circleCenterY;
    private int circleRadius;

    private int knobCenterX;
    private int knobCenterY;
    private int knobRadius;

    // default constructors
    public RoundTimer(Context context) {
        super(context);

        init(null);
    }

    public RoundTimer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        init(attrs);
    }

    public RoundTimer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        init(attrs);
    }

    public RoundTimer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.attrs = attrs;
        init(attrs);
    }

    /**
     * Initializes view based on provided attributes
     * @param set attribute set
     */
    private void init(@Nullable AttributeSet set){
        XMLAttrs = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RoundTimer,
                0, 0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* Update parameters */
        this.circleCenterX = getWidth() / 2;
        this.circleCenterY = getHeight() / 2;
        this.circleRadius = getWidth() < getHeight() ? getWidth() / 4 : getHeight() / 4 ;

        /* Draw timer */
        // draw background circle
        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(XMLAttrs.getFloat(R.styleable.RoundTimer_lineWidth, (float) 8.0));
        circlePaint.setColor(XMLAttrs.getColor(R.styleable.RoundTimer_lineColor,
                ContextCompat.getColor(getContext(), R.color.colorPrimary)));
        canvas.drawCircle(circleCenterX, circleCenterY, circleRadius, circlePaint);

        // draw knob

    }
}
