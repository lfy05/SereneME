package dev.feiyang.sereneme;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.input.RotaryEncoder;
import android.util.Log;
import android.view.MotionEvent;
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
import java.util.List;

import dev.feiyang.common.CustomViews.RoundTimer;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends WearableActivity{
    private HashMap<Integer, Button> timeButtons = new HashMap<Integer, Button>();
    private static final String RECORD_KEY = "dev.feiyang.sereneme.records";
    private static final String TAG = "SereneME: ";
    private DataClient mDataClient;
    private SensorManager sensorManager;
    private SensorEventListener mHeartRateListener;
    private SensorEventListener mAccelerometerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check sensor permission
        if (checkSelfPermission("android.permission.BODY_SENSORS") == PackageManager.PERMISSION_DENIED)
            requestPermissions(new String[]{"android.permission.BODY_SENSORS"}, 66);

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

        for (final int timeButtonKey: timeButtons.keySet()){
            timeButtons.get(timeButtonKey).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setExtraElementVisibility(false);
                    start.setVisibility(View.GONE);
                    roundTimerView.startTimer(timeButtonKey);
                }
            });
        }

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
                stopSensors();

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

        Log.d("SereneME", "Attach Listener");

        roundTimerView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                // check if it is rotary input
                if (motionEvent.getAction() == MotionEvent.ACTION_SCROLL
                        && RotaryEncoder.isFromRotaryEncoder(motionEvent)){
                    setExtraElementVisibility(false);
                    float delta = (float) (-RotaryEncoder.getRotaryAxisValue(motionEvent) * 0.7);
                    roundTimerView.updateKnobPosition(roundTimerView.getKnobSweepAngle() +
                            delta);
                    roundTimerView.syncDigitTimer();
                    return true;
                }
                return false;
            }
        });
        roundTimerView.requestFocus();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double timeSet = 60 * roundTimerView.getKnobSweepAngle() / (Math.PI * 2);
                roundTimerView.startTimer(timeSet);
                start.setVisibility(View.GONE);
                startSensors();
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

        mHeartRateListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d(TAG, "HeartRate Event Occurred: " + sensorEvent.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        mAccelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d(TAG, "Accelerometer event occured: " + sensorEvent.values);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private void setExtraElementVisibility(boolean visibility){
        for (int buttonKey: timeButtons.keySet()){
            timeButtons.get(buttonKey).setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

    private void startSensors(){

        // prepare sensor accesses
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // check for heart rate sensor
        Sensor defaultHeartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (defaultHeartRateSensor != null){
            Log.d(TAG, "HeartRate sensor found");

            sensorManager.registerListener(mHeartRateListener,
                    defaultHeartRateSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        Sensor defaultAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (defaultAccelerometer != null){
            Log.d(TAG, "Accelerometer Found");
            sensorManager.registerListener(mAccelerometerListener,
                    defaultAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        Log.d("SereneME: ", "Sensor Started");
    }

    private void stopSensors(){
        sensorManager.unregisterListener(mAccelerometerListener);
        sensorManager.unregisterListener(mHeartRateListener);

        Log.d("SereneME: ", "Sensor Stopped");
    }
}
