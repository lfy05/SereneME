package dev.feiyang.sereneme.Data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class JournalFragmentVM extends AndroidViewModel {
    private MeditationRecordsRepository mMRMeditationRecordsRepository;

    public JournalFragmentVM(@NonNull Application application) {
        super(application);
        mMRMeditationRecordsRepository = new MeditationRecordsRepository(application);
    }

    public LiveData<List<MeditationRecord>> getLiveRecords() {
        return mMRMeditationRecordsRepository.getMeditationRecords();
    }

    public void addRecords(MeditationRecord... records){
        mMRMeditationRecordsRepository.insertRecord(records);
    }

    public void deleteRecord(MeditationRecord record){

    }


}
