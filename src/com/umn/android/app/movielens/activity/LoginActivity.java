package com.umn.android.app.movielens.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.constants.DataUtils;


public class LoginActivity extends Activity{
	ImageButton mFacebookButton;
	ImageButton mGooglePlusButton;
	ImageButton mMovieLensButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_login);
		DataUtils.createMovieList(this);
		DataUtils.createUserList(this);
		//DataUtils.createVotedMovielist(this);

		mFacebookButton = (ImageButton)findViewById(R.id.facebook_button);
		mGooglePlusButton = (ImageButton)findViewById(R.id.google_button);
		mMovieLensButton = (ImageButton)findViewById(R.id.movielens_button);

		mFacebookButton.setOnClickListener(mLoginOnclickListener);
		mGooglePlusButton.setOnClickListener(mLoginOnclickListener);
		mMovieLensButton.setOnClickListener(mLoginOnclickListener);
	}

	private OnClickListener mLoginOnclickListener = new OnClickListener() {

		@Override
		public void onClick(View button) {
			switch(button.getId()){
			case R.id.facebook_button:

			case R.id.google_button:

			case R.id.movielens_button:
				gotoGroupCreationActvity();
				break;
			default:
				break;
			}

		}
	};



	private void gotoGroupCreationActvity(){

		Intent i = new Intent(LoginActivity.this, GroupListActivity.class);
		startActivity(i);
		finish();

	}
}
