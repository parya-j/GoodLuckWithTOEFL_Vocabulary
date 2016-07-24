package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class LessonsActivity  extends Activity
{
	private Settings _settings;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lessons);
        
        final MediaPlayer _media_player;
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _media_player = MediaPlayer.create(this, res_id);
        
        DatabaseManager DM = new DatabaseManager(this);
        List<Word> wordsList = DM.getAllWords();
        int wordsCount = wordsList.size();
        int lessonsCount = (int)(wordsCount / 10) + ( (wordsCount % 10) == 0 ? 0 : 1);
        LinearLayout LL_Lessons = (LinearLayout)findViewById(R.id.LinearLayout_lessons);
        _settings = ((GoodLuckApp)getApplicationContext()).getSettings();
        for(int index = 0; index < lessonsCount; index++)
        {
	        View child = getLayoutInflater().inflate(R.layout.element_lesson, null);
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
		        	
		            Intent i = new Intent(LessonsActivity.this, LessonActivity.class);
		            i.putExtra("lesson_number", String.valueOf(final_index + 1));
		            startActivity(i);
		        }
		    });    
	        LL_Lessons.addView(child);
        }
    }
}
