/**
 * 
 */
package ir.goodluckapps.vocabulary;

/**
 * @author Dophlin
 *
 */

public class ChallengeRow {
	
	int _lesson_number;
	int _word_number;
	String _questionableWords;
	String _remainWords;
	int _score;
	
	public ChallengeRow()
	{
		
	}
	
	public ChallengeRow(int lesson_number, int word_number, String questionableWords, String remainWords, int score)
	{
		this._lesson_number = lesson_number;
		this._word_number = word_number;
		this._questionableWords = questionableWords;
		this._remainWords = remainWords;
		this._score = score;
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
	
	public String getQuestionableWords()
	{
		return this._questionableWords;
	}
	
	public void setQuestionableWords(String questionableWords)
	{
		this._questionableWords = questionableWords;
	}
	
	public String getRemainWords()
	{
		return this._remainWords;
	}
	
	public void setRemainWords(String remainWords)
	{
		this._remainWords = remainWords;
	}
	
	public int getScore()
	{
		return this._score;
	}
	
	public void setScore(int score)
	{
		this._score = score;
	}
}
