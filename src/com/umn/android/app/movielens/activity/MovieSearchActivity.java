package com.umn.android.app.movielens.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.fragments.MovieSearchFragment;

public class MovieSearchActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_search_screen);
		DataUtils.createMovieList(getApplicationContext());
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		MovieSearchFragment fragment = new MovieSearchFragment();
		fragmentTransaction.add(R.id.movie_info_fragment, fragment);
		fragmentTransaction.commit();
	}

	public void changefragmenttomovieInfo(Fragment fg){
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.movie_info_fragment, fg);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
}
