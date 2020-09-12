package dev.feiyang.sereneme.UI;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import dev.feiyang.common.CustomViews.RoundTimer;
import dev.feiyang.sereneme.Data.JournalFragmentVM;
import dev.feiyang.sereneme.Data.MeditationRecord;
import dev.feiyang.sereneme.R;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MeditateFragment extends Fragment{
    private JournalFragmentVM mJournalVM;
    private HashMap<Integer, Button> timeButtons = new HashMap<Integer, Button>();
    private RoundTimer mRoundTimer;
    private KonfettiView mKonfettiView;
    private TextView mMsgOnKnob;


    public MeditateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJournalVM = new ViewModelProvider(getActivity()).get(JournalFragmentVM.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meditate, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // link with buttons
        Button time5 = getView().findViewById(R.id.time5);
        Button time10 = getView().findViewById(R.id.time10);
        Button time15 = getView().findViewById(R.id.time15);
        Button time30 = getView().findViewById(R.id.time30);
        Button time45 = getView().findViewById(R.id.time45);
        Button time60 = getView().findViewById(R.id.time60);
        Button start = getView().findViewById(R.id.buttonStart);

        timeButtons.put(5, time5);
        timeButtons.put(10, time10);
        timeButtons.put(15, time15);
        timeButtons.put(30, time30);
        timeButtons.put(45, time45);
        timeButtons.put(60, time60);
        timeButtons.put(-1, start);

        mRoundTimer = (RoundTimer) getView().findViewById(R.id.roundTimer);
        mKonfettiView = getView().findViewById(R.id.konfettiView);

        mMsgOnKnob = (TextView) getView().findViewById(R.id.msgKnob);

        for (int buttonKey: timeButtons.keySet()){
            timeButtons.get(buttonKey).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button btn = (Button) view;
                    mRoundTimer.startTimer(Double.parseDouble((String) btn.getText()));
                    setExtraElementVisibility(false);
                    Toast.makeText(getContext(),
                            "A meditation of " + btn.getText() + " has started ",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }

        mRoundTimer.setCountDownCompleteListener(new RoundTimer.CountDownCompleteListener() {
            @Override
            public void onCountDownComplete(double minutes) {
                mKonfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12, 5f))
                        .setPosition(-50f, mKonfettiView.getWidth() + 50f, -50f, -50f)
                        .streamFor(300, 5000L);
                mRoundTimer.setDigitTimerText(getContext().getString(R.string.countdownComplete));
                MeditationRecord record = new MeditationRecord();
                record.mID = (int) (Math.random() * 10000000);
                record.mLength = (int) minutes;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                record.mDate = dtf.format(now);
                record.mScore = 100;
                mJournalVM.addRecords(record);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double timeSet = 60 * mRoundTimer.getKnobSweepAngle() / (Math.PI * 2);
                mRoundTimer.startTimer(timeSet);
                setExtraElementVisibility(false);
                Toast.makeText(getContext(),
                        "A meditation of " + (int) timeSet + " has started ",
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void setExtraElementVisibility(boolean visibility){
        for (int buttonKey: timeButtons.keySet()){
            timeButtons.get(buttonKey).setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
        mMsgOnKnob.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}