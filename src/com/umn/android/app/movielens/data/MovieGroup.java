package com.umn.android.app.movielens.data;

import java.io.Serializable;
import java.util.ArrayList;

public class MovieGroup implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<User> mFriends;
	private String mName;
	private int mId;
	private boolean isFavourite = false;
	private ArrayList<Movie> movieList = new ArrayList<Movie>();

	public MovieGroup(ArrayList<User> friends, String name){
		this.mName = name;
		this.mId = name.hashCode();
		this.mFriends = friends;
	}

	public void addFriends(User friend){
		if(mFriends == null)
			mFriends = new ArrayList<User>();
		mFriends.add(friend);		
	}

	public String getName(){
		return mName;
	}

	public boolean isFavourite(){
		return isFavourite;
	}

	public int getId(){
		return mId;
	}

	public void setFavorite(boolean favorite){
		isFavourite = favorite;
	}
	public ArrayList<User> getGroupMembers(){
		return mFriends;
	}

	public void setFriendList(ArrayList<User> mfriendList) {
		if(mFriends == null)
			mFriends = new ArrayList<User>(mfriendList);
		else{
			mFriends.clear();
			mFriends.addAll(mfriendList);
		}

	}

	public void setMovieList(ArrayList<Movie> movies){
		
		for(Movie m : movies){
			if(!movieList.contains(m))
				movieList.add(m);
		}
	}
	public void addMovieList(ArrayList<Movie> movies){
		for(Movie m : movies){
			if(!movieList.contains(m))
				movieList.add(m);
		}
	}
	public ArrayList<Movie> getMovieList(){
		return movieList;
	}
}
