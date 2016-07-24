package ir.goodluckapps.vocabulary;

public class Settings {
	public float _volume;
	public int _font_size;
	public boolean _notification;
	public String _version;
	public boolean _reset_notification;
	
	public Settings()
	{
		_volume = 1.5f;
		_font_size = 0;
		_notification = true;
		_version = "1.0";
		_reset_notification = true;
	}
	
	public Settings(float volume, int font_size, boolean notification, String version, boolean reset_notification)
	{
		_volume = volume;
		_font_size = font_size;
		_notification = notification;
		_version = version;
		_reset_notification = reset_notification;
	}
}
