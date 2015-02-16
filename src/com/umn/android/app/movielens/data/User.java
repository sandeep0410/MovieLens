package com.umn.android.app.movielens.data;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mName;
	private long mId;
	private boolean mIsMovieLensUser;
	private String  mDrawable;

	public User(String name, String drawable, boolean movielLensAccount, long id){
		mName = name;
		mId =id;
		mDrawable = "users/"+drawable;
		mIsMovieLensUser = movielLensAccount;
	}

	public String getName(){
		return mName;
	}

	public String getDrawable(){
		return mDrawable;
	}
	public long getID(){
		return mId;

	}

	public boolean isMovieLensUser(){
		return mIsMovieLensUser;
	}

	@Override
	public int hashCode() {
		return mName.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return (o.hashCode() == hashCode());
	}
}
