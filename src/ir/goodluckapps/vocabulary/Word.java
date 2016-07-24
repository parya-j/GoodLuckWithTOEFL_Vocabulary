/**
 * 
 */
package ir.goodluckapps.vocabulary;

/**
 * @author Dophlin
 *
 */

public class Word {
	
	int _id;
	String _word;
	String _phonetics;
	String _partOfSpeech;
	String _description;
	String _examples;
	
	public Word()
	{
		
	}
	
	public Word(String word, String phonetics, String partOfSpeech, String description, String examples)
	{
		this._word = word;
		this._phonetics = phonetics;
		this._partOfSpeech = partOfSpeech;
		this._description = description;
		this._examples = examples;
	}

	public Word(int id, String word, String phonetics, String partOfSpeech, String description, String examples)
	{
		this._id = id;
		this._word = word;
		this._phonetics = phonetics;
		this._partOfSpeech = partOfSpeech;
		this._description = description;
		this._examples = examples;
	}
	
	public int getId()
	{
		return this._id;
	}
	
	public void setId(int id)
	{
		this._id = id;
	}
	
	public String getWord()
	{
		return this._word;
	}
	
	public void setWord(String word)
	{
		this._word = word;
	}
	
	public String getPhonetics()
	{
		return this._phonetics;
	}
	
	public void setPhonetics(String phonetics)
	{
		this._phonetics = phonetics;
	}
	
	public String getPartOfSpeech()
	{
		return this._partOfSpeech;
	}
	
	public void setPartOfSpeech(String partOfSpeech)
	{
		this._partOfSpeech = partOfSpeech;
	}
	
	public String getDescription()
	{
		return this._description;
	}
	
	public void setDescription(String description)
	{
		this._description = description;
	}
	
	public String getExamples()
	{
		return this._examples;
	}
	
	public void setExamples(String examples)
	{
		this._examples = examples;
	}
}
