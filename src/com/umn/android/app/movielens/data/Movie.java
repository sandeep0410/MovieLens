package com.umn.android.app.movielens.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.umn.android.app.movielens.constants.Constants;

public class Movie implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mId;
	private String mDrawable;
	private String mName;
	private String mOverview;
	private String mTagline;
	private String mReleaseDate;
	private int mBudget;
	private int mRevenue;
	private int mVotecount;
	private int mRuntime;
	private double mAverageVote;
	private ArrayList<String> mGenres;
	private ArrayList<String> mLanguages;
	private int NUMBER_UPVOTES = 0;
	private int NUMBER_DOWNVOTES = 0;

	private int mVotedvalue = Constants.Votes.NOTVOTED;
	private boolean isVoted=false;	

	public Movie(int id, String name, String drawable, String overview, String tagline, String releasedate, int budget,
			int revenue, int votecount, int runtime,double averagevote, 
			ArrayList<String> genres, ArrayList<String> languages){
		mId = id;
		mDrawable = "movies/" +drawable;
		mName = name;
		mOverview = overview;
		mTagline = tagline;
		mReleaseDate = releasedate;
		mBudget = budget;
		mRevenue = revenue;
		mVotecount = votecount;
		mRuntime = runtime;
		mAverageVote = averagevote;
		mGenres = genres;
		mLanguages = languages;
	}

	public Movie(String moviename, String drawable ){
		this.mName = moviename;
		this.mId = moviename.hashCode();
		this.mDrawable = drawable;
	}

	public Movie(Movie m){
		mId = m.mId;
		mDrawable = m.mDrawable;
		mName = m.mName;
		mOverview = m.mOverview;
		mTagline = m.mTagline;
		mReleaseDate = m.mReleaseDate;
		mBudget = m.mBudget;
		mRevenue = m.mRevenue;
		mVotecount = m.mVotecount;
		mRuntime = m.mRuntime;
		mAverageVote = m.mAverageVote;
		mGenres = m.mGenres;
		mLanguages = m.mLanguages;
	}

	public int getmovieId(){
		return mId;
	}
	public String getMovieName(){
		return mName;
	}
	public String getDrawable(){
		return mDrawable;
	}
	public ArrayList<String> getGenre(){
		return mGenres;
	}
	public double getAverageVote(){
		return mAverageVote;
	}
	public String getReleasedate(){
		return mReleaseDate;
	}

	public int getRevenue(){
		return mRevenue;
	}

	public int getruntime(){
		return mRuntime;
	}
	public int getBudget(){
		return mBudget;
	}
	public ArrayList<String> getSpokenLanguages(){
		return mLanguages;
	}
	public String getTagline(){
		return mTagline;
	}
	public int getVotedValue(){
		return mVotedvalue;
	}
	public String getOverView(){
		return mOverview;
	}
	public int getVoteCount(){
		return mVotecount;
	}

	public boolean isVoted(){
		return isVoted;
	}

	public void setvotedValue(int votedvalue){
		mVotedvalue = votedvalue;
		if(votedvalue == Constants.Votes.UPVOTED)
			changeUpvoteValue(true);
		if(votedvalue == Constants.Votes.DOWNVOTED)
			changeDownvoteValue(true);
	}

	public void setVoted(boolean vote){
		isVoted = vote;
	}

	public void changeUpvoteValue(boolean increase){
		if(increase){
			NUMBER_UPVOTES++;
		}else 
			NUMBER_UPVOTES--;
	}
	public int getUpVoteCount(){
		return NUMBER_UPVOTES;
	}

	public void changeDownvoteValue(boolean increase){
		if(increase){
			NUMBER_DOWNVOTES++;
		}else 
			NUMBER_DOWNVOTES--;
	}
	public int getDownVoteCount(){
		return NUMBER_DOWNVOTES;
	}
	public void resetValues(){
		NUMBER_DOWNVOTES =0;
		NUMBER_UPVOTES = 0;
		setVoted(false);
		setvotedValue(Constants.Votes.NOTVOTED);
	}
}
