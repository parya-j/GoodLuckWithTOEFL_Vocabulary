package ir.goodluckapps.vocabulary;

public class Notification {
	public int _id;
	public String _body;
	public String _author;
	
	public Notification()
	{
		
	}
	public Notification(int id, String body, String author)
	{
		_id = id;
		_body = body;
		_author = author;
	}
}
