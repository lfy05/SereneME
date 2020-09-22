package dev.feiyang.sereneme;

import android.support.wearable.activity.WearableActivity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
}