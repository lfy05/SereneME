package dev.feiyang.sereneme;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import dev.feiyang.common.CustomViews.RoundTimer;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends WearableActivity{
    private HashMap<Integer, Button> timeButtons = new HashMap<Integer, Button>();
    private static final String RECORD_KEY = "dev.feiyang.sereneme.records";
    private DataClient mDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // data synchronization setup
        mDataClient = Wearable.getDataClient(this);

        // setting up timer and confetti
        final KonfettiView konfettiView = findViewById(R.id.konfettiView);
        final RoundTimer roundTimerView = (RoundTimer) findViewById(R.id.roundTimer);

        Button time5 = (Button) findViewById(R.id.time5);
        Button time15 = (Button) findViewById(R.id.time15);
        Button time30 = (Button) findViewById(R.id.time30);
        final Button start = (Button) findViewById(R.id.startButton);
        final Button reset = (Button) findViewById(R.id.resetButton);

        timeButtons.put(5, time5);
        timeButtons.put(15, time15);
        timeButtons.put(30, time30);

        roundTimerView.setCountDownCompleteListener(new RoundTimer.CountDownCompleteListener() {
            @Override
            public void onCountDownComplete(double minutes) {
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
                roundTimerView.setDigitTimerText(getResources().getString(R.string.complete));
                reset.setVisibility(View.VISIBLE);

                MeditationRecord record = new MeditationRecord();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                record.mDate = dtf.format(now);
                record.mScore = 100;
                record.mID = (int) (Math.random() * 10000000);
                record.mLength = (int) minutes;

                PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/meditation_records");
                putDataMapRequest.getDataMap().putByteArray(RECORD_KEY, record.getBytes());
                PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
                Task<DataItem> putDataTask = mDataClient.putDataItem(putDataRequest);
                Log.d("SereneME: ", "New Records Posted");
            }
        });

        roundTimerView.setActionDownListener(new RoundTimer.ActionDownListener() {
            @Override
            public void onActionDown(double knobSweepAngle) {
                setExtraElementVisibility(false);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double timeSet = 60 * roundTimerView.getKnobSweepAngle() / (Math.PI * 2);
                roundTimerView.startTimer(timeSet);
                start.setVisibility(View.GONE);

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfettiView.stopGracefully();
                setExtraElementVisibility(true);
                roundTimerView.setDigitTimerText("");
                reset.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setExtraElementVisibility(boolean visibility){
        for (int buttonKey: timeButtons.keySet()){
            timeButtons.get(buttonKey).setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }
}
