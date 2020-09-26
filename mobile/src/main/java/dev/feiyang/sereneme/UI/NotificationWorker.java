package dev.feiyang.sereneme.UI;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import dev.feiyang.sereneme.R;

public class NotificationWorker extends Worker {
    private String CHANNEL_ID = "SereneME Reminder";
    private Context context;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(context.getResources().getString(R.string.reminder_notification_title))
                .setContentText(context.getResources().getString(R.string.reminder_notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context).notify(200, builder.build());
        return Result.success();
    }
}
