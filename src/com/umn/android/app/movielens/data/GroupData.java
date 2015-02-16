package com.umn.android.app.movielens.data;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GroupData {
	public static ArrayList<Integer> mGroupIds = new ArrayList<Integer>();
	public static ArrayList<MovieGroup> mMoviegroupList = new ArrayList<MovieGroup>();

	public static void createDummyGroup(){/*
		ArrayList<User> onefriends = new ArrayList<User>();
		onefriends.add(new User("Mike", R.drawable.contact_icon, false));
		onefriends.add(new User("John", R.drawable.contact_icon, false));
		MovieGroup firstGroup = new MovieGroup(onefriends, "Buddies");
		onefriends.add(new User("Amy", R.drawable.contact_icon, false));
		MovieGroup secondGroup = new MovieGroup(onefriends, "Movie Buddies");

		mGroupIds.add(firstGroup.getId());
		mGroupIds.add(secondGroup.getId());
		mMoviegroupList.add(firstGroup);
		mMoviegroupList.add(secondGroup);
	 */}

	public static boolean createGroup(ArrayList<User> memberList, String name){
		if(mGroupIds.contains(name.hashCode())){
			return false;
		}else{
			MovieGroup mg = new MovieGroup(memberList, name);
			mMoviegroupList.add(mg);
			mGroupIds.add(name.hashCode());
			return true;
		}

	}

	public static void saveGroupsToSharedPref(Context context){
		SharedPreferences appSharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEditor = appSharedPrefs.edit();
		Gson gson = new Gson();
		String jsonMoviesGroup = gson.toJson(mMoviegroupList);
		String jsonMovieGrpIds = gson.toJson(mGroupIds);
		prefsEditor.putString("MovieGroups", jsonMoviesGroup);
		prefsEditor.putString("groupIds", jsonMovieGrpIds);
		prefsEditor.commit();
	}

	public static void retrieveSavedgroups(Context context){
		SharedPreferences appSharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Gson gson = new Gson();
		String moviegroupList = appSharedPrefs.getString("MovieGroups", "none");	
		String moviegroupIds = appSharedPrefs.getString("groupIds", "none");
		if(moviegroupIds.equals("none") || moviegroupList.equals("none"))
			return;
		Type type = new TypeToken<ArrayList<MovieGroup>>(){}.getType();
		Type type2 = new TypeToken<ArrayList<Integer>>(){}.getType();
		mMoviegroupList.clear();
		mGroupIds.clear();
		mMoviegroupList = gson.fromJson(moviegroupList, type);
		mGroupIds = gson.fromJson(moviegroupIds, type2);
	}

	public static void editGroup(ArrayList<User> mfriendList, String name) {
		int index = mGroupIds.indexOf(name.hashCode());
		mMoviegroupList.get(index).setFriendList(mfriendList);
	}

	public static MovieGroup getGroup(String name){
		for(MovieGroup mg: mMoviegroupList){
			if(mg.getName().equals(name))
				return mg;
		}

		return null;

	}
}
