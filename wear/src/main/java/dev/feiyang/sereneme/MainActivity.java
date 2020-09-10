package dev.feiyang.sereneme;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import dev.feiyang.common.CustomViews.RoundTimer;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final KonfettiView konfettiView = findViewById(R.id.konfettiView);
        final RoundTimer roundTimerView = (RoundTimer) findViewById(R.id.roundTimer);
        roundTimerView.setCountDownCompleteListener(new RoundTimer.CountDownCompleteListener() {
            @Override
            public void onCountDownComplete() {
                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12, 5f))
                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                        .streamFor(30, 5000L);
            }
        });
        roundTimerView.startTimer(0.5);
    }
}
