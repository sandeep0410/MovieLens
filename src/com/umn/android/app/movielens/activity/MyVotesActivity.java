package com.umn.android.app.movielens.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.constants.Constants;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.data.Movie;
import com.umn.android.app.movielens.data.MovieGroup;

public class MyVotesActivity extends Activity{
	ListView mListView;
	Button mClearAll;
	ArrayList<Movie> movieList = new ArrayList<Movie>() ;

	VotedMoviesAdapter mVotedAdapter ;
	MovieGroup mg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_votes_screen);
		if(GroupDetailsActivity.getInstance()!=null){
			mg =	GroupDetailsActivity.getInstance().getGroup();
		}else
			mg = (MovieGroup)getIntent().getSerializableExtra("group");
		createMovieList(mg);
		getActionBar().setTitle("My Votes");
		mListView = (ListView)findViewById(R.id.voted_movie_list);
		mVotedAdapter= new VotedMoviesAdapter();
		mListView.setAdapter(mVotedAdapter);
		mClearAll = (Button)findViewById(R.id.clear_all);
		mClearAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ConfirmDeleteDialogFragment dialog = new ConfirmDeleteDialogFragment();
				dialog.show(getFragmentManager(), "confirm");

			}
		});

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}


	private void createMovieList(MovieGroup group) {
		if(movieList.size()>0)
			movieList.clear();

		for(Movie m: group.getMovieList()){
			if(m.isVoted())
				movieList.add(m);
		}


	}

	private class VotedMoviesAdapter extends BaseAdapter{
		LayoutInflater mInflater;

		private OnClickListener voteClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isSelected =v.isSelected();
				Movie movie =  (Movie) v.getTag();
				ImageView otherImage;

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
					break;
				default: 
					break;
				}

			}
		};

		public VotedMoviesAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);		

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
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
				convertView = mInflater.inflate(R.layout.voted_movie_item, parent, false);

			ImageView iv = (ImageView)convertView.findViewById(R.id.voted_movie_image_icon);
			TextView tv = (TextView) convertView.findViewById(R.id.voted_movie_name); 
			ImageView im_up = (ImageView)convertView.findViewById(R.id.voted_up_image);
			ImageView im_down = (ImageView)convertView.findViewById(R.id.voted_down_image);	
			im_up.setSelected(false);
			im_down.setSelected(false);
			im_up.setTag(movieList.get(position));
			im_up.setTag(R.id.voted_down_image, im_down);
			im_down.setTag(movieList.get(position));
			im_down.setTag(R.id.voted_up_image, im_up);
			im_up.setOnClickListener(voteClickListener);
			im_down.setOnClickListener(voteClickListener);
			iv.setImageDrawable(DataUtils.getDrawablefromAssets(movieList.get(position).getDrawable(),getApplicationContext()));
			tv.setText(movieList.get(position).getMovieName());		

			if(movieList.get(position).isVoted()){
				if(movieList.get(position).getVotedValue() == Constants.Votes.UPVOTED)
					im_up.setSelected(true);
				else if(movieList.get(position).getVotedValue() == Constants.Votes.DOWNVOTED)
					im_down.setSelected(true);
			}
			return convertView;
		}

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	private class ConfirmDeleteDialogFragment extends DialogFragment{

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Confirm Delete");
			builder.setMessage("Do you want to clear all the votes?");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					for(Movie m: mg.getMovieList()){
						m.resetValues();
					}
					createMovieList(mg);
					mVotedAdapter.notifyDataSetChanged();
					dismiss();
					getActivity().finish();
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();

				}
			});


			return builder.create();

		}

	}

}
