package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MainActivity extends Activity {
	public Settings _settings;
	public Dialog dialog;
	
	public void virtualOnBackProcess()
	{
		super.onBackPressed();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Feedback");
        ((TextView)dialog.findViewById(R.id.textView_term)).setText("Would you like to leave any feedback?");
        Button btn_yes = (Button) dialog.findViewById(R.id.Yes);
        btn_yes.setText("Sure");
        Button btn_no = (Button) dialog.findViewById(R.id.No);
        btn_no.setText("No");

        btn_yes.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafebazaar.ir/app/ir.goodluckapps.vocabulary/?l=fa"));
	        	startActivity(browserIntent);
	        	dialog.dismiss();
	        	MainActivity.this.virtualOnBackProcess();
	        }
	    }); 
        btn_no.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	dialog.dismiss();
	        	MainActivity.this.virtualOnBackProcess();
	        }
	    });
        
        TextView TV_vocabulary = (TextView)findViewById(R.id.text_words);
        TextView TV_about = (TextView)findViewById(R.id.text_about);
        TextView TV_settings = (TextView)findViewById(R.id.text_settings);
        TextView TV_challenge = (TextView)findViewById(R.id.text_challenge);
        
        final MediaPlayer _media_player;
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _media_player = MediaPlayer.create(this, res_id);
        DatabaseManager DM = new DatabaseManager(this);
        _settings = DM.getSettings();
        ((GoodLuckApp)getApplicationContext()).setSettings(_settings);
        TV_vocabulary.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	            Intent mainIntent = new Intent(MainActivity.this,VocabularyActivity.class);
	            MainActivity.this.startActivity(mainIntent);
	            _settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
	        	_media_player.start();
	        }
	    }); 
        
        TV_about.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	            Intent mainIntent = new Intent(MainActivity.this,AboutActivity.class);
	            MainActivity.this.startActivity(mainIntent);
	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
	        	_media_player.start();
	        }
	    }); 
        
        TV_settings.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	            Intent mainIntent = new Intent(MainActivity.this,SettingsActivity.class);
	            MainActivity.this.startActivity(mainIntent);
	            _settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
	        	_media_player.start();
	        }
	    }); 
        
        TV_challenge.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	            Intent mainIntent = new Intent(MainActivity.this,ChallengeLessonsActivity.class);
	            MainActivity.this.startActivity(mainIntent);
	            _settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
	        	_media_player.start();
	        }
	    }); 
        
        LinearLayout LL_Share = (LinearLayout)findViewById(R.id.share);
        ImageView IV_Logo = (ImageView) findViewById(R.id.logo);
        IV_Logo.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume * 0.5), 0);
	        	_media_player.start();
	        	Intent intent = new Intent(Intent.ACTION_SEND);
	        	intent.setType("text/plain");
	        	intent.putExtra(Intent.EXTRA_TEXT, "I'm using 'Good Luck with TOEFL' now and I strongly suggest you to try it. \n\nOfficial website:\nwww.goodluckapps.ir \n\nCafe Bazar:\nwww.cafebazaar.ir/app/ir.goodluckapps.vocabulary");
	        	startActivity(Intent.createChooser(intent, "Share with"));
	        }
	    }); 
        LL_Share.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume * 0.5), 0);
	        	_media_player.start();
	        	Intent intent = new Intent(Intent.ACTION_SEND);
	        	intent.setType("text/plain");
	        	intent.putExtra(Intent.EXTRA_TEXT, "I'm using 'Good Luck with TOEFL' now and I strongly suggest you to try it. \n\nOfficial website:\nwww.goodluckapps.ir \n\nCafe Bazar:\nwww.cafebazaar.ir/app/ir.goodluckapps.vocabulary");
	        	startActivity(Intent.createChooser(intent, "Share with"));
	        }
	    });
        _settings = DM.getSettings();
        if(_settings._reset_notification && _settings._notification)
        {
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
        
        	Intent intentAlarm = new Intent(this, AlarmReciever.class);
        	AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        	alarmManager.set(AlarmManager.RTC_WAKEUP, Time_next.getTime(), PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        	DM.setSettings_reset_notification(false);
        	_settings._reset_notification = false;
        	((GoodLuckApp)getApplicationContext()).setSettings(_settings);
        }
	}
	
	@Override
	public void onBackPressed()
	{
        dialog.show();
	}
}
