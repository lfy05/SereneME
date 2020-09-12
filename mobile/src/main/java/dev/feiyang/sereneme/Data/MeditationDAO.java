package dev.feiyang.sereneme.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeditationDAO {
    @Query("SELECT * FROM MEDITATION_RECORDS")
    List<MeditationRecord> getAllRecords();

    @Query("SELECT * FROM MEDITATION_RECORDS WHERE mID LIKE :ID")
    MeditationRecord getRecordByID(int ID);

    @Query("SELECT * FROM MEDITATION_RECORDS")
    LiveData<List<MeditationRecord>> loadAllRecords();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRecords(MeditationRecord... records);

    @Delete
    void deleteRecord(MeditationRecord record);
}

