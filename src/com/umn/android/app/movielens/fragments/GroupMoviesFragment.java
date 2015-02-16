package com.umn.android.app.movielens.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.activity.MovieSearchActivity;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.data.Movie;

public class GroupMoviesFragment extends Fragment implements OnQueryTextListener{
	private ArrayList<Movie> selectedMovies=new ArrayList<Movie>();
	private ArrayList<Movie> movieList;	
	ListView mListView;
	SearchView mSearchView;
	LinearLayout hs;
	TextView main_horizontal_textview;
	Activity mContext;
	

    private String[] mFriendsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.movie_search_fragment, container, false);
		mContext = getActivity();


//        mFriendsList = getResources().getStringArray(R.id.friend_names_drawer);
//        mDrawerLayout = (DrawerLayout) findViewById(R.layout.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.right_drawer_list);

        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, mFriendsList));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		mContext.getActionBar().setTitle(getResources().getString(R.string.add_movies_screen));
		mContext.getActionBar().setDisplayHomeAsUpEnabled(false);
		setHasOptionsMenu(true);

		mListView = (ListView)rootView.findViewById(R.id.movies_list);
		mListView.setAdapter(new AddMoviesListAdapter());
		mListView.setTextFilterEnabled(true);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb = (CheckBox)view.findViewById(R.id.add_friend_check);

				if(cb.isChecked()){
					cb.setChecked(false);
					hs.removeViewAt(selectedMovies.indexOf(movieList.get(position))+1);
					selectedMovies.remove(movieList.get(position));
					if(hs.getChildCount()==1)
						main_horizontal_textview.setVisibility(View.VISIBLE);
				}else{
					cb.setChecked(true);
					selectedMovies.add(movieList.get(position));
				}
				mContext.invalidateOptionsMenu();
			}

		});
		return rootView;
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @SuppressWarnings("rawtypes")
		@Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(mFriendsList[position]);
	    mDrawerLayout.closeDrawer(mDrawerList);
	}

	public void setTitle(CharSequence title) {
//	    mTitle = title;
//	    getActionBar().setTitle(mTitle);
	}
	
	private class AddMoviesListAdapter extends BaseAdapter implements Filterable{
		private LayoutInflater mInflater;
		private ArrayList<Movie> orig;
		private OnClickListener imageClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Movie m = (Movie)v.getTag();							
				MovieinfoFragment fragment = new MovieinfoFragment(m);
				((MovieSearchActivity) mContext).changefragmenttomovieInfo(fragment);
			}
		};


		public AddMoviesListAdapter() {
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			movieList = new ArrayList<Movie>(DataUtils.movieList);
		}

		@Override
		public int getCount() {
			return movieList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){

			if(convertView==null)
				convertView = mInflater.inflate(R.layout.add_friends_screen_item, parent, false);
			ImageView iv = (ImageView)convertView.findViewById(R.id.add_friends_item_icon);
			TextView tv = (TextView) convertView.findViewById(R.id.add_friend_name); 
			CheckBox cb = (CheckBox)convertView.findViewById(R.id.add_friend_check);
			iv.setImageDrawable(DataUtils.getDrawablefromAssets(movieList.get(position).getDrawable(), mContext));
			iv.setTag(movieList.get(position));
			iv.setOnClickListener(imageClickListener);
			tv.setText(movieList.get(position).getMovieName());
			cb.setChecked(false);
			if(selectedMovies.contains(movieList.get(position)))
				cb.setChecked(true);
			return convertView;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					movieList = (ArrayList<Movie>) results.values;
					notifyDataSetChanged();}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					final FilterResults oReturn = new FilterResults();
					final ArrayList<Movie> results = new ArrayList<Movie>();
					if (orig == null)
						orig = movieList;
					if (constraint != null) {
						if (orig != null && orig.size() > 0) {
							for (final Movie g : orig) {
								if (g.getMovieName().toLowerCase()
										.contains(constraint.toString()))
									results.add(g);
							}
						}
						oReturn.values = results;
					}
					return oReturn;
				}
			};
		}

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (TextUtils.isEmpty(newText)) {
			mListView.clearTextFilter();
		} else {
			mListView.setFilterText(newText);
		}
		return true;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

		MenuItem item = (MenuItem)menu.findItem(R.id.add_friends_done);
		if(selectedMovies == null || selectedMovies.size()==0){
			item.setEnabled(false);
		}else
			item.setEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.add_friends_done){
			mContext.finish();
		}
		return true;

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_freinds_menu, menu);


	}
}
