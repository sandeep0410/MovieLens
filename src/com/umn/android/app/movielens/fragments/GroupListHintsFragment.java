package com.umn.android.app.movielens.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umn.android.app.movielens.R;

public class GroupListHintsFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		return inflater.inflate(R.layout.fragment_grouplist_hints, container, false);
	}
}
