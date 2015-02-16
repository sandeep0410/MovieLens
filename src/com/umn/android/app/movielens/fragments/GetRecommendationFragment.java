package com.umn.android.app.movielens.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.activity.GroupDetailsActivity;
import com.umn.android.app.movielens.activity.MovieSearchActivity;

public class GetRecommendationFragment extends Fragment {
	Button mMovielens;
	Button mYourSelection;	
	private GroupDetailsActivity activity;
	boolean isAdditionIntent = false;
	public GetRecommendationFragment(boolean additionintent) {
		isAdditionIntent =additionintent;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.group_detail_first_fragment, container, false);
		activity = (GroupDetailsActivity)getActivity();
		mMovielens = (Button)rootView.findViewById(R.id.select_ml);
		mYourSelection = (Button)rootView.findViewById(R.id.your_movie_selection_button);
		/*activity.getActionBar().setHomeButtonEnabled(false);
		setHasOptionsMenu(true);*/
		activity.getActionBar().setTitle(((GroupDetailsActivity)activity).getGroup().getName());
		mMovielens.setOnClickListener(mOnclickListener);
		mYourSelection.setOnClickListener(mOnclickListener);

		return rootView;
	}

	private OnClickListener mOnclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.select_ml:
				if(isAdditionIntent)
					activity.setMode(GroupDetailsActivity.MODE_ML_ADD);
				else
					activity.setMode(GroupDetailsActivity.MODE_ML);
				GroupVotingFragment fragment = new GroupVotingFragment();
				((GroupDetailsActivity) activity).changefragment(fragment);
				break;
			case R.id.your_movie_selection_button:
				activity.setMode(GroupDetailsActivity.MODE_SELF);
				Intent i = new Intent(activity, MovieSearchActivity.class);
				if(isAdditionIntent)
					activity.startActivityForResult(i, 2001);
				else
					activity.startActivityForResult(i, 2000);
				break;
			default:
				break;
			}
		}
	};
}
