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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LessonActivity  extends Activity
{
	private Settings _settings;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lesson);
       
        int lesson_number = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("lesson_number");
            lesson_number = Integer.parseInt(value);
        }
        
		TextView TV_title = (TextView)findViewById(R.id.title_text);
		TV_title.setText("Lesson " + Integer.toString(lesson_number));
        
        
        DatabaseManager DM = new DatabaseManager(this);
        List<Word> words = DM.getWordsOfLesson(lesson_number);
        
        final MediaPlayer _media_player;
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _media_player = MediaPlayer.create(this, res_id);
        
        LinearLayout LL_Words = (LinearLayout)findViewById(R.id.LinearLayout_meanings);
        _settings = ((GoodLuckApp)getApplicationContext()).getSettings();
        for(int index = 0; index < words.size(); index++)
        {
	        View child = getLayoutInflater().inflate(R.layout.element_word, null);
	        Button btn_go_Word = (Button) child.findViewById(R.id.go_word);
	        Word word = words.get(index);
	        btn_go_Word.setText(word.getWord());
	        ImageView IV_star = (ImageView)child.findViewById(R.id.image_star);
	        if(DM.isFavorite(word.getId()))
	        {
	        	
	        	IV_star.setImageResource(R.drawable.star_gold);
	        }
	        else
	        {
	        	IV_star.setImageResource(R.drawable.star_grey);	        	
	        }
	        final DatabaseManager final_DM = DM;
	        final int final_word_number = word.getId();
	        IV_star.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View arg0) {
		        	if(final_DM.isFavorite(final_word_number))
		        	{
		        		if(final_DM.removeFavoriteWord(final_word_number))
		        		{
		        			ImageView IV_crr_star = (ImageView)arg0.findViewById(R.id.image_star);
		        			IV_crr_star.setImageResource(R.drawable.star_grey);
		        		}
		        	}else
		        	{
		        		if(final_DM.addFavoriteWord(final_word_number))
		        		{
		        			ImageView IV_crr_star = (ImageView)arg0.findViewById(R.id.image_star);
		        			IV_crr_star.setImageResource(R.drawable.star_gold);
		        		}
		        	}
		        }
		    }); 
	        final int final_index = (lesson_number - 1) * 10 + index;
	        final int final_lesson_number = lesson_number;
	        btn_go_Word.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View arg0) {
					AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
					int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
		        	_media_player.start();
		        	
		            Intent i = new Intent(LessonActivity.this, WordActivity.class);
		            i.putExtra("word_number", String.valueOf(final_index + 1));
		            i.putExtra("lesson_number", String.valueOf(final_lesson_number));
		            startActivityForResult(i, final_index + 1);
		        }
		    });
	        
	        child.setTag(word.getId());
	        LL_Words.addView(child);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	LinearLayout LL_Words = (LinearLayout)findViewById(R.id.LinearLayout_meanings);
    	View view_Word = LL_Words.findViewWithTag(requestCode);
    	ImageView IV_star = (ImageView)view_Word.findViewById(R.id.image_star);
    	
    	DatabaseManager DM = new DatabaseManager(this);
    	if(DM.isFavorite(requestCode))
    	{
    		IV_star.setImageResource(R.drawable.star_gold);
    	}
    	else
    	{
    		IV_star.setImageResource(R.drawable.star_grey);
    	}
    }//onActivityResult
}
