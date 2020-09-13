package dev.feiyang.sereneme.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

import dev.feiyang.sereneme.Data.JournalFragmentVM;
import dev.feiyang.sereneme.Data.MeditationRecord;
import dev.feiyang.sereneme.R;


public class JournalFragment extends Fragment{
    private JournalFragmentVM mJournalVM;
    private RecyclerView mRecordRecyclerView;
    private MeditationRecordsRVAdapter mRRVAdapter;
    private static final String RECORD_KEY = "dev.feiyang.sereneme.records";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get view model
        mJournalVM = new ViewModelProvider(getActivity()).get(JournalFragmentVM.class);
        mJournalVM.getLiveRecords().observe(this, new Observer<List<MeditationRecord>>() {
            @Override
            public void onChanged(List<MeditationRecord> meditationRecords) {
                mRRVAdapter.setMeditationRecords(meditationRecords);
                mRRVAdapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journal, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // initialize recycler view
        mRecordRecyclerView = getView().findViewById(R.id.meditationRecordsList);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRRVAdapter = new MeditationRecordsRVAdapter(mJournalVM.getLiveRecords().getValue(), getContext());
        mRecordRecyclerView.setAdapter(mRRVAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}