package ir.goodluckapps.vocabulary;

import android.app.Application;

public class GoodLuckApp extends Application {
	  private Settings _settings;
	  
	  public Settings getSettings()
	  {
		  return _settings;
	  }
	  public void setSettings(Settings settings)
	  {
		  _settings = settings;
	  }
}