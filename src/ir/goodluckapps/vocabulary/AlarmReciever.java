package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReciever extends BroadcastReceiver
{
    @Override
       public void onReceive(Context context, Intent intent)
       {
	        DatabaseManager DM = new DatabaseManager(context);
	        Settings _settings = DM.getSettings();
	        if(_settings._notification)
	        {
        		int notes_count = DM.getNotificationsCount();
        		if(notes_count == 0)
        			return;
        		Random rand = new Random();
        		int note_id = rand.nextInt(notes_count) + 1;
        		Notification note = DM.getNotification(note_id);
        		if(note == null)
        			return;
        		
        		NotificationCompat.Builder mBuilder =
        			    new NotificationCompat.Builder(context)
        			    .setSmallIcon(R.drawable.ic_launcher)
        			    .setContentTitle("Good Luck with TOEFL")
        			    .setContentText(note._body)
        			    .setAutoCancel(true);
        		
        		Intent resultIntent = new Intent(context, ShowNotificationActivity.class);
        		resultIntent.putExtra("notification_id", Integer.toString(note._id));
        		// Because clicking the notification opens a new ("special") activity, there's
        		// no need to create an artificial back stack.
        		PendingIntent resultPendingIntent =
        		    PendingIntent.getActivity(
        		    context,
        		    note._id,
        		    resultIntent,
        		    PendingIntent.FLAG_UPDATE_CURRENT
        		);
        		mBuilder.setContentIntent(resultPendingIntent);
        		// Sets an ID for the notification
        		int mNotificationId = note._id;
        		// Gets an instance of the NotificationManager service
        		NotificationManager mNotifyMgr = 
        		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        		// Builds the notification and issues it.
        		mNotifyMgr.notify(mNotificationId, mBuilder.build());
        		
        	    //----------------------------------------------------------------
            	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
            	Date Time_next;
            	if(calendar.get(Calendar.HOUR_OF_DAY) >= 8 && calendar.get(Calendar.HOUR_OF_DAY) < 16)
            	{
            		Time_next = calendar.getTime();
            		Time_next.setMinutes(0);
            		Time_next.setHours(16);
            	}else if(calendar.get(Calendar.HOUR_OF_DAY) >= 16 && calendar.get(Calendar.HOUR_OF_DAY) < 20)
            	{
            		Time_next = calendar.getTime();
            		Time_next.setMinutes(0);
            		Time_next.setHours(20);        		
            	}else if(calendar.get(Calendar.HOUR_OF_DAY) >= 20)
            	{
            		Time_next = calendar.getTime();
            		Time_next.setMinutes(0);
            		Time_next.setHours(8);
            		Time_next.setTime(Time_next.getTime() + 24 * 60 * 60 * 1000);            		
            	}else
            	{
            		Time_next = calendar.getTime();
            		Time_next.setMinutes(0);
            		Time_next.setHours(8);
            	}
            
            	Intent intentAlarm = new Intent(context, AlarmReciever.class);
            	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            	alarmManager.set(AlarmManager.RTC_WAKEUP, Time_next.getTime(), PendingIntent.getBroadcast(context, 1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            }
        }
 
}