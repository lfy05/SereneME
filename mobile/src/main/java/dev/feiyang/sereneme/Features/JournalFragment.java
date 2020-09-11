package dev.feiyang.sereneme.Features;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dev.feiyang.sereneme.Data.MeditateRecordVM;
import dev.feiyang.sereneme.Data.MeditationRecord;
import dev.feiyang.sereneme.R;


public class JournalFragment extends Fragment {
    private MeditateRecordVM mRecordVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journal, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mRecordVM = new ViewModelProvider(this).get(MeditateRecordVM.class);
    }
}