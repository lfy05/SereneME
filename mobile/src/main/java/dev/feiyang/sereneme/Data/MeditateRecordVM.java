package dev.feiyang.sereneme.Data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MeditateRecordVM extends ViewModel {
    private MutableLiveData<List<MeditationRecord>> mMeditations;

    public LiveData<List<MeditationRecord>> getMeditations(){
        if (mMeditations == null){
            mMeditations = new MutableLiveData<>();
            loadMeditations();
        }

        return mMeditations;
    }

    private void loadMeditations(){

    }
}
