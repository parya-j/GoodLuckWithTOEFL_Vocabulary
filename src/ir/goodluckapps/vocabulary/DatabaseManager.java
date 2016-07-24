package ir.goodluckapps.vocabulary;

import java.util.ArrayList;
import java.util.List;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "aryawords.sqlite";
    private static final int DATABASE_VERSION = 1;
	private static final String TABLE_WORDS = "main_words";
	private static final String TABLE_MEANINGS = "meanings";
	private static final String TABLE_Favorites = "favorites";
	private static final String TABLE_Challenge = "challenge";
	private static final String TABLE_ChallengeScores = "challenge_scores";
	private static final String TABLE_ChallengeWords = "challenge_words";
	private static final String TABLE_Settings = "settings";
	private static final String TABLE_Notifications = "notifications";
	
	// Words Table Columns names
	private static final String KEY_ID = "_id";
	private static final String KEY_WORD = "main_word";
	private static final String KEY_PHONETICS = "phonetics";
	private static final String KEY_PARTOFSPEECH = "part_of_speech";
	private static final String KEY_DESCRIPTION = "defenition";
	private static final String KEY_EXAMPLES = "examples";

	
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //----------------------------------------------------------------
    public void setNotification(int id, String body, String author)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_Notifications + " SET 'body' = '" + body + "' 'author' = '" + author + "' WHERE id = '" + id + "'";
		db.execSQL(updateQuery);
		return;    	
    }
    
    public Notification getNotification(int id)
    {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT id, body, author FROM " + TABLE_Notifications + " WHERE id = " + id , null);
		if(cursor == null || cursor.getCount() == 0)
			return new Notification();

		Notification notification = new Notification();
		if(cursor.moveToFirst())
		{
			notification = new Notification(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		}
		cursor.close();
		return notification;    
    }
    
    public int getNotificationsCount()
    {
		String countQuery = "SELECT count(*) FROM " + TABLE_Notifications;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = 0;
		if(cursor.moveToFirst())
		{
			count = cursor.getInt(0);
		}
		cursor.close();
		return count;
    }
    //----------------------------------------------------------------
    public void cleanScores()
    {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ TABLE_ChallengeScores);
    }
    public void removeFavorites()
    {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ TABLE_Favorites);
    }
    //----------------------------------------------------------------
    public void setSettings_Volume(float volume)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_Settings + " SET 'value' = '" + (int)volume + "' WHERE name = 'volume'";
		db.execSQL(updateQuery);
		return;	
    }
    public void setSettings_FontSize(int font_size)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_Settings + " SET 'value' = '" + font_size + "' WHERE name = 'font_size'";
		db.execSQL(updateQuery);
		return;	
    }
    public void setSettings_notification(boolean notification)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_Settings + " SET 'value' = '" + notification + "' WHERE name = 'notification'";
		db.execSQL(updateQuery);
		return;    	
    }
    public void setSettings_version(String version)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_Settings + " SET 'value' = '" + version + "' WHERE name = 'version'";
		db.execSQL(updateQuery);
		return; 
    }
    public void setSettings_reset_notification(boolean reset_notification)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_Settings + " SET 'value' = '" + reset_notification + "' WHERE name = 'reset_notification'";
		db.execSQL(updateQuery);
		return; 
    }
    public Settings getSettings()
    {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT name, value FROM " + TABLE_Settings, null);
		if(cursor == null || cursor.getCount() == 0)
			return new Settings();

		Settings settings = new Settings();
		cursor.moveToFirst();
		do
		{
			String type = cursor.getString(0);
			if(type.equals("volume"))
			{
				settings._volume = Integer.parseInt(cursor.getString(1));
				settings._volume = (float) ((settings._volume / 50.0) * 1.5);
			}else if(type.equals("font_size"))
			{
				settings._font_size = Integer.parseInt(cursor.getString(1));
			}else if(type.equals("notification"))
			{
				settings._notification = Boolean.parseBoolean(cursor.getString(1));
			}else if(type.equals("version"))
			{
				settings._version = cursor.getString(1);
			}else if(type.equals("reset_notification"))
			{
				settings._reset_notification = Boolean.parseBoolean(cursor.getString(1));
			}
		}while(cursor.moveToNext());

		cursor.close();
		return settings;    
    }
    //----------------------------------------------------------------
    public boolean isThereChallengeWords(int lesson_number)
    {
		String countQuery = "SELECT count(*) FROM " + TABLE_ChallengeWords + " WHERE lesson_number = " + lesson_number;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if(cursor == null)
			return false;
		
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		
		if(count > 0)
			return true;

		return false;
    }
    
    public List<ChallengeWord> createBaseChallengeWord(int lesson_number, Word word)
    {
    	if(word == null)
    		return null;
   		List<ChallengeWord> challengeWords = new ArrayList<ChallengeWord>();
    	List<Meaning> meanings = this.getMeaning(word.getId());
   		String mainWord = word.getWord().replace('\"',  ' ').trim().toLowerCase();
   		for(int index = 0; index < meanings.size(); index++)
   		{
   			Meaning meaning = meanings.get(index);
   			if(meaning.getSynonyms() == null)
   				continue;
   			String synonyms = mainWord + "," + meaning.getSynonyms().replace('\"',  ' ').trim().toLowerCase();
   			ChallengeWord challengeWord = new ChallengeWord(lesson_number, word.getId(), index + 1, synonyms, "");
	    	ContentValues initialValues = new ContentValues();
	    	initialValues.put("lesson_number", challengeWord.getLessonNumber());
	    	initialValues.put("word_number", challengeWord.getWordNumber());
	    	initialValues.put("meaning_id", challengeWord.getWordNumber());
	    	initialValues.put("synonyms", challengeWord.getSynonyms());
	    	initialValues.put("conflict_ids", "");
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	db.insert(TABLE_ChallengeWords, null, initialValues);
   			challengeWords.add(challengeWord);
   		}
   		return challengeWords;
    }
    
    public int getFirstIdChallengeLesson(int lesson_number)
    {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_ChallengeWords + " WHERE lesson_number = " + Integer.toString(lesson_number), null);
		if(cursor == null || cursor.getCount() == 0)
			return -1;

		int first_id = -2;
		if(cursor.moveToFirst())
		{
			first_id = cursor.getInt(0);	
		}

		cursor.close();
		return first_id;
    }
    
    public ChallengeWord getChallengeWord(int lesson_number, int id)
    {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT id, lesson_number, word_number, meaning_id, synonyms, conflict_ids FROM " + TABLE_ChallengeWords + " WHERE id = " + Integer.toString(id), null);
		if(cursor == null || cursor.getCount() == 0)
			return null;

		ChallengeWord challengeWord = null;
		if(cursor.moveToFirst())
		{
				challengeWord = new ChallengeWord(Integer.parseInt(cursor.getString(0)),
						Integer.parseInt(cursor.getString(1)),
						Integer.parseInt(cursor.getString(2)),
						Integer.parseInt(cursor.getString(3)),
						 cursor.getString(4),
						 cursor.getString(5));
		}

		cursor.close();
		return challengeWord;    	
    }
    
    public List<ChallengeWord> getChallengeWords(int lesson_number)
    {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT id, lesson_number, word_number, meaning_id, synonyms, conflict_ids FROM " + TABLE_ChallengeWords + " WHERE lesson_number = " + Integer.toString(lesson_number), null);
		if(cursor == null || cursor.getCount() == 0)
			return null;

		List<ChallengeWord> challengeWords = new ArrayList<ChallengeWord>();
		if(cursor.moveToFirst())
		{
			do {
				ChallengeWord challengeWord = new ChallengeWord(Integer.parseInt(cursor.getString(0)),
						Integer.parseInt(cursor.getString(1)),
						Integer.parseInt(cursor.getString(2)),
						Integer.parseInt(cursor.getString(3)),
						 cursor.getString(4),
						 cursor.getString(5));
				
				challengeWords.add(challengeWord);
			}while(cursor.moveToNext());
		}

		cursor.close();
		return challengeWords;
    }    
    
    public String generateConflicts(List<ChallengeWord> challengeWords, ChallengeWord challengeWord)
    {
    	String[] main_synonyms = challengeWord.getSynonyms().split(",");
    	String conflict_ids = "";
    	for(int index_ms = 0; index_ms < main_synonyms.length; index_ms++)
    	{
    		String main_synonym = main_synonyms[index_ms].toLowerCase().trim();
        	for(int index = 0; index < challengeWords.size(); index++)
        	{
        		String tmp_conflict_ids = conflict_ids + ",";
        		if(tmp_conflict_ids.contains("," + challengeWords.get(index).getId() + ","))
        			continue;
        		if(challengeWords.get(index).getSynonyms().toLowerCase().contains(main_synonym))
        		{
        			conflict_ids += "," + challengeWords.get(index).getId();  
        		}
        	}
    	}
    	return conflict_ids.replaceFirst(",", "");
    }
    
    public void updateConflictsChallengeWord(int id, String conflict_ids)
    {
		SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_ChallengeWords + " SET conflict_ids = '" + conflict_ids + "' WHERE id = " + id;
		db.execSQL(updateQuery);
		return;	
    }
   
    public int createChallengeWords(int lesson_number)
    {
    	if(!isThereChallengeWords(lesson_number))
    	{
	    	List<Word> words = this.getWordsOfLesson(lesson_number);
	    	if(words.size() < 1)
	    		return -1;
	    	
	    	for(int index = 0; index < words.size(); index++)
	    	{
//	    		List<ChallengeWord> challengeWords = this.createBaseChallengeWord(lesson_number, words.get(index));
	    		this.createBaseChallengeWord(lesson_number, words.get(index));
//	    		if(challengeWords == null || challengeWords.size() < 1)
//	    			return -2;
	    	}
	    	List<ChallengeWord> challengeWords = this.getChallengeWords(lesson_number);
	    	if(challengeWords == null || challengeWords.size() < 1)
	    		return -4;
	    	
	    	for(int reIndex = 0; reIndex < challengeWords.size(); reIndex++)
    		{
    			String conflict_ids = this.generateConflicts(challengeWords, challengeWords.get(reIndex));
    			this.updateConflictsChallengeWord(challengeWords.get(reIndex).getId(), conflict_ids);
    		}
	    	return 1;
    	}
    	return -3;
    }

    public int getCountChallengeWords(int lesson_number)
    {
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT count(*) FROM " + TABLE_ChallengeWords + " WHERE lesson_number = " + Integer.toString(lesson_number);
	    Cursor cursor = db.rawQuery(query, null);
		if(cursor == null)
			return -1;
		
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
    }    
    //----------------------------------------------------------------
    public int getChallengeScore(int lesson_number)
    {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT score FROM " + TABLE_ChallengeScores + " WHERE lesson_number = " + Integer.toString(lesson_number), null);
		if(cursor == null || cursor.getCount() == 0)
			return 0;
		int score = 0;
		if(cursor.moveToFirst())
		{
			score = cursor.getInt(0);
		}
		cursor.close();
		return score;
    }
    
    public boolean isThereChallengeScore(int lesson_number)
    {
		String countQuery = "SELECT count(*) FROM " + TABLE_ChallengeScores + " WHERE lesson_number = " + Integer.toString(lesson_number);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if(cursor == null)
			return false;
		
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		
		if(count > 0)
			return true;

		return false;
    }
    
    public void setChallengeScore(int lesson_number, int score)
    {
    	if(isThereChallengeScore(lesson_number))
    	{
    		SQLiteDatabase db = this.getWritableDatabase();
    		String updateQuery = "UPDATE " + TABLE_ChallengeScores + " SET score = " + Integer.toString(score) + " WHERE lesson_number = " + Integer.toString(lesson_number);
    		db.execSQL(updateQuery);
    		return;    		
    	}
    	else
    	{
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	ContentValues initialValues = new ContentValues();
	    	initialValues.put("lesson_number", lesson_number);
	    	initialValues.put("score", score);
	    	db.insert(TABLE_ChallengeScores, null, initialValues);	

    	}
    }
    
    public ChallengeRow createBaseChallengeRow(int lesson_number, Word word)
    {
    	
    	if(word == null)
    		return null;
   		List<Meaning> meanings = this.getMeaning(word.getId());
   		String questionable_words = word.getWord().replace('\"',  ' ').trim().toLowerCase();
   		for(int index = 0; index < meanings.size(); index++)
   		{
   			Meaning meaning = meanings.get(index);
   			questionable_words += "," + meaning.getSynonyms().replace('\"',  ' ').trim().toLowerCase();
   		}
   		// In the beginning, the remain_words is same as the questionable_words.
   		return new ChallengeRow(lesson_number, word.getId(), questionable_words, questionable_words, 0);
    }
    
    public boolean isThereChallengeRow(int lesson_number, int word_number)
    {
		String countQuery = "SELECT count(*) FROM " + TABLE_Challenge + " WHERE lesson_number = " + Integer.toString(lesson_number) + " AND word_number = " + Integer.toString(word_number);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if(cursor == null)
			return false;
		
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		
		if(count > 0)
			return true;

		return false;    	
    }
    
    public boolean isThereChallengeRows(int lesson_number)
    {
		String countQuery = "SELECT count(*) FROM " + TABLE_Challenge + " WHERE lesson_number = " + Integer.toString(lesson_number);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if(cursor == null)
			return false;
		
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		
		if(count > 0)
			return true;

		return false;
    }
    
    public int createChallengeRows(int lesson_number)
    {
    	if(!isThereChallengeRows(lesson_number))
    	{
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	List<Word> words = this.getWordsOfLesson(lesson_number);
	    	if(words.size() < 1)
	    		return -1;
	    	
	    	for(int index = 0; index < words.size(); index++)
	    	{
	    		ChallengeRow challengeRow = this.createBaseChallengeRow(lesson_number, words.get(index));
	    		if(challengeRow == null)
	    			return -2;
	    		
		    	ContentValues initialValues = new ContentValues();
		    	initialValues.put("lesson_number", challengeRow.getLessonNumber());
		    	initialValues.put("word_number", challengeRow.getWordNumber());
		    	initialValues.put("questionable_words", challengeRow.getQuestionableWords().trim().toLowerCase());
		    	initialValues.put("remain_words", challengeRow.getRemainWords().trim().toLowerCase());
		    	initialValues.put("score", challengeRow.getScore());
		    	db.insert(TABLE_Challenge, null, initialValues);	
	    	}
	    	return 1;
    	}
    	return -3;
    }
    
    public ChallengeRow getChallengeRow(int lesson_number, int word_number)
    {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT lesson_number, word_number, questionable_words, remain_words, score FROM " + TABLE_Challenge + " WHERE lesson_number = " + Integer.toString(lesson_number) + " AND word_number = " + Integer.toString(word_number), null);
		if(cursor == null || cursor.getCount() == 0)
			return null;
		ChallengeRow challengeRow = null;
		if(cursor.moveToFirst())
		{
			challengeRow = new ChallengeRow(Integer.parseInt(cursor.getString(0)),
					Integer.parseInt(cursor.getString(1)), 
					cursor.getString(2),
					cursor.getString(3),
					Integer.parseInt(cursor.getString(4)));
		}
		cursor.close();
		return challengeRow;
    }
    
    public List<ChallengeRow> getChallengeRows(int lesson_number)
    {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT lesson_number, word_number, questionable_words, remain_words, score FROM " + TABLE_Challenge + " WHERE lesson_number = " + Integer.toString(lesson_number), null);
		if(cursor == null || cursor.getCount() == 0)
			return null;
				
		List<ChallengeRow> challengeRow_list = new ArrayList<ChallengeRow>();
		if(cursor.moveToFirst())
		{
			do {
				ChallengeRow challengeRow = new ChallengeRow(Integer.parseInt(cursor.getString(0)),
						 Integer.parseInt(cursor.getString(1)), 
						 cursor.getString(2),
						 cursor.getString(3),
						 Integer.parseInt(cursor.getString(4)));
				
				challengeRow_list.add(challengeRow);
			}while(cursor.moveToNext());
		}
		cursor.close();
		
		return challengeRow_list;    	
    }
    
    public String updateRemainWords(String remainWords, String eliminatingWord)
    {
//    	String res_remainWords = remainWords.replaceAll(eliminatingWord + ",", "");
//    	res_remainWords = remainWords.replaceAll("," + eliminatingWord, "");
//    	res_remainWords = remainWords.replaceAll(eliminatingWord, "");
//    	return res_remainWords;
    	
    	String[] remain_words_list = remainWords.trim().split(",");
    	String res_remainWords = "";
    	for(int index = 0; index < remain_words_list.length; index++)
    	{
    		if(!remain_words_list[index].trim().toLowerCase().equals(eliminatingWord.trim().toLowerCase()))
    			res_remainWords += "," + remain_words_list[index].trim().toLowerCase();
    	}
    	return res_remainWords.replaceFirst(",", "").trim().toLowerCase();
    }
    
    public boolean deleteChallengeRow(int lesson_number, int word_number)
    {
    	if(isThereChallengeRow(lesson_number, word_number))
    	{
    		SQLiteDatabase db = this.getWritableDatabase();
    		String updateQuery = "DELETE FROM " + TABLE_Challenge + " WHERE lesson_number = " + Integer.toString(lesson_number) + " AND word_number = " + Integer.toString(word_number);
    		db.execSQL(updateQuery);
    		return true;
    	}
    	else
    	{
    		return false;
    	}    	
    }
    
    public boolean updateChallengeRow(int lesson_number, int word_number, String remainWords, int score)
    {
    	if(isThereChallengeRow(lesson_number, word_number))
    	{
    		SQLiteDatabase db = this.getWritableDatabase();
    		String updateQuery = "UPDATE " + TABLE_Challenge + " SET remain_words = '" + remainWords + "' , score = " + Integer.toString(score) + " WHERE lesson_number = " + Integer.toString(lesson_number) + " AND word_number = " + Integer.toString(word_number);
    		db.execSQL(updateQuery);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    //----------------------------------------------------------------
    public List<Word> getFavorites()
    {
    	List<Word> favorite_words = new ArrayList<Word>();
    	String selectQuery = "SELECT * FROM " + TABLE_Favorites;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst())
		{
			do {
				int word_id = Integer.parseInt(cursor.getString(0));
				Word word = this.getWord(word_id);
				favorite_words.add(word);
			}while(cursor.moveToNext());
		}
		cursor.close();
		
		
		return favorite_words;
    }
    
    public boolean isFavorite(int word_id)
    {
		String countQuery = "SELECT count(*) FROM " + TABLE_Favorites + " WHERE word_id = " + Integer.toString(word_id);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		
		if(count > 0)
			return true;

		return false;
    }
    
    public boolean addFavoriteWord(int word_id)
    {
    	if(!this.isFavorite(word_id))
    	{
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	ContentValues initialValues = new ContentValues();
	    	initialValues.put("word_id", word_id);
	    	db.insert(TABLE_Favorites, null, initialValues);
	    	return true;
    	}
    	return false;
    }
    
    public boolean removeFavoriteWord(int word_id)
    {
    	if(this.isFavorite(word_id))
    	{
    		SQLiteDatabase db = this.getWritableDatabase();
    		db.delete(TABLE_Favorites, "word_id = " + Integer.toString(word_id), null);
    		return true;
    	}
    	return false;
    }
    //----------------------------------------------------------------
    public List<Meaning> getMeaning(int word_id)
    {
		List<Meaning> meaningList = new ArrayList<Meaning>();
		String selectQuery = "SELECT * FROM " + TABLE_MEANINGS + " WHERE word_id = " + Integer.toString(word_id);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst())
		{
			do {
				Meaning meaning = new Meaning(Integer.parseInt(cursor.getString(0)),
						 cursor.getString(1),
						 cursor.getString(2));
				
				meaningList.add(meaning);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return meaningList;    	
    }
    
	public Word getWord(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery("SELECT " + KEY_ID + "," + KEY_WORD + "," + KEY_PHONETICS + "," + KEY_PARTOFSPEECH + "," + KEY_DESCRIPTION + "," + KEY_EXAMPLES + " FROM " + TABLE_WORDS + " WHERE " + KEY_ID + " = " + Integer.toString(id), null);
		if(cursor.getCount() == 0)
			return null;
		
	    if(cursor != null)
			cursor.moveToFirst();
		
		Word word = new Word(Integer.parseInt(cursor.getString(0)),
				 cursor.getString(1),
				 cursor.getString(2),
				 cursor.getString(3),
				 cursor.getString(4),
				 cursor.getString(5));
		
		return word;
	}
	
	public List<Word> getAllWords()
	{
		List<Word> wordList = new ArrayList<Word>();
		String selectQuery = "SELECT * FROM " + TABLE_WORDS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst())
		{
			do {
				Word word = new Word(Integer.parseInt(cursor.getString(0)),
						 cursor.getString(1),
						 cursor.getString(2),
						 cursor.getString(3),
						 cursor.getString(4),
						 cursor.getString(5));
				
				wordList.add(word);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return wordList;
	}
	
	public List<Word> getWordsOfLesson(int lesson_number)
	{
		List<Word> wordList = new ArrayList<Word>();
		int lower_id = (lesson_number - 1) * 10;
		int upper_id = lesson_number * 10 + 1;
		String selectQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE _id > " + Integer.toString(lower_id) + " AND _id < " + Integer.toString(upper_id);
//		String selectQuery = "SELECT * FROM main_words WHERE _id > 0 AND _id < 11";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst())
		{
			do {
				Word word = new Word(Integer.parseInt(cursor.getString(0)),
						 cursor.getString(1),
						 cursor.getString(2),
						 cursor.getString(3),
						 cursor.getString(4),
						 cursor.getString(5));
				
				wordList.add(word);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return wordList;
	}	
	
	public int getWordsCount(int lesson_number)
	{
		String countQuery = "SELECT count(*) FROM " + TABLE_WORDS + " WHERE _id > " + Integer.toString((lesson_number - 1) * 10) + " AND _id <= " + Integer.toString(lesson_number * 10);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}
	
	public int getWordsCount()
	{
		String countQuery = "SELECT count(*) FROM " + TABLE_WORDS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}
}