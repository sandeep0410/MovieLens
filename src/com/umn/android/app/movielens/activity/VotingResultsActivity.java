package com.umn.android.app.movielens.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.data.GroupData;
import com.umn.android.app.movielens.data.Movie;
import com.umn.android.app.movielens.data.MovieGroup;

public class VotingResultsActivity extends Activity{
	ListView mListView;
	MovieGroup moviegroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voting_results_screen);
		String groupname = getIntent().getStringExtra("group");
		moviegroup = GroupData.getGroup(groupname);		
		getActionBar().setTitle("Voting Results");
		if(moviegroup != null )
			DataUtils.createVotedMovielist(this, moviegroup);
		else
			DataUtils.createVotedMovielist(this);
		TextView view = new TextView(this);
		int padding = getResources().getDimensionPixelSize(R.dimen.padding_voting_results_text);
		view.setPadding(padding, padding, padding, padding);
		view.setText(getResources().getString(R.string.more_movies_text));
		view.setGravity(Gravity.CENTER);
		view.setTextSize(19);
		mListView = (ListView)findViewById(R.id.movie_results);
		mListView.setAdapter(new MoviesResultsAdapter());
		mListView.addFooterView(view);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	private class MoviesResultsAdapter extends BaseAdapter{
		LayoutInflater mInflater;
		ArrayList<Movie> movieList;


		public MoviesResultsAdapter(){
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			movieList = DataUtils.votedmovieList;
			Collections.sort(movieList, new Comparator<Movie>() {

				@Override
				public int compare(Movie lhs, Movie rhs) {
					int vote_diff = (rhs.getUpVoteCount() - rhs.getDownVoteCount()) - (lhs.getUpVoteCount()-lhs.getDownVoteCount());
					if(vote_diff==0)
						vote_diff = rhs.getUpVoteCount()-lhs.getUpVoteCount();
					return vote_diff;
				}
			});

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return DataUtils.votedmovieList.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
				convertView = mInflater.inflate(R.layout.voted_movie_result_item, parent, false);

			ImageView iv = (ImageView)convertView.findViewById(R.id.voted_movie_result_image_icon);
			TextView tv = (TextView) convertView.findViewById(R.id.voted_movie_result_name); 
			ImageView im_up = (ImageView)convertView.findViewById(R.id.voted_up_result_image);
			ImageView im_down = (ImageView)convertView.findViewById(R.id.voted_down_result_image);	
			TextView upvoted_number = (TextView)convertView.findViewById(R.id.upvoted_number);
			TextView down_voted_number = (TextView)convertView.findViewById(R.id.downvoted_number);
			im_up.setSelected(false);
			im_down.setSelected(false);		
			tv.setSelected(true);
			iv.setImageDrawable(DataUtils.getDrawablefromAssets(movieList.get(position).getDrawable(),getApplicationContext()));
			tv.setText(movieList.get(position).getMovieName());		
			upvoted_number.setText("" +movieList.get(position).getUpVoteCount());
			down_voted_number.setText("" +movieList.get(position).getDownVoteCount());
			if(movieList.get(position).isVoted()){
				if(movieList.get(position).getUpVoteCount()<movieList.get(position).getDownVoteCount()){
					im_down.setSelected(true);
				}else
					im_up.setSelected(true);
			}
			return convertView;

		}

	}
}
