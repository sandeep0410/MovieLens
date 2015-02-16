package com.umn.android.app.movielens.fragments;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.activity.GroupDetailsActivity;
import com.umn.android.app.movielens.activity.MovieSearchActivity;
import com.umn.android.app.movielens.activity.MyVotesActivity;
import com.umn.android.app.movielens.activity.VotingResultsActivity;
import com.umn.android.app.movielens.constants.Constants;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.data.Movie;

public class GroupVotingFragment extends Fragment {
	private ArrayList<Movie> movieList;
	private ArrayList<Movie> votedMovieList = new ArrayList<Movie>();
	private Button mYourVotes;
	private Button mVotingResults;
	private ListView mListView;
	private GroupDetailsActivity mContext;
	private MovieListAdapter mMovielistAdapter;
	private RelativeLayout parent;
	private View dummyAnimationView;
	private TextView statusText;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.group_voting_fragment, container, false);
		mContext = (GroupDetailsActivity) getActivity();
		mListView = (ListView)rootView.findViewById(R.id.movie_selection_list);
		statusText = (TextView)rootView.findViewById(R.id.status_txt);
		parent = (RelativeLayout)rootView.findViewById(R.id.rel_layout_voting_fragment);

		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dummyAnimationView = vi.inflate(R.layout.voted_movie_item, null);
		parent.addView(dummyAnimationView);
		dummyAnimationView.setVisibility(View.GONE);
		mMovielistAdapter = new MovieListAdapter(); 
		mYourVotes = (Button)rootView.findViewById(R.id.my_votes_button);
		mVotingResults = (Button)rootView.findViewById(R.id.voting_results_button);
		mYourVotes.setOnClickListener(mButtonClickListenener);
		mVotingResults.setOnClickListener(mButtonClickListenener);
		if(mContext.getMode() == GroupDetailsActivity.MODE_SELF)
			movieList = ((GroupDetailsActivity)mContext).getGroup().getMovieList();
		else if(mContext.getMode() == GroupDetailsActivity.MODE_ML_ADD){
			movieList = ((GroupDetailsActivity)mContext).getGroup().getMovieList();
			if(movieList!=null){
				ArrayList<Movie> trmpList = DataUtils.getRandomRecomList();
				for(Movie m:trmpList){
					if(!movieList.contains(m))
						movieList.add(m);
				}
			}
			mContext.setMode(GroupDetailsActivity.MODE_SELF);
		}
		else if(movieList == null){
			movieList = DataUtils.getRandomRecomList();

		}
		((GroupDetailsActivity)mContext).getGroup().addMovieList(movieList);
		mListView.setAdapter(mMovielistAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
		mContext.getActionBar().setTitle(((GroupDetailsActivity)mContext).getGroup().getName());
		mContext.getActionBar().setDisplayHomeAsUpEnabled(false);

		LinearLayout ll = new LinearLayout(mContext);
		ViewGroup.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		Button ml_button = new Button(mContext);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, 1.0f);
		int padding = getResources().getDimensionPixelSize(R.dimen.padding_voting_results_text);
		ml_button.setPadding(0, padding, 0, padding);			
		ml_button.setText("Get More Suggestions");
		ml_button.setGravity(Gravity.CENTER);
		ml_button.setTextSize(17);
		ml_button.setLayoutParams(param);
		ml_button.setOnClickListener(addMoviesButtonListener);
		ml_button.setTag("ML_Button");

		Button own_movie_button = new Button(mContext);		
		own_movie_button.setPadding(0, padding,0, padding);
		own_movie_button.setText("Add Your Own Movies");
		own_movie_button.setGravity(Gravity.CENTER);
		own_movie_button.setTextSize(17);
		own_movie_button.setLayoutParams(param);
		own_movie_button.setOnClickListener(addMoviesButtonListener);
		own_movie_button.setTag("Own_Movie_Button");
		ll.setLayoutParams(lp);
		ll.addView(ml_button);
		ll.addView(own_movie_button);
		mListView.addFooterView(ll);


		return rootView;
	}

	private OnClickListener addMoviesButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String tag = (String) v.getTag();
			if(tag.equals("ML_Button")){
				mContext.setMode(GroupDetailsActivity.MODE_ML_ADD);
				movieList = ((GroupDetailsActivity)mContext).getGroup().getMovieList();
				if(movieList!=null){
					ArrayList<Movie> trmpList = DataUtils.getRandomRecomList();
					for(Movie m:trmpList){
						if(!movieList.contains(m))
							movieList.add(m);
					}
				}
				mMovielistAdapter.notifyDataSetChanged();
				mListView.smoothScrollToPosition(mMovielistAdapter.getCount()-1);
			}else if(tag.equals("Own_Movie_Button")){
				mContext.setMode(GroupDetailsActivity.MODE_SELF);
				Intent i = new Intent(mContext, MovieSearchActivity.class);
				mContext.startActivityForResult(i, 2001);
			}
		}
	};

	private class MovieListAdapter extends BaseAdapter{
		LayoutInflater mInflater;
		private OnClickListener voteClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isSelected =v.isSelected();
				Movie movie = (Movie)v.getTag();
				ImageView otherImage;
				View itemView = (View) v.getTag(R.layout.voted_movie_item);

				switch(v.getId()){
				case R.id.voted_up_image:
					otherImage =  (ImageView) v.getTag(R.id.voted_down_image);
					if(isSelected){						
						v.setSelected(false);
						movie.setVoted(false);
						movie.setvotedValue(Constants.Votes.NOTVOTED);
						movie.changeUpvoteValue(false);
					}else if(!isSelected){						
						v.setSelected(true);
						otherImage.setSelected(false);
						movie.setVoted(true);
						movie.setvotedValue(Constants.Votes.UPVOTED);
					}
					votedMovieList.add(movie);
					break;
				case R.id.voted_down_image:
					otherImage =  (ImageView) v.getTag(R.id.voted_up_image);
					if(isSelected){
						v.setSelected(false);
						movie.setVoted(false);
						movie.setvotedValue(Constants.Votes.NOTVOTED);
						movie.changeDownvoteValue(false);
					}else if(!isSelected){
						v.setSelected(true);
						otherImage.setSelected(false);
						movie.setVoted(true);
						movie.setvotedValue(Constants.Votes.DOWNVOTED);
					}
					votedMovieList.add(movie);
					break;
				default: 
					break;
				}
				createDummyView(movie);
				int[] loc = {0,0};

				itemView.getLocationOnScreen(loc);
				int x = itemView.getLeft();
				int y = itemView.getBottom();
				Log.d("sandeep","valueof x: " +x +"valueof y : " +y +"delta y: " +(getResources().getDimensionPixelSize(R.dimen.device_height)-y));
				dummyAnimationView.setTranslationX(x);
				dummyAnimationView.setTranslationY(y);

				dummyAnimationView.setVisibility(View.VISIBLE);
				AnimationSet set = new AnimationSet(true);

				AlphaAnimation alph = new AlphaAnimation(1.0f, 0.0f);
				alph.setDuration(1700);
				TranslateAnimation trans= new TranslateAnimation( Animation.ABSOLUTE, 0,  Animation.ABSOLUTE, -(getResources().getDimensionPixelSize(R.dimen.device_width)-x)/4,
						Animation.ABSOLUTE, 0,  Animation.ABSOLUTE, getResources().getDimensionPixelSize(R.dimen.device_height)-y);
				trans.setDuration(1700);
				trans.setInterpolator(new AccelerateDecelerateInterpolator());
				ScaleAnimation scale = new ScaleAnimation(
				         1f, 0.0f, 
				         1f, 0.5f,
				         Animation.RELATIVE_TO_PARENT, 0.5f,
				         Animation.RELATIVE_TO_PARENT, 0);
				scale.setDuration(1700);
				set.setFillAfter(true);
				set.setFillEnabled(true);
				set.addAnimation(scale);
				set.addAnimation(alph);
				set.addAnimation(trans);
				set.setDuration(1700);
				set.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						dummyAnimationView.setVisibility(View.GONE);

					}
				});
				dummyAnimationView.startAnimation(set);

				notifyDataSetChanged();
				updateStatusText();
				Log.d("sandeep","valueof x: " +x +"valueof y : " +y);



			}
		};

		public MovieListAdapter() {
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		}

		protected void createDummyView(Movie movie) {

			ImageView iv = (ImageView)dummyAnimationView.findViewById(R.id.voted_movie_image_icon);
			TextView tv = (TextView) dummyAnimationView.findViewById(R.id.voted_movie_name); 
			ImageView im_up = (ImageView)dummyAnimationView.findViewById(R.id.voted_up_image);
			ImageView im_down = (ImageView)dummyAnimationView.findViewById(R.id.voted_down_image);	
			iv.setImageDrawable(DataUtils.getDrawablefromAssets(movie.getDrawable(),mContext));
			tv.setText(movie.getMovieName());
			im_up.setSelected(movie.isVoted() && movie.getVotedValue() == Constants.Votes.UPVOTED);
			im_down.setSelected(movie.isVoted() && movie.getVotedValue() == Constants.Votes.DOWNVOTED);
			return ;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return movieList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			int i=1;
			if(movieList.get(position).isVoted())
				i=0 ;

			return i;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {


			if(getItemViewType(position)==1){
				if(convertView == null )
					convertView = mInflater.inflate(R.layout.voted_movie_item, parent, false);
			}else if(getItemViewType(position)==0){
				if(convertView == null )
					convertView = mInflater.inflate(R.layout.nulll_item, parent, false);
				return convertView;
			}

			ImageView iv = (ImageView)convertView.findViewById(R.id.voted_movie_image_icon);
			TextView tv = (TextView) convertView.findViewById(R.id.voted_movie_name); 
			ImageView im_up = (ImageView)convertView.findViewById(R.id.voted_up_image);
			ImageView im_down = (ImageView)convertView.findViewById(R.id.voted_down_image);	
			Movie m = movieList.get(position);
			im_up.setSelected(false);
			im_down.setSelected(false);
			im_up.setTag(m);

			im_up.setTag(R.id.voted_down_image, im_down);
			im_down.setTag(m);
			im_down.setTag(R.id.voted_up_image, im_up);
			im_up.setOnClickListener(voteClickListener);
			im_down.setOnClickListener(voteClickListener);
			iv.setImageDrawable(DataUtils.getDrawablefromAssets(movieList.get(position).getDrawable(),mContext));
			tv.setText(movieList.get(position).getMovieName());		
			convertView.setTag(movieList.get(position));
			if(movieList.get(position).isVoted()){
				if(movieList.get(position).getVotedValue() == Constants.Votes.UPVOTED)
					im_up.setSelected(true);
				else if(movieList.get(position).getVotedValue() == Constants.Votes.DOWNVOTED)
					im_down.setSelected(true);
			}
			im_up.setTag(R.layout.voted_movie_item, convertView);
			im_down.setTag(R.layout.voted_movie_item, convertView);
			return convertView;

		}

	}
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Movie movie = (Movie)view.getTag();
			if(movie==null)
				return;
			MovieinfoFragment fragment = new MovieinfoFragment(movie);
			((GroupDetailsActivity) mContext).changefragment(fragment);

		}
	};

	@Override
	public void onResume() {
		super.onResume();
		movieList = ((GroupDetailsActivity)mContext).getGroup().getMovieList();
		mMovielistAdapter.notifyDataSetChanged();		
		createvotedMovieList();
		updateStatusText();
	};

	private void updateStatusText() {
		statusText.setText("You have voted on " +votedMovieList.size() +" movies.");
	}

	private void createvotedMovieList() {
		votedMovieList.clear();
		for(Movie m: movieList){
			if(m.isVoted())
				votedMovieList.add(m);
		}



	}

	private OnClickListener mButtonClickListenener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.my_votes_button:
				Intent i = new Intent(mContext, MyVotesActivity.class);
				i.putExtra("group", ((GroupDetailsActivity)mContext).getGroup());
				startActivity(i);
				break;
			case R.id.voting_results_button:
				Intent j = new Intent(mContext, VotingResultsActivity.class);
				j.putExtra("group", ((GroupDetailsActivity)mContext).getGroup().getName().toString());
				startActivity(j);
				break;

			default:
				break;
			}

		}
	};
}
