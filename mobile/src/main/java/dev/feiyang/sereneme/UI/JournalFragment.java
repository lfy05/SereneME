package dev.feiyang.sereneme.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import dev.feiyang.sereneme.Data.JournalFragmentVM;
import dev.feiyang.sereneme.Data.MeditationRecord;
import dev.feiyang.sereneme.R;


public class JournalFragment extends Fragment{
    private JournalFragmentVM mJournalVM;
    private RecyclerView mRecordRecyclerView;
    private LineChart mMeditationScoreChart;
    private MeditationRecordsRVAdapter mRRVAdapter;
    private static final String RECORD_KEY = "dev.feiyang.sereneme.records";

    private LineDataSet mRecordDataSet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mMeditationScoreChart = getView().findViewById(R.id.meditationScoreChart);
        this.mMeditationScoreChart.setDrawBorders(false);
        this.mMeditationScoreChart.getDescription().setEnabled(false);
        this.mMeditationScoreChart.setNoDataText("No Records");
        this.mMeditationScoreChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        this.mMeditationScoreChart.getLegend().setEnabled(false);
        this.mMeditationScoreChart.invalidate();

        XAxis xAxis = mMeditationScoreChart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxisLeft = mMeditationScoreChart.getAxisLeft();
        YAxis yAxisRight = mMeditationScoreChart.getAxisRight();

        yAxisLeft.setDrawLabels(false);
        yAxisRight.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisRight.setDrawGridLines(false);

        //get view model
        mJournalVM = new ViewModelProvider(getActivity()).get(JournalFragmentVM.class);
        mJournalVM.getLiveRecords().observe(getViewLifecycleOwner(), new Observer<List<MeditationRecord>>() {
            @Override
            public void onChanged(List<MeditationRecord> meditationRecords) {
                mRRVAdapter.setMeditationRecords(meditationRecords);
                mRRVAdapter.notifyDataSetChanged();

                List<Entry> entries = new ArrayList<>();
                int index = 0;
                for (MeditationRecord meditationRecord: meditationRecords){
                    entries.add(new Entry(index, meditationRecord.mScore));
                    index++;
                }

                if(entries.size() > 0){
                    LineDataSet dataSet = new LineDataSet(entries, "Score");
                    dataSet.setLineWidth(3);
                    LineData lineData = new LineData(dataSet);
                    mMeditationScoreChart.setData(lineData);
                    mMeditationScoreChart.invalidate();
                    return;
                }

                mMeditationScoreChart.invalidate();
            }
        });
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