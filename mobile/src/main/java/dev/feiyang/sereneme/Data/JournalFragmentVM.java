package dev.feiyang.sereneme.Data;

import android.app.Application;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class JournalFragmentVM extends AndroidViewModel implements DataClient.OnDataChangedListener{
    private static final String RECORD_KEY = "dev.feiyang.sereneme.records";
    private static final String RECORD_PATH = "/meditation_records";
    private MeditationRecordsRepository mMRMeditationRecordsRepository;

    public JournalFragmentVM(@NonNull Application application) {
        super(application);
        mMRMeditationRecordsRepository = new MeditationRecordsRepository(application);
        Wearable.getDataClient(application).addListener(this);
    }

    public LiveData<List<MeditationRecord>> getLiveRecords() {
        return mMRMeditationRecordsRepository.getMeditationRecords();
    }

    public void addLocalRecords(MeditationRecord... records){
        mMRMeditationRecordsRepository.insertRecord(records);
    }

    public void deleteLocalRecord(MeditationRecord record){

    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent event: dataEventBuffer){
            if (event.getType() == DataEvent.TYPE_CHANGED){
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(RECORD_PATH) == 0){
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    MeditationRecord deltaRecord =
                            MeditationRecord.getFromBytes(dataMap.getByteArray(RECORD_KEY));
                    Log.d("JFVMDataLayer", "Delta Record Received");
                    addLocalRecords(deltaRecord);
                }

            } else if (event.getType() == DataEvent.TYPE_DELETED){
                Log.d("JFVMDataLayer", "Delta Record Deleted");
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Wearable.getDataClient(getApplication()).removeListener(this);
    }
}
