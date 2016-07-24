package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class ShowNotificationActivity extends Activity
{
   @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_notification);
        
		DatabaseManager DM = new DatabaseManager(this);
        int notification_id = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("notification_id");
            notification_id = Integer.parseInt(value);
        }
        Notification notification = DM.getNotification(notification_id);
		if(notification == null)
			return;
		
        TextView TV_body = ((TextView)findViewById(R.id.body));
        TV_body.setText(notification._body);
        
        TextView TV_author = ((TextView)findViewById(R.id.author));
        TV_author.setText("-" + notification._author);
        
        final MediaPlayer _media_player;
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _media_player = MediaPlayer.create(this, res_id);
        Button btn_sendFeedback = (Button) findViewById(R.id.button_thanks);
        btn_sendFeedback.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
				AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume), 0);
	        	_media_player.start();
	            Intent mainIntent = new Intent(ShowNotificationActivity.this, MainActivity.class);
	            ShowNotificationActivity.this.startActivity(mainIntent);
	            ShowNotificationActivity.this.finish();
	        }
	    });
    }
}
