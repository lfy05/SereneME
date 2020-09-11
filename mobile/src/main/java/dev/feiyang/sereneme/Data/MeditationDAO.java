package dev.feiyang.sereneme.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeditationDAO {
    @Query("SELECT * FROM MeditationRecord")
    List<MeditationRecord> getAllRecords();

    @Query("SELECT * FROM MeditationRecord WHERE mID LIKE :ID")
    MeditationRecord getRecordByID(int ID);

    @Query("SELECT * FROM MeditationRecord")
    LiveData<List<MeditationRecord>> loadAllRecords();

    @Insert
    void insertRecords(MeditationRecord... records);

    @Delete
    void deleteRecord(MeditationRecord record);
}

