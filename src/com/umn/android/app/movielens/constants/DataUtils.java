package com.umn.android.app.movielens.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.umn.android.app.movielens.data.Movie;
import com.umn.android.app.movielens.data.MovieGroup;
import com.umn.android.app.movielens.data.User;

public class DataUtils {
	public static final String[] contacts = {
		"Amy",
		"Bob",
		"Daniel",
		"Evelyn",
		"Frank",
		"Hannah",
		"Joe",
		"John",
		"Loren",
		"Mark",
		"Mary",
		"Mike",
		"Paul",
		"Ricky",
	"Walter"};


	/*private static final String[] fixedMovieList={
		"InterStellar",
		"Gone Girl"
	};*/

	public static ArrayList<Movie> movieList= new ArrayList<Movie>();
	public static ArrayList<User> userList = new ArrayList<User>();
	public static ArrayList<Movie> votedmovieList = new ArrayList<Movie>();

	public static void createMovieList(Context context){
		if(movieList.size()>0)
			return;

		try {
			JSONArray a = new JSONArray(loadJSONFromAsset(context, "movies/movies.json"));
			JSONObject movie;
			int budget;
			String image;
			String overview;
			int pk;
			String release_date;
			int revenue;
			int runtime;
			String tagline;
			String title;
			double vote_average;
			int vote_count;
			String[] genres;
			String[] spoken_languages;
			for(int i=0; i<a.length();i++)
			{
				movie = (JSONObject) a.get(i);

				budget = (Integer)movie.get("budget");

				image = (String) movie.get("image");	

				overview = (String)movie.get("overview");

				pk = (Integer)movie.get("pk");

				release_date = (String)movie.get("release_date");				
				revenue = (Integer)movie.get("revenue");				
				runtime = (Integer)movie.get("runtime");				
				tagline = (String) movie.get("tagline");				
				title = (String)movie.get("title");				
				vote_average = (Double)movie.get("vote_average");				
				vote_count = (Integer)movie.get("vote_count");
				JSONArray temp = (JSONArray)movie.get("genres");
				int length = temp.length();
				genres = null;
				if (length > 0) {
					genres = new String [length];
					for (int j = 0; j < length; j++) {
						genres[j] = (String) temp.get(j);
					}
				}
				temp = (JSONArray)movie.get("spoken_languages");
				length = temp.length();
				spoken_languages = null;
				if (length > 0) {
					spoken_languages = new String [length];
					for (int j = 0; j < length; j++) {
						spoken_languages[j] = (String) temp.get(j);
					}
				}
				movieList.add(new Movie(pk, title, image, overview, tagline, release_date, budget, revenue, vote_count,
						runtime, vote_average, new ArrayList<String>(Arrays.asList(genres)), new ArrayList<String>(Arrays.asList(spoken_languages))));
			}
			Collections.sort(movieList, new Comparator<Movie>() {

				@Override
				public int compare(Movie lhs, Movie rhs) {
					return lhs.getMovieName().compareTo(rhs.getMovieName());
				}


			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createUserList(Context context){

		if(userList.size()>0)
			return;

		try {			
			JSONArray a =  new JSONArray(loadJSONFromAsset(context, "users/users.json"));
			JSONObject user;
			String name;

			String image;
			boolean ml;
			int pk;
			for(int i=0; i<a.length();i++)
			{

				user = (JSONObject)a.get(i);
				name = (String) user.get("first") +" " +(String) user.get("last");
				image = (String) user.get("image");
				ml = (Boolean) user.get("ml");
				pk = (Integer)user.get("pk");
				userList.add(new User(name, image, ml, pk));
			}
			Collections.sort(userList, new Comparator<User>() {

				@Override
				public int compare(User lhs, User rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});

		} catch (JSONException e){
			e.printStackTrace();
		}
	}


	/*	private static final int[] movieicons={
		R.drawable.interstellar,
		R.drawable.gone_girl
	};*/

	private static String loadJSONFromAsset(Context context, String filename) {
		String json = null;
		try {

			InputStream is = context.getAssets().open(filename);

			int size = is.available();

			byte[] buffer = new byte[size];

			is.read(buffer);

			is.close();

			json = new String(buffer, "UTF-8");


		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

	public static Drawable getDrawablefromAssets(String filename, Context context){
		Drawable d = null;
		InputStream ims;
		try {

			ims = context.getAssets().open(filename);
			d = Drawable.createFromStream(ims, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return d;
	}

	public static void createVotedMovielist(Context context){
		Random rand = new Random();
		votedmovieList = getRandomRecomList();


		for(int i=0; i<votedmovieList.size();i=i+2){
			int randomNum = rand.nextInt((7 - 1) + 1) + 1;
			for(int j=0;j<=randomNum;j++){
				votedmovieList.get(i).changeUpvoteValue(true);
				votedmovieList.get(i).setVoted(true);
				votedmovieList.get(i).setvotedValue(Constants.Votes.UPVOTED);
			}
			randomNum = rand.nextInt((7 - 1) + 1) + 1;
			for(int j=0;j<=randomNum;j++){
				votedmovieList.get(i).changeDownvoteValue(true);
				votedmovieList.get(i).setVoted(true);
				votedmovieList.get(i).setvotedValue(Constants.Votes.DOWNVOTED);
			}
		}
		for(int i=1; i<votedmovieList.size();i=i+2){
			int randomNum = rand.nextInt((7 - 1) + 1) + 1;
			for(int j=0;j<=randomNum;j++){
				votedmovieList.get(i).changeDownvoteValue(true);
				votedmovieList.get(i).setVoted(true);
				votedmovieList.get(i).setvotedValue(Constants.Votes.DOWNVOTED);
			}
			randomNum = rand.nextInt((7 - 1) + 1) + 1;
			for(int j=0;j<=randomNum;j++){
				votedmovieList.get(i).changeUpvoteValue(true);
				votedmovieList.get(i).setVoted(true);
				votedmovieList.get(i).setvotedValue(Constants.Votes.UPVOTED);
			}
		}

	}

	public static ArrayList<Movie> getRandomRecomList(){
		ArrayList<Movie> randomArrayList = new ArrayList<Movie>();
		Random rand = new Random();
		Log.d("sandeep","moviesize" +movieList.size());
		int randomNum = rand.nextInt((20 - 0) + 1) + 0;
		for(int i=randomNum; i<200; i=i+20){
			Log.d("sandeep","i: " +i);
			randomArrayList.add(new Movie(movieList.get(i)));
		}

		return randomArrayList;
	}

	public static void createVotedMovielist(
			Context context, MovieGroup moviegroup) {
		votedmovieList.clear();
		ArrayList<Movie> groupMovieList = moviegroup.getMovieList();
		int maxVotes = moviegroup.getGroupMembers().size()+1;
		for(Movie m: groupMovieList){
			votedmovieList.add(new Movie(m));
		}
		Random rand = new Random();
		int upVotes;
		int downvotes;
		for(Movie m: votedmovieList){
			upVotes = rand.nextInt(maxVotes+1);
			Log.d("sandeep","" +maxVotes);
			Log.d("sandeep"," " +upVotes);
			downvotes = maxVotes -upVotes;
			Log.d("sandeep"," " +downvotes);
			for(int i=0; i<upVotes; i++){
				m.setVoted(true);
				m.setvotedValue(Constants.Votes.UPVOTED);
			}
			for(int j=0; j<downvotes; j++){
				m.setVoted(true);
				m.setvotedValue(Constants.Votes.DOWNVOTED);
			}

		}

	}
}
