package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChallengeGameActivity  extends Activity
{
	public int _lesson_number;
	public DatabaseManager _DM;
	public int _score = 0;
	public float _default_font_size;
	public void resetStyle()
	{
		findViewById(R.id.text_word_first).setTag(null);
		findViewById(R.id.text_word_first).setClickable(true);
		((TextView)findViewById(R.id.text_word_first)).setBackgroundResource(R.drawable.element_challenge_answer);
		((TextView)findViewById(R.id.text_word_first)).setTextColor(Color.BLACK);
		findViewById(R.id.text_word_second).setTag(null);
		findViewById(R.id.text_word_second).setClickable(true);
		((TextView)findViewById(R.id.text_word_second)).setBackgroundResource(R.drawable.element_challenge_answer);
		((TextView)findViewById(R.id.text_word_second)).setTextColor(Color.BLACK);
		findViewById(R.id.text_word_third).setTag(null);
		findViewById(R.id.text_word_third).setClickable(true);
		((TextView)findViewById(R.id.text_word_third)).setBackgroundResource(R.drawable.element_challenge_answer);
		((TextView)findViewById(R.id.text_word_third)).setTextColor(Color.BLACK);
		findViewById(R.id.text_word_forth).setTag(null);
		findViewById(R.id.text_word_forth).setClickable(true);
		((TextView)findViewById(R.id.text_word_forth)).setBackgroundResource(R.drawable.element_challenge_answer);
		((TextView)findViewById(R.id.text_word_forth)).setTextColor(Color.BLACK);
		((TextView)findViewById(R.id.text_earn_points)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.text_next)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.text_question_mark)).setVisibility(View.VISIBLE);
		
	}
	
	public void prepareNextWord()
	{
        resetStyle();
        findViewById(R.id.text_next).setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {   	
	        	prepareNextWord();
	        }
	    }); 
        
        
        int countChallengeWords = _DM.getCountChallengeWords(_lesson_number);
        List<ChallengeWord> selectedChallengeWords = getFourRandChallengeWords(_lesson_number, countChallengeWords);
        
        String[] synonyms = selectedChallengeWords.get(0).getSynonyms().split(",");
        String[] selectedSynonyms = getTwoSynonyms(synonyms);
        ((TextView)findViewById(R.id.highlighted_word)).setText(selectedSynonyms[0]);
        
        List<Integer> poses = new ArrayList<Integer>();
        
        poses.add(rand_instead(4, poses));
        setAnswer(poses.get(0) + 1, selectedSynonyms[1], true);
        
        poses.add(rand_instead(4, poses));
        setAnswer(poses.get(1) + 1, getOneSynonyms(selectedChallengeWords.get(1).getSynonyms().split(",")), false);

        poses.add(rand_instead(4, poses));
        setAnswer(poses.get(2) + 1, getOneSynonyms(selectedChallengeWords.get(2).getSynonyms().split(",")), false);
        
        poses.add(rand_instead(4, poses));
        setAnswer(poses.get(3) + 1, getOneSynonyms(selectedChallengeWords.get(3).getSynonyms().split(",")), false);
        
		TextView highlighted_word = (TextView)findViewById(R.id.highlighted_word);
		highlighted_word.setTextSize(TypedValue.COMPLEX_UNIT_SP, _default_font_size);
		if(highlighted_word.getLineCount() > 1)
			highlighted_word.setTextSize(TypedValue.COMPLEX_UNIT_SP, _default_font_size - 2);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_challenge_game);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("lesson_number");
            _lesson_number = Integer.parseInt(value);
        }  
        TextView highlighted_word = (TextView)findViewById(R.id.highlighted_word);
        _default_font_size = pixelsToSp(highlighted_word.getTextSize());
        
        _DM = new DatabaseManager(this);
        _score = _DM.getChallengeScore(_lesson_number);
        ((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
        TextView TV_title = (TextView)findViewById(R.id.text_title_red);
        TV_title.setText("Good Luck On Challenge - Lesson " + Integer.toString(_lesson_number));
        _DM.createChallengeWords(_lesson_number);
        prepareNextWord();        
    }
    
    public float pixelsToSp(float px) {
        float scaledDensity =  this.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }
    
    public void setAnswer(int position, String word, boolean correctAnswer)
    {
    	switch(position)
        {
        	case 1:
        	{
        		TextView TV_word_first = (TextView)findViewById(R.id.text_word_first);
        		TV_word_first.setText(word);
        		TV_word_first.setTag(correctAnswer);
        		TV_word_first.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View arg0) {
			        	//--------------------------------
			        	Boolean tag = (Boolean)arg0.getTag();
			        	if(tag.booleanValue())
			        	{
			        		arg0.setBackgroundResource(R.drawable.element_challenge_answer_green);
			        		((TextView)arg0).setTextColor(Color.parseColor("#ffffff"));
			        		((TextView)findViewById(R.id.text_earn_points)).setText("You earned: 50 points");
			        		((TextView)findViewById(R.id.text_earn_points)).setTextColor(Color.BLACK);
			        		_score += 50;
			        		_DM.setChallengeScore(_lesson_number, _score);
			        		((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
			        	}
			        	else
			        	{
			        		arg0.setBackgroundResource(R.drawable.element_challenge_answer_red);
			        		((TextView)arg0).setTextColor(Color.parseColor("#ffffff"));
			        		
			        		LinearLayout LV = (LinearLayout) findViewById(R.id.LL_Up_words);
			        		TextView TV_currect_answer = (TextView) LV.findViewWithTag(true);
			        		if(TV_currect_answer == null)
			        		{
				        		LV = (LinearLayout) findViewById(R.id.LL_Down_words);
				        		TV_currect_answer = (TextView) LV.findViewWithTag(true);			        			
			        		}
			        		TV_currect_answer.setBackgroundResource(R.drawable.element_challenge_answer_green);
			        		TV_currect_answer.setTextColor(Color.parseColor("#ffffff"));
			        		((TextView)findViewById(R.id.text_earn_points)).setText("You lost: 50 points");
			        		((TextView)findViewById(R.id.text_earn_points)).setTextColor(Color.RED);
			        		_score -= 50;
			        		_DM.setChallengeScore(_lesson_number, _score);
			        		((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
			        	}
			        	findViewById(R.id.text_word_first).setClickable(false);
			        	findViewById(R.id.text_word_second).setClickable(false);
			        	findViewById(R.id.text_word_third).setClickable(false);
			        	findViewById(R.id.text_word_forth).setClickable(false);
			        	findViewById(R.id.text_next).setVisibility(View.VISIBLE);
			        	findViewById(R.id.text_earn_points).setVisibility(View.VISIBLE);
			        	findViewById(R.id.text_question_mark).setVisibility(View.INVISIBLE);
			        }
			    }); 
        		break;
        	}
        	case 2:
        	{
        		TextView TV_word_second = (TextView)findViewById(R.id.text_word_second);
        		TV_word_second.setText(word);
        		TV_word_second.setTag(correctAnswer);
        		TV_word_second.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View arg0) {
			        	//--------------------------------
			        	Boolean tag = (Boolean)arg0.getTag();
			        	if(tag.booleanValue())
			        	{
			        		arg0.setBackgroundResource(R.drawable.element_challenge_answer_green);
			        		((TextView)arg0).setTextColor(Color.parseColor("#ffffff"));
			        		((TextView)findViewById(R.id.text_earn_points)).setText("You earned: 50 points");
			        		((TextView)findViewById(R.id.text_earn_points)).setTextColor(Color.BLACK);
			        		_score += 50;
			        		_DM.setChallengeScore(_lesson_number, _score);
			        		((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
			        	}
			        	else
			        	{
			        		arg0.setBackgroundResource(R.drawable.element_challenge_answer_red);
			        		((TextView)arg0).setTextColor(Color.parseColor("#ffffff"));
			        		
			        		LinearLayout LV = (LinearLayout) findViewById(R.id.LL_Up_words);
			        		TextView TV_currect_answer = (TextView) LV.findViewWithTag(true);
			        		if(TV_currect_answer == null)
			        		{
				        		LV = (LinearLayout) findViewById(R.id.LL_Down_words);
				        		TV_currect_answer = (TextView) LV.findViewWithTag(true);			        			
			        		}
			        		TV_currect_answer.setBackgroundResource(R.drawable.element_challenge_answer_green);
			        		TV_currect_answer.setTextColor(Color.parseColor("#ffffff"));
			        		((TextView)findViewById(R.id.text_earn_points)).setText("You lost: 50 points");
			        		((TextView)findViewById(R.id.text_earn_points)).setTextColor(Color.RED);
			        		_score -= 50;
			        		_DM.setChallengeScore(_lesson_number, _score);
			        		((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
			        	}
			        	findViewById(R.id.text_word_first).setClickable(false);
			        	findViewById(R.id.text_word_second).setClickable(false);
			        	findViewById(R.id.text_word_third).setClickable(false);
			        	findViewById(R.id.text_word_forth).setClickable(false);
			        	findViewById(R.id.text_next).setVisibility(View.VISIBLE);
			        	findViewById(R.id.text_earn_points).setVisibility(View.VISIBLE);
			        	findViewById(R.id.text_question_mark).setVisibility(View.INVISIBLE);   
			        }
			    }); 
        		break;
        	}
        	case 3:
        	{
        		TextView TV_word_third = (TextView)findViewById(R.id.text_word_third);
        		TV_word_third.setText(word);
        		TV_word_third.setTag(correctAnswer);
        		TV_word_third.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View arg0) {
			        	//--------------------------------
			        	Boolean tag = (Boolean)arg0.getTag();
			        	if(tag.booleanValue())
			        	{
			        		arg0.setBackgroundResource(R.drawable.element_challenge_answer_green);
			        		((TextView)arg0).setTextColor(Color.parseColor("#ffffff"));
			        		((TextView)findViewById(R.id.text_earn_points)).setText("You earned: 50 points");
			        		((TextView)findViewById(R.id.text_earn_points)).setTextColor(Color.BLACK);
			        		_score += 50;
			        		_DM.setChallengeScore(_lesson_number, _score);
			        		((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
			        	}
			        	else
			        	{
			        		arg0.setBackgroundResource(R.drawable.element_challenge_answer_red);
			        		((TextView)arg0).setTextColor(Color.parseColor("#ffffff"));
			        		
			        		LinearLayout LV = (LinearLayout) findViewById(R.id.LL_Up_words);
			        		TextView TV_currect_answer = (TextView) LV.findViewWithTag(true);
			        		if(TV_currect_answer == null)
			        		{
				        		LV = (LinearLayout) findViewById(R.id.LL_Down_words);
				        		TV_currect_answer = (TextView) LV.findViewWithTag(true);			        			
			        		}
			        		TV_currect_answer.setBackgroundResource(R.drawable.element_challenge_answer_green);
			        		TV_currect_answer.setTextColor(Color.parseColor("#ffffff"));
			        		((TextView)findViewById(R.id.text_earn_points)).setText("You lost: 50 points");
			        		((TextView)findViewById(R.id.text_earn_points)).setTextColor(Color.RED);
			        		_score -= 50;
			        		_DM.setChallengeScore(_lesson_number, _score);
			        		((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
			        	}
			        	findViewById(R.id.text_word_first).setClickable(false);
			        	findViewById(R.id.text_word_second).setClickable(false);
			        	findViewById(R.id.text_word_third).setClickable(false);
			        	findViewById(R.id.text_word_forth).setClickable(false);
			        	findViewById(R.id.text_next).setVisibility(View.VISIBLE);
			        	findViewById(R.id.text_earn_points).setVisibility(View.VISIBLE);
			        	findViewById(R.id.text_question_mark).setVisibility(View.INVISIBLE);
			        }
			    }); 
        		break;
        	}
        	case 4:
        	{
        		TextView TV_word_forth = (TextView)findViewById(R.id.text_word_forth);
        		TV_word_forth.setText(word);
        		TV_word_forth.setTag(correctAnswer);
        		TV_word_forth.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View arg0) {
			        	//--------------------------------
			        	Boolean tag = (Boolean)arg0.getTag();
			        	if(tag.booleanValue())
			        	{
			        		arg0.setBackgroundResource(R.drawable.element_challenge_answer_green);
			        		((TextView)arg0).setTextColor(Color.parseColor("#ffffff"));
			        		((TextView)findViewById(R.id.text_earn_points)).setText("You earned: 50 points");
			        		((TextView)findViewById(R.id.text_earn_points)).setTextColor(Color.BLACK);
			        		_score += 50;
			        		_DM.setChallengeScore(_lesson_number, _score);
			        		((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
			        	}
			        	else
			        	{
			        		arg0.setBackgroundResource(R.drawable.element_challenge_answer_red);
			        		((TextView)arg0).setTextColor(Color.parseColor("#ffffff"));
			        		
			        		LinearLayout LV = (LinearLayout) findViewById(R.id.LL_Up_words);
			        		TextView TV_currect_answer = (TextView) LV.findViewWithTag(true);
			        		if(TV_currect_answer == null)
			        		{
				        		LV = (LinearLayout) findViewById(R.id.LL_Down_words);
				        		TV_currect_answer = (TextView) LV.findViewWithTag(true);			        			
			        		}
			        		TV_currect_answer.setBackgroundResource(R.drawable.element_challenge_answer_green);
			        		TV_currect_answer.setTextColor(Color.parseColor("#ffffff"));
			        		((TextView)findViewById(R.id.text_earn_points)).setText("You lost: 50 points");
			        		((TextView)findViewById(R.id.text_earn_points)).setTextColor(Color.RED);
			        		_score -= 50;
			        		_DM.setChallengeScore(_lesson_number, _score);
			        		((TextView)findViewById(R.id.text_score)).setText("Score: " + _score);
			        	}
			        	findViewById(R.id.text_word_first).setClickable(false);
			        	findViewById(R.id.text_word_second).setClickable(false);
			        	findViewById(R.id.text_word_third).setClickable(false);
			        	findViewById(R.id.text_word_forth).setClickable(false);
			        	findViewById(R.id.text_next).setVisibility(View.VISIBLE);
			        	findViewById(R.id.text_earn_points).setVisibility(View.VISIBLE);
			        	findViewById(R.id.text_question_mark).setVisibility(View.INVISIBLE);
			        }
			    }); 
        		break;
        	}
        }
    }

    public String[] getTwoSynonyms(String[] synonyms)
    {
    	Random rand = new Random();
    	int first_syn_order = rand.nextInt(synonyms.length);
    	int second_syn_order;
    	boolean pursue = true;
    	do
    	{
    		second_syn_order = rand.nextInt(synonyms.length);
    		if(second_syn_order != first_syn_order)
    			pursue = false;
    	}while(pursue);
    	String[] res = {synonyms[first_syn_order].trim(), synonyms[second_syn_order].trim()};
    	return res;
    }
    
    public String getOneSynonyms(String[] synonyms)
    {
    	Random rand = new Random();
    	return synonyms[rand.nextInt(synonyms.length)].trim();
    }
    
    public List<ChallengeWord> getFourRandChallengeWords(int lesson_number, int maxPossibleNumber)
    {
    	Random rand = new Random();
    	int first = rand.nextInt(maxPossibleNumber);
    	int second;
    	int third;
    	int forth;
    	int first_id = _DM.getFirstIdChallengeLesson(lesson_number);
    	first = first_id + first;
    	ChallengeWord first_challengeWord = _DM.getChallengeWord(lesson_number, first);
    	boolean pursue = true;
    	do
    	{
    		second = rand.nextInt(maxPossibleNumber) + first_id;
    		if(second != first && !("," + first_challengeWord.getConflictIds() + ",").contains("," + Integer.toString(second) + ","))
    			pursue = false;
    	}while(pursue);
    	ChallengeWord second_challengeWord = _DM.getChallengeWord(lesson_number, second);
    	pursue = true;
    	do
    	{
    		third = rand.nextInt(maxPossibleNumber) + first_id;
    		if(third != first && third != second 
    						  && !("," + first_challengeWord.getConflictIds() + ",").contains("," + Integer.toString(third) + ",")
    						  && !("," + second_challengeWord.getConflictIds() + ",").contains("," + Integer.toString(third) + ","))
    			pursue = false;
    	}while(pursue);
    	ChallengeWord third_challengeWord = _DM.getChallengeWord(lesson_number, third);
    	pursue = true;
    	do
    	{
    		forth = rand.nextInt(maxPossibleNumber) + first_id;
    		if(forth != first && forth != second && forth != third
    						  && !("," + first_challengeWord.getConflictIds() + ",").contains("," + Integer.toString(forth) + ",")
    						  && !("," + second_challengeWord.getConflictIds() + ",").contains("," + Integer.toString(forth) + ",")
    						  && !("," + third_challengeWord.getConflictIds() + ",").contains("," + Integer.toString(forth) + ","))
    			pursue = false;
    	}while(pursue);
    	ChallengeWord forth_challengeWord = _DM.getChallengeWord(lesson_number, forth);
    	List<ChallengeWord> res = new ArrayList<ChallengeWord>();
    	res.add(first_challengeWord);
    	res.add(second_challengeWord);
    	res.add(third_challengeWord);
    	res.add(forth_challengeWord);
    	return res;
    }

    public int rand_instead(int max, List<Integer> insteads)
    {
    	Random rand = new Random();
    	int new_rand = 0;
    	boolean enough = false;
    	do
    	{
    		new_rand = rand.nextInt(max);
    		enough = true;
    		for(int index = 0; index < insteads.size(); index++)
    			if(new_rand == insteads.get(index))
    				enough = false;
    	}while(!enough);
    	return new_rand;    	
    }
    public int rand_instead(int max, int[] insteads)
    {
    	Random rand = new Random();
    	int new_rand = 0;
    	boolean enough = false;
    	do
    	{
    		new_rand = rand.nextInt(max);
    		enough = true;
    		for(int index = 0; index < insteads.length; index++)
    			if(new_rand == insteads[index])
    				enough = false;
    	}while(!enough);
    	return new_rand;
    }
    public int rand_instead(int max, int instead)
    {
    	Random rand = new Random();
    	int new_rand = 0;
    	do
    	{
    		new_rand = rand.nextInt(max);
    	}while(new_rand == instead);
    	return new_rand;
    }
}
