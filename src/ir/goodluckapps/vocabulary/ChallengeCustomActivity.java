package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


public class ChallengeCustomActivity  extends Activity
{

	   @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        //Remove title bar
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_lesson);	        
	    }
	    

}
