package dev.feiyang.sereneme.wear;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import androidx.annotation.Nullable;

public class MainActivity extends WearableActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAmbientEnabled();
    }
}
