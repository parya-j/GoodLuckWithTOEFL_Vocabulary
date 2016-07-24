package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ChallengeLessonsActivity  extends Activity
{
		private Settings _settings;
	   @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        //Remove title bar
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_challenge_lessons);
			TextView TV_title = (TextView)findViewById(R.id.title_text);
			TV_title.setText("Good Luck with TOEFL - Challenge");
			TV_title.setBackgroundColor(Color.parseColor("#961013"));
			
			View view_seperator = findViewById(R.id.view_seperator);
			view_seperator.setBackgroundColor(Color.parseColor("#402223"));
	        final MediaPlayer _media_player;
	        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
	        _media_player = MediaPlayer.create(this, res_id);
	        LinearLayout LL_Lessons = (LinearLayout)findViewById(R.id.LinearLayout_lessons);
	        DatabaseManager DM = new DatabaseManager(this);
	        _settings = DM.getSettings();
	        for(int index = 0; index < 16; index++)
	        {
		        View child = getLayoutInflater().inflate(R.layout.element_challenge_lesson, null);
		        Button btn_go_Lesson = (Button) child.findViewById(R.id.go_lesson);
		        btn_go_Lesson.setText("Lesson " + (index + 1));
		        // TimeLine Write button click event
		        final int final_index = index;
		        btn_go_Lesson.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View arg0) {
						AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
						int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
			        	_media_player.start();
			        	
			            Intent i = new Intent(ChallengeLessonsActivity.this, ChallengeGameActivity.class);
			            i.putExtra("lesson_number", String.valueOf(final_index + 1));
			            startActivity(i);
			        }
			    });    
		        LL_Lessons.addView(child);
	        }
	    }
}
