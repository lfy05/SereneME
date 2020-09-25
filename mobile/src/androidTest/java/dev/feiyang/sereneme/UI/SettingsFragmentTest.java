package dev.feiyang.sereneme.UI;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import dev.feiyang.sereneme.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class SettingsFragmentTest {

    @Rule
    public ActivityScenarioRule<SettingsActivity> settingsActivityActivityScenarioRule =
            new ActivityScenarioRule<SettingsActivity>(SettingsActivity.class);

    @Test
    public void testSettingsFragment(){
        onView(withId(R.id.settingsFragmentContainer))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSettingsToolbar(){
        onView(withContentDescription("Back to Profile"))
                .perform(click());
    }

}