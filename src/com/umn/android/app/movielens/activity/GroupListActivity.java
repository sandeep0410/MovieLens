package com.umn.android.app.movielens.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.data.GroupData;
import com.umn.android.app.movielens.fragments.GroupListHintsFragment;
import com.umn.android.app.movielens.fragments.GroupListNameFragments;
import com.umn.android.app.movielens.fragments.GroupListNameFragments.GroupListEmptyListener;

public class GroupListActivity extends Activity implements GroupListEmptyListener{
	Button mCreateGroupButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list_screen);
		DataUtils.createUserList(this);
		mCreateGroupButton = (Button)findViewById(R.id.bt_create_group);
		//GroupData.createDummyGroup();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		GroupListHintsFragment fragment = new GroupListHintsFragment();
		fragmentTransaction.add(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
		mCreateGroupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GroupListActivity.this, GroupCreationActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.group_list_screen_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment newFragment;
		FragmentTransaction transaction;
		switch (item.getItemId()) {

		case R.id.listfragment:
			newFragment = new GroupListNameFragments();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, newFragment);
			//transaction.addToBackStack(null);
			transaction.commit();

			break;
		case R.id.hintfragment:
			newFragment = new GroupListHintsFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, newFragment);
			//transaction.addToBackStack(null);
			transaction.commit();
		default:
			break;
		}

		return false;

	}

	@Override
	protected void onResume() {
		super.onResume();
		// TODO Auto-generated method stub
		Fragment newFragment;
		FragmentTransaction transaction;
		if(GroupData.mMoviegroupList.size()==0){
			GroupData.retrieveSavedgroups(getApplicationContext());
		}
		if(GroupData.mMoviegroupList.size()>0){
			newFragment = new GroupListNameFragments();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, newFragment);
			//transaction.addToBackStack(null);
			transaction.commit();
		}else{
			newFragment = new GroupListHintsFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, newFragment);
			//transaction.addToBackStack(null);
			transaction.commit();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		GroupData.saveGroupsToSharedPref(getApplicationContext());
	}

	@Override
	public void changefragment() {
		Fragment newFragment;
		FragmentTransaction transaction;
		newFragment = new GroupListHintsFragment();
		transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		//transaction.addToBackStack(null);
		transaction.commit();
	}
}
