package ir.goodluckapps.vocabulary;

public class Meaning {
	int _word_id;
	String _persian_meaning;
	String _synonyms;
	
	public Meaning()
	{
		
	}
	
	public Meaning(int word_id, String persian_meaning, String synonyms)
	{
		this._word_id = word_id;
		this._persian_meaning = persian_meaning;
		this._synonyms = synonyms;
	}
	
	public Meaning(String persian_meaning, String synonyms)
	{
		this._persian_meaning = persian_meaning;
		this._synonyms = synonyms;
	}
	
	public int getWordId()
	{
		return this._word_id;
	}
	
	public String getPersianMeaning()
	{
		return this._persian_meaning;
	}
	
	public String getSynonyms()
	{
		return this._synonyms;
	}
}
