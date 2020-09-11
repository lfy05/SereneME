package dev.feiyang.sereneme.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MeditationRecord.class}, version = 1)
public abstract class MeditationRecordDB extends RoomDatabase {
    public abstract MeditationDAO meditationDAO();
}
