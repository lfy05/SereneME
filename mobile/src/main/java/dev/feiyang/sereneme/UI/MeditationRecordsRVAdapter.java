package dev.feiyang.sereneme.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.feiyang.sereneme.Data.MeditationRecord;
import dev.feiyang.sereneme.R;

public class MeditationRecordsRVAdapter extends RecyclerView.Adapter<MeditationRecordsRVAdapter.MeditationRecordVH> {
    private List<MeditationRecord> meditationRecords;
    private Context mContext;

    public MeditationRecordsRVAdapter(List<MeditationRecord> meditationRecords, Context mContext) {
        this.meditationRecords = meditationRecords;
        this.mContext = mContext;
    }

    public class MeditationRecordVH extends RecyclerView.ViewHolder {
        TextView mDate;
        TextView mLength;
        TextView mScore;

        public MeditationRecordVH(@NonNull View itemView) {
            super(itemView);
            mDate = (TextView) itemView.findViewById(R.id.meditationDate);
            mLength = (TextView) itemView.findViewById(R.id.meditationLength);
            mScore = (TextView) itemView.findViewById(R.id.meditationScore);
        }
    }

    @NonNull
    @Override
    public MeditationRecordVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeditationRecordVH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meditation_record_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MeditationRecordVH holder, int position) {
        MeditationRecord currentRecord = meditationRecords.get(meditationRecords.size() - (position + 1));
        holder.mDate.setText(currentRecord.mDate);
        holder.mLength.setText("" + currentRecord.mLength);
        holder.mScore.setText(mContext.getResources().getString(R.string.focusScore) + currentRecord.mScore);
    }

    @Override
    public int getItemCount() {
        if (meditationRecords == null)
            return 0;
        return meditationRecords.size();
    }

    public void setMeditationRecords(List<MeditationRecord> meditationRecords) {
        this.meditationRecords = meditationRecords;
    }
}
