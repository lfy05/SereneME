package dev.feiyang.sereneme.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MEDITATION_RECORDS")
public class MeditationRecord {
    @PrimaryKey
    public int mID;

    @ColumnInfo(name = "Date")
    public String mDate;

    @ColumnInfo(name = "Length")
    public int mLength;

    @ColumnInfo(name = "Score")
    public int mScore;
}
