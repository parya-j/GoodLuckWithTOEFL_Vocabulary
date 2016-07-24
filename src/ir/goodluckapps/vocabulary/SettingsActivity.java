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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	private DatabaseManager _DM;
	private Settings _settings;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        _DM = new DatabaseManager(this);
        _settings = ((GoodLuckApp)getApplicationContext()).getSettings();
        final MediaPlayer _media_player;
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _media_player = MediaPlayer.create(this, res_id);
        SeekBar volumeControl = (SeekBar) findViewById(R.id.volume_input);
		volumeControl.setProgress((int)((_settings._volume / 1.5) * 50));
		
        volumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progressChanged = 50;

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				progressChanged = progress;
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				_DM.setSettings_Volume(progressChanged);
				_settings._volume = (float) ((progressChanged / 50.0) * 1.5);
				((GoodLuckApp)getApplicationContext()).setSettings(_settings);
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
	        	_media_player.start();
			}
		});
        
        RadioGroup radioGroup_fontSize = (RadioGroup) findViewById(R.id.radioFontSize);
        if(_settings._font_size == 0)
        {
        	((RadioButton)radioGroup_fontSize.findViewById(R.id.radioButton_font_size_large)).setChecked(false);
        	((RadioButton)radioGroup_fontSize.findViewById(R.id.radioButton_font_size_normal)).setChecked(true);
        }
        else
        {
        	((RadioButton)radioGroup_fontSize.findViewById(R.id.radioButton_font_size_normal)).setChecked(false);
        	((RadioButton)radioGroup_fontSize.findViewById(R.id.radioButton_font_size_large)).setChecked(true);        	
        }
        radioGroup_fontSize.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	if(checkedId == R.id.radioButton_font_size_normal)
            	{
            		_settings._font_size = 0;
            	}else if(checkedId == R.id.radioButton_font_size_large)
            	{
            		_settings._font_size = 2;
            	}
        		_DM.setSettings_FontSize(_settings._font_size);
            	((GoodLuckApp)getApplicationContext()).setSettings(_settings);
            }
        });
        
    	CheckBox checkBox_notification = (CheckBox) findViewById(R.id.notification_input);
    	checkBox_notification.setChecked(_settings._notification);
    	checkBox_notification.setOnClickListener(new OnClickListener() {
    	  @Override
    	  public void onClick(View v) {
    		  _settings._notification = ((CheckBox) v).isChecked();
    		  _DM.setSettings_notification(_settings._notification);
    		  ((GoodLuckApp)getApplicationContext()).setSettings(_settings);
              if(_settings._notification)
              {
            	Context context = SettingsActivity.this;
            	
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
            	AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            	alarmManager.set(AlarmManager.RTC_WAKEUP, Time_next.getTime(), PendingIntent.getBroadcast(context,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
              }
    	  }
    	});
    	
    	final Dialog dialog = new Dialog(this);
    	Button btn_removeFavorites = (Button)findViewById(R.id.button_removeFavorites);
    	btn_removeFavorites.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
	        	_media_player.start();

                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Remove Favorites");
                ((TextView)dialog.findViewById(R.id.textView_term)).setText("Are you sure that you want to remove your favorites?");
                Button btn_clear = (Button) dialog.findViewById(R.id.Yes);
                Button btn_cancel = (Button) dialog.findViewById(R.id.No);
                
                btn_cancel.setOnClickListener(new View.OnClickListener() {
        	        @Override
        	        public void onClick(View arg0) {
        	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
        				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
        	        	_media_player.start();
        	        	dialog.dismiss();
        	        }
        	    }); 
                btn_clear.setOnClickListener(new View.OnClickListener() {
        	        @Override
        	        public void onClick(View arg0) {
        	        	_DM.removeFavorites();
        	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
        				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
        	        	_media_player.start();
        	        	dialog.dismiss();
        	        }
        	    }); 
                dialog.show();
	        }
	    });
    	
    	Button btn_sendFeedback = (Button)findViewById(R.id.button_thanks);
    	
    	btn_sendFeedback.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	            Intent sendFeedbackIntent = new Intent(SettingsActivity.this, SendFeedbackActivity.class);
	            SettingsActivity.this.startActivity(sendFeedbackIntent);
	            
	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
	        	_media_player.start();
	        }
    	});
    	
    	Button btn_clearScores = (Button)findViewById(R.id.button_clearScores);
    	btn_clearScores.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
	        	_media_player.start();

                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Clear Scores");
                
                Button btn_clear = (Button) dialog.findViewById(R.id.Yes);
                Button btn_cancel = (Button) dialog.findViewById(R.id.No);
                
                btn_cancel.setOnClickListener(new View.OnClickListener() {
        	        @Override
        	        public void onClick(View arg0) {
        	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
        				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
        	        	_media_player.start();
        	        	dialog.dismiss();
        	        }
        	    }); 
                btn_clear.setOnClickListener(new View.OnClickListener() {
        	        @Override
        	        public void onClick(View arg0) {
        	        	_DM.cleanScores();
        	        	_settings = ((GoodLuckApp)getApplicationContext()).getSettings();
        				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
        	        	_media_player.start();
        	        	dialog.dismiss();
        	        }
        	    }); 
                dialog.show();
	        }
	    }); 
	}
}
