package com.umn.android.app.movielens.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.data.User;

public class AddFriendsActivity extends Activity implements OnQueryTextListener{

	ArrayList<User> selectedfriends;
	public ArrayList<User> friendsArrayList;
	ListView mListView;
	SearchView mSearchView;
	LinearLayout hs;
	TextView main_horizontal_textview;
	private AddFriendsListAdapter mAddFriendsAdapter;
	Filter filter;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friends_screen);
		selectedfriends = new ArrayList<User>();		
		hs = (LinearLayout)findViewById(R.id.horizontal_scrollview);
		main_horizontal_textview = (TextView)findViewById(R.id.scrollView_text);
		main_horizontal_textview.setHint(getResources().getString(R.string.no_friend_selected));
		mSearchView = (SearchView)findViewById(R.id.search_view);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setQueryHint(getResources().getString(R.string.search_query_hint));
		getActionBar().setTitle(getResources().getString(R.string.add_friends_screen_name));
		mListView = (ListView)findViewById(R.id.add_friends_list);
		mAddFriendsAdapter = new AddFriendsListAdapter();
		mListView.setAdapter(mAddFriendsAdapter);
		mListView.setTextFilterEnabled(false);
		filter = mAddFriendsAdapter.getFilter();

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb = (CheckBox)view.findViewById(R.id.add_friend_check);

				if(cb.isChecked()){
					cb.setChecked(false);
					hs.removeViewAt(selectedfriends.indexOf(friendsArrayList.get(position))+1);
					selectedfriends.remove(friendsArrayList.get(position));
					if(hs.getChildCount()==1)
						main_horizontal_textview.setVisibility(View.VISIBLE);
				}else{
					cb.setChecked(true);
					selectedfriends.add(friendsArrayList.get(position));
					addItemToHorizontalScrollView(friendsArrayList.get(position));
				}
				invalidateOptionsMenu();
			}
		});
		ArrayList<? extends Parcelable> tempArrayList = getIntent().getParcelableArrayListExtra("FromGrpCreation");
		if(tempArrayList!=null && tempArrayList.size()>0){
			selectedfriends = (ArrayList<User>) tempArrayList;	
			updateHorizontalLayout();
		}
	}


	private void updateHorizontalLayout() {
		if(selectedfriends!=null && selectedfriends.size()>0){
			for(User seUser: selectedfriends){
				addItemToHorizontalScrollView(seUser);
			}

		}
	}


	private void addItemToHorizontalScrollView(User user) {
		ImageView im = new ImageView(this);
		int h_w = getResources().getDimensionPixelSize(R.dimen.horizontal_scrollview_item_width_height);
		int margin = getResources().getDimensionPixelSize(R.dimen.horizontal_scrollview_item_margins);
		LinearLayout.LayoutParams lp = new LayoutParams(h_w, h_w);
		lp.setMargins(margin, margin, margin, margin);
		im.setLayoutParams(lp);
		im.setImageDrawable(DataUtils.getDrawablefromAssets(user.getDrawable(),this));
		hs.addView(im);

		main_horizontal_textview.setVisibility(View.GONE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_freinds_menu, menu);
		return true;

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		MenuItem item = (MenuItem)menu.findItem(R.id.add_friends_done);
		if(selectedfriends == null || selectedfriends.size()==0){
			item.setEnabled(false);
		}else
			item.setEnabled(true);

		return true;
	}
	private class AddFriendsListAdapter extends BaseAdapter implements Filterable{
		private LayoutInflater mInflater;

		public ArrayList<User> orig;
		public AddFriendsListAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			friendsArrayList =new ArrayList<User>(DataUtils.userList);
		}

		public Filter getFilter(){
			return new Filter(){

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					final FilterResults oReturn = new FilterResults();
					final ArrayList<User> results = new ArrayList<User>();
					if (orig == null)
						orig = friendsArrayList;
					if (constraint != null) {
						if (orig != null && orig.size() > 0) {
							for (final User g : orig) {
								if (g.getName().toLowerCase()
										.contains(constraint.toString()))
									results.add(g);
							}
						}
						oReturn.values = results;
					}
					return oReturn;
				}


				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					friendsArrayList = (ArrayList<User>) results.values;
					notifyDataSetChanged();}

			};
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return friendsArrayList.size();
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
			if(convertView==null)
				convertView = mInflater.inflate(R.layout.add_friends_screen_item, parent, false);
			ImageView iv = (ImageView)convertView.findViewById(R.id.add_friends_item_icon);
			TextView tv = (TextView) convertView.findViewById(R.id.add_friend_name); 
			CheckBox cb = (CheckBox)convertView.findViewById(R.id.add_friend_check);
			iv.setImageDrawable(DataUtils.getDrawablefromAssets(friendsArrayList.get(position).getDrawable(),getApplicationContext()));
			tv.setText(friendsArrayList.get(position).getName());
			cb.setChecked(false);
			if(selectedfriends.contains(friendsArrayList.get(position))){
				cb.setChecked(true);
			}



			return convertView;
		}

	}
	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		filter.filter(newText);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.add_friends_done){
			Intent returnIntent = new Intent();
			returnIntent.putParcelableArrayListExtra("selected", (ArrayList<? extends Parcelable>) selectedfriends);
			setResult(RESULT_OK,returnIntent);
			finish();
		}
		return true;

	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();

	}
}
