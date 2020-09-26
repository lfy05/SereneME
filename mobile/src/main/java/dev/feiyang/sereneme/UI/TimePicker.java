package dev.feiyang.sereneme.UI;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface TimePickedListener{
        public void onTimePicked(int hourOfDay, int minuteOfHour);
    }

    private TimePickedListener timePickedListener;

    public void setTimePickedListener(TimePickedListener timePickedListener) {
        this.timePickedListener = timePickedListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
        this.timePickedListener.onTimePicked(i, i1);
    }
}
