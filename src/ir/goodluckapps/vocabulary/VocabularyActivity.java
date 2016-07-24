package ir.goodluckapps.vocabulary;

import ir.goodluckapps.vocabulary.R;
import android.R.color;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

public class VocabularyActivity extends TabActivity {

	TabHost tabHost;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vocabulary);
         
        tabHost = getTabHost();
         
        View view_tab_lessons = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        TextView label_lessons = (TextView) view_tab_lessons.findViewById(R.id.lbTabTitle);
        label_lessons.setText("Lessons");
        Intent lessonsIntent = new Intent(this, LessonsActivity.class);
        tabHost.addTab(tabHost.newTabSpec("tab_lessons").setIndicator(view_tab_lessons).setContent(lessonsIntent));

        View view_tab_favorites = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        TextView label_favorites = (TextView) view_tab_favorites.findViewById(R.id.lbTabTitle);
        label_favorites.setText("Favorites");
        Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
        tabHost.addTab(tabHost.newTabSpec("tab_favorites").setIndicator(view_tab_favorites).setContent(favoritesIntent));
       
		View tabView = tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab());
		View LinearLayout_tab = tabView.findViewById(R.id.LinearLayout_tab);
		LinearLayout_tab.setBackgroundColor(Color.parseColor("#222D8C"));
		TextView title_view = (TextView) LinearLayout_tab.findViewById(R.id.lbTabTitle);
		title_view.setTextColor(Color.WHITE);	
        
		TextView TV_title = (TextView)findViewById(R.id.title_text);
		TV_title.setText("Good Luck with TOEFL - Vocabulary");
		
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {				
					
					for(int ind = 0; ind < tabHost.getTabWidget().getTabCount(); ind++)
					{
						
						View tabView = tabHost.getTabWidget().getChildTabViewAt(ind);
						View LinearLayout_tab = tabView.findViewById(R.id.LinearLayout_tab);
						LinearLayout_tab.setBackgroundColor(color.white);
						TextView title_view = (TextView) LinearLayout_tab.findViewById(R.id.lbTabTitle);
						title_view.setTextColor(Color.parseColor("#151515"));
					}
					View tabView = tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab());
					View LinearLayout_tab = tabView.findViewById(R.id.LinearLayout_tab);
					LinearLayout_tab.setBackgroundColor(Color.parseColor("#222D8C"));
					TextView title_view = (TextView) LinearLayout_tab.findViewById(R.id.lbTabTitle);
					title_view.setTextColor(Color.WHITE);
					
					Activity currentAcitvity = getCurrentActivity();
					if(currentAcitvity instanceof FavoritesActivity)
					{
						((FavoritesActivity)currentAcitvity).update();
					}
			}
		});



    }
	
	@Override
	public void onResume() {
	    // do something on back.
		Activity currentAcitvity = getCurrentActivity();
		if(currentAcitvity instanceof FavoritesActivity)
		{
			((FavoritesActivity)currentAcitvity).update();
		}
		super.onResume();
	}
}
