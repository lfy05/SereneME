package dev.feiyang.sereneme.UI;

import android.view.View;
import android.widget.Button;

import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dev.feiyang.sereneme.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MeditateFragmentTest {

    // this launches MainActivity before test starts and closes when all tests finish
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testSetButtonVisibility(){
        onView(withId(R.id.time5)).perform(click());
        onView(withId(R.id.time15)).check(matches(new BoundedMatcher<View, Button>(Button.class) {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(Button item) {
                return item.getVisibility() == View.GONE;
            }
        }));
    }
}