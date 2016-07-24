package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class WordActivity extends Activity
{
	private TextToSpeech _TTS;
	private ClickableSpan getClickableSpan(final String word)
	{
		return new ClickableSpan() {
			final String _word;
			{
				_word = word;
			}
			@Override
			public void onClick(View arg0) {
	            _TTS.speak(_word, TextToSpeech.QUEUE_FLUSH, null);
			}
			
			@Override
	        public void updateDrawState(TextPaint ds) {
	            super.updateDrawState(ds);
	            ds.setColor(Color.BLACK);
	            ds.setUnderlineText(false);
	        }
		};
	}
	private void setupTTS(TextView textView)
	{
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		Spannable spans = (Spannable) textView.getText();
		String text = textView.getText().toString();
		int ignoreIndex = "Syn.".length();
		for(int end = ignoreIndex, start = ignoreIndex, index = ignoreIndex; index < text.length(); index++)
		{
			if(text.charAt(index) == ',')
			{
				end = index;
				String possibleWord = text.substring(start, end);
				while(possibleWord.charAt(0) == ' ' && start < end)
				{
					start++;
					possibleWord = text.substring(start, end);
				}
		        if (start < end && Character.isLetterOrDigit(possibleWord.charAt(0))) {
		            ClickableSpan clickSpan = getClickableSpan(possibleWord);
		            spans.setSpan(clickSpan, start, end,
		                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        }
		        start = index + 1; end = index + 1;
			}else if(index == text.length() - 1)
			{
				end = index;
				String possibleWord = text.substring(start, end + 1);
				while(possibleWord.charAt(0) == ' ' && start < end)
				{
					start++;
					possibleWord = text.substring(start, end + 1);
				}
		        if (start < end && Character.isLetterOrDigit(possibleWord.charAt(0))) {
		            ClickableSpan clickSpan = getClickableSpan(possibleWord);
		            spans.setSpan(clickSpan, start, end + 1,
		                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        }
		        start = index + 1; end = index + 1;				
			}
		}
	}
	private Settings _settings;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        _TTS = new TextToSpeech(getApplicationContext(), 
      	      new TextToSpeech.OnInitListener() {
      	      @Override
      	      public void onInit(int status) {
      	         if(status != TextToSpeech.ERROR){
      	             _TTS.setLanguage(Locale.US);
      	            }				
      	         }
      	      });
        
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_word);
        _settings = ((GoodLuckApp)getApplicationContext()).getSettings();
        
        int word_number = 0;
        int lesson_number = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("word_number");
            word_number = Integer.parseInt(value);
            String value_lesson = extras.getString("lesson_number");
            lesson_number = Integer.parseInt(value_lesson);
        }
        
		TextView TV_title = (TextView)findViewById(R.id.title_text);
		TV_title.setText("Lesson " + Integer.toString(lesson_number) + " - Word " + Integer.toString(word_number));
        
        DatabaseManager DM = new DatabaseManager(this);
        Word word = DM.getWord(word_number);
        if(DM.isFavorite(word.getId()))
        {
        	ImageView IV_star = (ImageView)findViewById(R.id.image_star);
        	IV_star.setImageResource(R.drawable.star_gold);        
        }
        else
        {
        	ImageView IV_star = (ImageView)findViewById(R.id.image_star);
        	IV_star.setImageResource(R.drawable.star_grey);    	
        }
        TextView TV_Word = (TextView)findViewById(R.id.word);
        TV_Word.setText(word.getWord());
        TextView TV_PartOfSpeech = (TextView)findViewById(R.id.part_of_speech);
        TV_PartOfSpeech.setText('"' + word.getPartOfSpeech().replace('\"',  ' ').trim() + '"');
        TV_PartOfSpeech.setTextColor(Color.RED);
        TextView TV_Definition = (TextView)findViewById(R.id.definition);
        TV_Definition.setTextSize(TypedValue.COMPLEX_UNIT_SP, pixelsToSp(TV_Definition.getTextSize()) + _settings._font_size);
        TV_Definition.setText(Html.fromHtml("<i>" + word.getDescription().replace('\"',  ' ').trim() + "</i>"));
        TextView TV_Phonetic = (TextView)findViewById(R.id.phonetic);
        Typeface type_phonetics = Typeface.createFromAsset(getAssets(),"fonts/Andika-R.ttf");
        TV_Phonetic.setTypeface(type_phonetics);
        TV_Phonetic.setText("/" + word.getPhonetics() + "/");
        List<Meaning> meanings = DM.getMeaning(word_number);
        LinearLayout LL_Meanings = (LinearLayout)findViewById(R.id.LinearLayout_meanings);
        for(int index = 0; index < meanings.size(); index++)
        {
	        View child = getLayoutInflater().inflate(R.layout.element_meaning, null);
	        TextView btn_go_Meaning = (TextView) child.findViewById(R.id.persian_meaning);
	        btn_go_Meaning.setTextSize(TypedValue.COMPLEX_UNIT_SP, pixelsToSp(btn_go_Meaning.getTextSize()) + _settings._font_size);
	        btn_go_Meaning.setText(Integer.toString(index + 1) + ") " + meanings.get(index).getPersianMeaning().replace('\"',  ' ').trim()); 
	        TextView btn_go_Synonyms = (TextView) child.findViewById(R.id.synonyms);
	        btn_go_Synonyms.setTextSize(TypedValue.COMPLEX_UNIT_SP, pixelsToSp(btn_go_Synonyms.getTextSize()) + _settings._font_size);
	        if(meanings.get(index).getSynonyms() != null)
	        {
		        btn_go_Synonyms.setText(Html.fromHtml("<b>Syn.</b> " + meanings.get(index).getSynonyms().replace('\"',  ' ').trim()), BufferType.SPANNABLE);
		        setupTTS(btn_go_Synonyms);
	        }else
	        {
	        	btn_go_Synonyms.setVisibility(View.GONE);
	        }
	        LL_Meanings.addView(child);
        }

        View child = getLayoutInflater().inflate(R.layout.element_examples, null);
        TextView btn_go_example = (TextView) child.findViewById(R.id.examples);
        String examples_pack = word.getExamples().replace('\"',  ' ').trim();
        String[] examples_list = examples_pack.split(";");
        String examples_show = "";
        for(int i = 0; i < examples_list.length; i++)
        {
        	examples_show += "* " + examples_list[i].trim() + "\r\n\r\n";
        }
        btn_go_example.setText(examples_show);
        btn_go_example.setTextColor(Color.rgb(0, 130, 0));
        btn_go_example.setTextSize(TypedValue.COMPLEX_UNIT_SP, pixelsToSp(btn_go_example.getTextSize()) + _settings._font_size);
        LL_Meanings.addView(child);

        final MediaPlayer _media_player;
        int res_id = this.getResources().getIdentifier(word.getWord().toLowerCase().replace(" ", "_"), "raw", this.getPackageName());
        _media_player = MediaPlayer.create(this, res_id);
	         LinearLayout LL_word_phonetic_audio = (LinearLayout)findViewById(R.id.LL_word_phonetic_audio);
	         LL_word_phonetic_audio.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View arg0) {
						AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
						int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * _settings._volume), 0);
			        	_media_player.start();
			        }
			    }); 
	         
	    RelativeLayout RL_title_star = (RelativeLayout)findViewById(R.id.RL_title_star);
	    final int final_word_number = word_number;
	    final DatabaseManager final_DM = DM;
	    RL_title_star.setOnClickListener(new View.OnClickListener() {
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
    }
    
    public float pixelsToSp(float px) {
        float scaledDensity =  this.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }
}
