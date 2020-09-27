package dev.feiyang.sereneme.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.feiyang.sereneme.Data.MRecordsAnalystService;
import dev.feiyang.sereneme.R;

public class MainActivity extends AppCompatActivity {
    private JobScheduler mScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup navigation
        NavController navController = Navigation.findNavController(this, R.id.fragmentHost);
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        NavigationUI.setupWithNavController(bottomNavView, navController);

        // Job Scheduler
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(getPackageName(), MRecordsAnalystService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceName);
        builder.setRequiresCharging(true);

        JobInfo analystJobInfo = builder.build();
        mScheduler.schedule(analystJobInfo);

        Toast.makeText(this, "Analyst Job Scheduled at Charging", Toast.LENGTH_LONG).show();

    }


}