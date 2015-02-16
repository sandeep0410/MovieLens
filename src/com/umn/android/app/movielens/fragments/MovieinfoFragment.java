package com.umn.android.app.movielens.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.data.Movie;

public class MovieinfoFragment extends Fragment{
	Movie mMovie;
	ImageView mImage;
	TextView mOverview;
	TextView mBudget;
	TextView mRuntime;
	TextView mReleasedate;
	TextView mLanguage;
	TextView mGenres;
	TextView mRevenue;
	TextView mVoteCount;
	TextView mAverageVote;
	TextView mTagline;

	public MovieinfoFragment(Movie movie) {
		mMovie = movie;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView = inflater.inflate(R.layout.movie_info, container, false);
		mImage = (ImageView)rootView.findViewById(R.id.movie_image);
		mOverview = (TextView)rootView.findViewById(R.id.movie_overview);
		mBudget = (TextView)rootView.findViewById(R.id.budget);
		mRuntime = (TextView)rootView.findViewById(R.id.runtime);
		mReleasedate = (TextView)rootView.findViewById(R.id.release_date);
		mLanguage = (TextView)rootView.findViewById(R.id.language);
		mGenres = (TextView)rootView.findViewById(R.id.genre);
		mRevenue = (TextView)rootView.findViewById(R.id.revenue);
		mVoteCount = (TextView)rootView.findViewById(R.id.vote_count);
		mAverageVote = (TextView)rootView.findViewById(R.id.averagevote);
		mTagline = (TextView)rootView.findViewById(R.id.tag_line);
		getActivity().getActionBar().setTitle(mMovie.getMovieName());
		setHasOptionsMenu(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		updateValues();

		return rootView;
	}


	private void updateValues() {
		if(mMovie!= null){
			mImage.setImageDrawable(DataUtils.getDrawablefromAssets(mMovie.getDrawable(), getActivity()));
			mOverview.setText(mMovie.getOverView());
			mBudget.setText("" +mMovie.getBudget());
			mRuntime.setText("" +mMovie.getruntime());
			mReleasedate.setText(mMovie.getReleasedate());
			mRevenue.setText("" +mMovie.getRevenue());
			mVoteCount.setText("" +mMovie.getVoteCount());
			mAverageVote.setText("" +mMovie.getAverageVote());
			mTagline.setText(mMovie.getTagline());
			mLanguage.setText(mMovie.getSpokenLanguages().toString());
			mGenres.setText(mMovie.getGenre().toString());
		}



	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:	       
			getActivity().getFragmentManager().popBackStack();
		}
		return true;
	}
}
