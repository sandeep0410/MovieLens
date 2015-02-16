package com.umn.android.app.movielens.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.data.GroupData;
import com.umn.android.app.movielens.data.Movie;
import com.umn.android.app.movielens.data.MovieGroup;
import com.umn.android.app.movielens.fragments.GetRecommendationFragment;
import com.umn.android.app.movielens.fragments.GroupVotingFragment;
import com.umn.android.app.movielens.fragments.MovieinfoFragment;

public class GroupDetailsActivity extends Activity{

	private static GroupDetailsActivity _instance;
	private MovieGroup mg;
	private int mode=0;	
	public static final int MODE_ML = 0;
	public static final int MODE_SELF =1;
	public static final int MODE_ML_ADD = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_details_screen);
		_instance = this;
		String groupname = getIntent().getStringExtra("group");
		mg = GroupData.getGroup(groupname);
		if(mg!=null)
			getActionBar().setTitle(mg.getName());

		if(mg!=null && mg.getMovieList()!=null && mg.getMovieList().size()>0){
			setMode(MODE_SELF);
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			GroupVotingFragment fragment = new GroupVotingFragment();
			fragmentTransaction.add(R.id.group_detail_fragment, fragment);
			fragmentTransaction.commit();
		}else{

			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			GetRecommendationFragment fragment = new GetRecommendationFragment(false);
			fragmentTransaction.add(R.id.group_detail_fragment, fragment);
			fragmentTransaction.commit();
		}
	}
	public void changefragment(Fragment fg){
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.group_detail_fragment, fg);
		if(fg instanceof MovieinfoFragment || fg instanceof GetRecommendationFragment)
			fragmentTransaction.addToBackStack(null);


		fragmentTransaction.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 2000 &&resultCode==RESULT_OK){
			ArrayList<? extends Parcelable> tempArrayList = data.getParcelableArrayListExtra("selectedMovies");
			ArrayList<Movie> selectedMovieListbyUser = new ArrayList<Movie>();
			for(int i = 0; i<tempArrayList.size();i++){
				selectedMovieListbyUser.add((Movie) tempArrayList.get(i));
			}
			Collections.sort(selectedMovieListbyUser, new Comparator<Movie>() {

				@Override
				public int compare(Movie lhs, Movie rhs) {
					return lhs.getMovieName().compareTo(rhs.getMovieName());
				}
			});
			getGroup().setMovieList(selectedMovieListbyUser);
			GroupVotingFragment fg = new GroupVotingFragment();
			changefragment(fg);
		}else if(requestCode==2001 && resultCode==RESULT_OK){

			ArrayList<? extends Parcelable> tempArrayList = data.getParcelableArrayListExtra("selectedMovies");
			ArrayList<Movie> selectedMovieListbyUser = new ArrayList<Movie>();
			for(int i = 0; i<tempArrayList.size();i++){
				selectedMovieListbyUser.add((Movie) tempArrayList.get(i));
			}
			Collections.sort(selectedMovieListbyUser, new Comparator<Movie>() {

				@Override
				public int compare(Movie lhs, Movie rhs) {
					return lhs.getMovieName().compareTo(rhs.getMovieName());
				}
			});
			getGroup().addMovieList(selectedMovieListbyUser);
			GroupVotingFragment fg = new GroupVotingFragment();
			changefragment(fg);

		}
	}

	public MovieGroup getGroup(){
		return mg;
	}

	public void setMode(int value){
		mode = value;
	}

	public int getMode(){
		return mode;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_instance = null;
	} 
	public static GroupDetailsActivity getInstance(){
		return _instance;
	}

}
