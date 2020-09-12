package dev.feiyang.sereneme.Data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MeditationRecordsRepository {
    private MeditationDAO mMeditationDAO;
    private LiveData<List<MeditationRecord>> mMeditationRecords;

    public MeditationRecordsRepository(Application app) {
        MeditationRecordDB db = MeditationRecordDB.getDatabase(app);
        mMeditationDAO = db.meditationDAO();
        mMeditationRecords =  mMeditationDAO.loadAllRecords();
    }

    public LiveData<List<MeditationRecord>> getMeditationRecords() {
        return mMeditationRecords;
    }

    public void insertRecord(final MeditationRecord... records){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMeditationDAO.insertRecords(records);
            }
        }).start();
    }
}
