package dev.feiyang.sereneme.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import dev.feiyang.sereneme.R;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    private String CHANNEL_ID = "SereneME Reminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notification from " + CHANNEL_ID, "Send notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(context.getResources().getString(R.string.reminder_notification_title))
                .setContentText(context.getResources().getString(R.string.reminder_notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context).notify(200, builder.build());
    }
}
