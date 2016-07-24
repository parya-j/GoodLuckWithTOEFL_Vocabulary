package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavoritesActivity  extends Activity
{
	
		public void update()
		{
	        LinearLayout LL_Container = (LinearLayout)findViewById(R.id.LL_Container);
	        LL_Container.removeView(LL_Container.findViewById(R.id.title_text));
	        LL_Container.removeView(LL_Container.findViewById(R.id.view_seperator));
	        LinearLayout LL_Words = (LinearLayout)findViewById(R.id.LinearLayout_meanings);
	        LL_Words.removeAllViews();
	        
	        DatabaseManager DM = new DatabaseManager(this);
	        List<Word> favorite_words = DM.getFavorites();
	        
	        if(favorite_words.size() == 0)
	        {
	        	TextView TV_noFavority = new TextView(this);
	        	TV_noFavority.setText("There is no favorite word at the moment.");
	        	TV_noFavority.setGravity(Gravity.CENTER_HORIZONTAL);
	        	TV_noFavority.setPadding(0, 10, 0, 0);
	        	LL_Words.addView(TV_noFavority);
	        }
	        
	        for(int index = 0; index < favorite_words.size(); index++)
	        {
		        View child = getLayoutInflater().inflate(R.layout.element_word, null);
		        Button btn_go_Word = (Button) child.findViewById(R.id.go_word);
		        Word word = favorite_words.get(index);
		        btn_go_Word.setText(word.getWord());
		        ImageView IV_star = (ImageView)child.findViewById(R.id.image_star);
		        IV_star.setImageResource(R.drawable.star_gold);
		        final int final_word_id = word.getId();
		        btn_go_Word.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View arg0) {
			            Intent i = new Intent(FavoritesActivity.this, WordActivity.class);
			            int lessonsCount = (int)((final_word_id - 1) / 10) + 1;
			            
			            i.putExtra("word_number", String.valueOf(final_word_id));
			            i.putExtra("lesson_number", String.valueOf(lessonsCount));
			            startActivityForResult(i, final_word_id);
//			            startActivity(i);
			        }
			    });
		        child.setTag(word.getId());
		        LL_Words.addView(child);
	        }
		}
	   @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        //Remove title bar
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_lesson);
	        
	        this.update();
	    }
	    
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	LinearLayout LL_Words = (LinearLayout)findViewById(R.id.LinearLayout_meanings);
	    	View view_Word = LL_Words.findViewWithTag(requestCode);
	    	DatabaseManager DM = new DatabaseManager(this);
	    	if(!DM.isFavorite(requestCode))
	    		LL_Words.removeView(view_Word);
	    }//onActivityResult
}
