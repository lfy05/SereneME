package dev.feiyang.sereneme.Features;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import dev.feiyang.common.CustomViews.RoundTimer;
import dev.feiyang.sereneme.R;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeditateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeditateFragment extends Fragment{

    private HashMap<Integer, Button> timeButtons = new HashMap<Integer, Button>();
    private RoundTimer mRoundTimer;
    private KonfettiView mKonfettiView;
    private TextView mMsgOnKnob;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeditateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeditateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeditateFragment newInstance(String param1, String param2) {
        MeditateFragment fragment = new MeditateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


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

        timeButtons.put(5, time5);
        timeButtons.put(10, time10);
        timeButtons.put(15, time15);
        timeButtons.put(30, time30);
        timeButtons.put(45, time45);
        timeButtons.put(60, time60);

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
                    Snackbar.make(getView(),
                            "A " + btn.getText() + " minutes meditation has started",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mRoundTimer.setCountDownCompleteListener(new RoundTimer.CountDownCompleteListener() {
            @Override
            public void onCountDownComplete() {
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
                mRoundTimer.setDigitTimerText(getResources().getString(R.string.countdownComplete));
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