package dev.feiyang.sereneme;

import org.junit.Test;

import static org.junit.Assert.*;

public class MeditationRecordTest {

    @Test
    public void testMeditationRecordSerialization() {
        MeditationRecord record = new MeditationRecord();
        record.mScore = 100;
        record.mLength = 10;
        record.mID = 1234567;
        record.mDate = "today";

        MeditationRecord recoveredRecord = MeditationRecord.getFromBytes(record.getBytes());

        assertTrue(record.mDate.equals(recoveredRecord.mDate));
        assertTrue(record.mID == recoveredRecord.mID);
        assertTrue(record.mLength == recoveredRecord.mLength);
        assertTrue(record.mScore == recoveredRecord.mScore);
    }
}