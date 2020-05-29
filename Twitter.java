package finalproject;

import java.util.ArrayList;
import java.util.LinkedList;

public class Twitter {
	// FIELDS
	
	private MyHashTable<String, ArrayList<Tweet>> tweetTableDate;// Key = date, value = tweet
	private MyHashTable<String, Tweet> tweetTableAuthor; // Key = author, value = tweet
	private MyHashTable<String,Integer> stopWordsTable; // Key = stopword, value = 1;
	private ArrayList<Tweet> tweets;
	
	
	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
		this.tweetTableDate = new MyHashTable<String,ArrayList<Tweet>>(tweets.size());
		this.tweetTableAuthor = new MyHashTable<String,Tweet>(tweets.size());
		this.stopWordsTable = new MyHashTable<String, Integer>(stopWords.size());
		for (String s: stopWords) { this.stopWordsTable.put(s, 1);}
		this.tweets = new ArrayList<Tweet>();
		for (Tweet t : tweets) {addTweet(t);}
		
		
	}
	
    /**
     * Add Tweet t to this Twitter
     * O(1)
     */
	public void addTweet(Tweet t) { 
		this.tweets.add(t);
		
		// BY AUTHOR
		Tweet retrievedTweet = this.tweetTableAuthor.get(t.getAuthor());
		if (retrievedTweet == null) {this.tweetTableAuthor.put(t.getAuthor(), t);}
		else {
			if (retrievedTweet.compareTo(t) < 0) {
				this.tweetTableAuthor.remove(t.getAuthor());
				this.tweetTableAuthor.put(t.getAuthor(), t);
				}
		}
		String date = t.getDateAndTime().substring(0,10);
		ArrayList<Tweet> arr = this.tweetTableDate.get(date);
		if (arr == null) { 
			ArrayList<Tweet> newArr = new ArrayList<Tweet>();
			newArr.add(t);
			this.tweetTableDate.put(date, newArr);
			}
		else {arr.add(t);}
	}
    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {
    	return this.tweetTableAuthor.get(author);
    }
    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
   
    public ArrayList<Tweet> tweetsByDate(String date) {
        return this.tweetTableDate.get(date);
    }
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
    public ArrayList<String> trendingTopics() {
    	MyHashTable<String, Integer> wordsTable = new MyHashTable<String,Integer>(this.tweets.size());
    	
    	for (Tweet t : this.tweets) {
    		ArrayList<String> message = getWords(t.getMessage());
    		ArrayList<String> uniqueWords = new ArrayList<String>();
    		for (String w : message) {
    			if (! uniqueWords.contains(w) && this.stopWordsTable.get(w) == null){
    				uniqueWords.add(w);
        			int occurences;
        			if (wordsTable.get(w) == null) {occurences = 1;}
        			else {occurences = wordsTable.remove(w);}
        			wordsTable.put(w, occurences + 1);
        			}}}
    		
    	return MyHashTable.fastSort(wordsTable);
    }
    
    /**
     * An helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    private static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }

    

}
