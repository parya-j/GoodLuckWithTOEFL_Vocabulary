/**
 * 
 */
package ir.goodluckapps.vocabulary;

/**
 * @author Dophlin
 *
 */

public class ChallengeWord {
	
	int _id;
	int _lesson_number;
	int _word_number;
	int _meaning_id;
	String _synonyms;
	String _conflict_ids;
	
	public ChallengeWord()
	{
		
	}

	public ChallengeWord(int id, int lesson_number, int word_number, int meaning_id, String synonyms, String conflict_ids)
	{
		this._id = id;
		this._lesson_number = lesson_number;
		this._word_number = word_number;
		this._meaning_id = meaning_id;
		this._synonyms = synonyms;
		this._conflict_ids = conflict_ids;
	}
	
	public ChallengeWord(int lesson_number, int word_number, int meaning_id, String synonyms, String conflict_ids)
	{
		this._lesson_number = lesson_number;
		this._word_number = word_number;
		this._meaning_id = meaning_id;
		this._synonyms = synonyms;
		this._conflict_ids = conflict_ids;
	}
	
	public int getId()
	{
		return this._id;
	}
	
	public void setId(int id)
	{
		this._id = id;
	}
	
	public int getLessonNumber()
	{
		return this._lesson_number;
	}
	
	public void setLessonNumber(int lessonNumber)
	{
		this._lesson_number = lessonNumber;
	}
	
	public int getWordNumber()
	{
		return this._word_number;
	}
	
	public void setWordNumber(int wordNumber)
	{
		this._word_number = wordNumber;
	}
	
	public int getMeaningId()
	{
		return this._meaning_id;
	}
	
	public void setMeaningId(int meaningId)
	{
		this._meaning_id = meaningId;
	}
	
	public String getSynonyms()
	{
		return this._synonyms;
	}
	
	public void setSynonyms(String synonyms)
	{
		this._synonyms = synonyms;
	}
	
	public String getConflictIds()
	{
		return this._conflict_ids;
	}
	
	public void setConflictIds(String conflictIds)
	{
		this._conflict_ids = conflictIds;
	}
}
