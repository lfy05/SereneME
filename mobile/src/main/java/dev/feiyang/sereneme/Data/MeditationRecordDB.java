package dev.feiyang.sereneme.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MeditationRecord.class}, version = 1)
public abstract class MeditationRecordDB extends RoomDatabase {
    private static MeditationDAO MeditationDAOInstance;
    public abstract MeditationDAO meditationDAO();
    private static MeditationRecordDB MeditationRecordDBInstance;

    public static MeditationRecordDB getDatabase(final Context context){
        if (MeditationRecordDBInstance == null){
            synchronized (MeditationRecordDB.class){
                if (MeditationRecordDBInstance == null){
                    MeditationRecordDBInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MeditationRecordDB.class, "MeditationRecord")
                            .build();
                }
            }
        }
        return MeditationRecordDBInstance;
    }
}
