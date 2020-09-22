package dev.feiyang.sereneme.Data;

import android.app.Application;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import dev.feiyang.sereneme.R;
import dev.feiyang.sereneme.UI.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JournalFragmentVMTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testAddLiveRecords() {
        onView(withId(R.id.navJournal)).perform(click());

        MeditationRecordsRepository repo =
                new MeditationRecordsRepository((Application) getApplicationContext());
        assertTrue(repo != null);

        MeditationRecord record = new MeditationRecord();
        record.mScore = 35;
        record.mDate = "today";
        record.mID = 20200944;
        record.mLength = 15;
        repo.insertRecord(record);

        onView(withId(R.id.meditationRecordsList))
                .check(matches(isDisplayed()))
                .check(matches(withAdapterData(withRecordContent(record))));

    }

    private static Matcher<View> withAdapterData(final Matcher<MeditationRecord> dataMatcher){
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if(!(item instanceof RecyclerView))
                    return false;

                RecyclerView.Adapter adapter = ((RecyclerView) item).getAdapter();
                for (int i = 0; i < adapter.getItemCount(); i++){
                    if(dataMatcher.matches(adapter.getItemId(i)));
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    private static Matcher<MeditationRecord> withRecordContent(final MeditationRecord expectedRecord){
        return new TypeSafeMatcher<MeditationRecord>() {
            @Override
            protected boolean matchesSafely(MeditationRecord item) {
                if (item.mDate.equals(expectedRecord.mDate)
                        || item.mScore == expectedRecord.mScore
                        || item.mLength == expectedRecord.mLength
                        || item.mID == expectedRecord.mID)
                    return true;
                else
                    return false;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}