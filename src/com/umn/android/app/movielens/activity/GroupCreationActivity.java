package com.umn.android.app.movielens.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.constants.DataUtils;
import com.umn.android.app.movielens.data.GroupData;
import com.umn.android.app.movielens.data.MovieGroup;
import com.umn.android.app.movielens.data.User;

public class GroupCreationActivity extends Activity implements TextWatcher{
	ArrayList<User> mfriendList = new ArrayList<User>();
	ListView lv ;
	ListViewGroupMembersAdapter groupAdapter;
	TextView mMembersCount;
	EditText mGroupName;
	MovieGroup mg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mg = (MovieGroup) getIntent().getSerializableExtra("EditGroup");
		getActionBar().setTitle(getResources().getString(R.string.new_group));
		setContentView(R.layout.group_creation_screen);
		lv = (ListView)findViewById(R.id.group_members_list);
		mMembersCount = (TextView)findViewById(R.id.group_members);
		mGroupName = (EditText)findViewById(R.id.group_name_new);	
		mGroupName.addTextChangedListener(this);
		if(mg!=null){
			mfriendList = mg.getGroupMembers();
			mGroupName.setText(mg.getName());
			updateGroupNumber();
		}
	
		groupAdapter = new ListViewGroupMembersAdapter();
		lv.setAdapter(groupAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(mfriendList ==null || mfriendList.size() == 0){
					if(position == 1){
						startActivityForResult(new Intent(GroupCreationActivity.this, AddFriendsActivity.class), 111);
					}
				}else if(position == mfriendList.size()+1){
					Intent i = new Intent(GroupCreationActivity.this, AddFriendsActivity.class);
					i.putParcelableArrayListExtra("FromGrpCreation", (ArrayList<? extends Parcelable>) mfriendList);
					startActivityForResult(i, 111);
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.group_creation_screen_menu, menu);
		return true;
	}


	private class ListViewGroupMembersAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		private OnClickListener mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				User friend = (User) v.getTag();
				mfriendList.remove(friend);
				updateGroupNumber();
				notifyDataSetChanged();
				invalidateOptionsMenu();

			}
		};
		public ListViewGroupMembersAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
		public boolean isEnabled(int position) {
			if(position == getCount()-1)
				return true;
			else
				return false;

		}
		@Override
		public int getCount() {
			int count;
			if(mfriendList==null)
				count= 2;
			else
				count = mfriendList.size()+2;
			return count;
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
				convertView = mInflater.inflate(R.layout.group_creation_screen_item, parent, false);
			ImageView iv = (ImageView)convertView.findViewById(R.id.item_icon);
			TextView tv = (TextView) convertView.findViewById(R.id.members_name); 
			ImageButton delete = (ImageButton)convertView.findViewById(R.id.delete_member);
			if(position==0){
				iv.setImageDrawable(getResources().getDrawable(R.drawable.me_icon));
				tv.setText(getResources().getString(R.string.item_me));
				delete.setVisibility(View.GONE);
			}else if(position == getCount()-1){
				iv.setImageDrawable(getResources().getDrawable(R.drawable.add_members));
				tv.setText(getResources().getString(R.string.add_members_item));
				delete.setVisibility(View.GONE);				
			}
			else if(mfriendList!=null){
				iv.setImageDrawable(DataUtils.getDrawablefromAssets(mfriendList.get(position-1).getDrawable(),getApplicationContext()));
				tv.setText(mfriendList.get(position-1).getName());
				delete.setVisibility(View.VISIBLE);
				delete.setTag(mfriendList.get(position-1));
				delete.setOnClickListener(mClickListener );
			}

			return convertView;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 111 && resultCode == RESULT_OK){
			ArrayList<? extends Parcelable> tempArrayList = data.getParcelableArrayListExtra("selected");
			for(int i = 0; i<tempArrayList.size(); i++){
				if(!mfriendList.contains((User)tempArrayList.get(i)))
					mfriendList.add((User) tempArrayList.get(i));
			}
			Collections.sort(mfriendList, new Comparator<User>() {

				@Override
				public int compare(User lhs, User rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			groupAdapter.notifyDataSetChanged();

		}
		updateGroupNumber();
		invalidateOptionsMenu();
	}

	private void updateGroupNumber() {
		mMembersCount.setText(getResources().getString(R.string.heading_members)+" (" +(mfriendList.size()+1) +")");
	}



	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.group_creation_done);
		if(mGroupName.getText().toString().trim().length()>0 && mfriendList.size()>0)
			item.setEnabled(true);
		else
			item.setEnabled(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.group_creation_done){
			if(mg!=null && mg.getName().equals(mGroupName.getText().toString())){
				GroupData.editGroup(mfriendList, mg.getName());				
				finish();
			}
			else if(GroupData.createGroup(mfriendList, mGroupName.getText().toString())){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_group_created), Toast.LENGTH_SHORT).show();
				Intent i = new Intent(GroupCreationActivity.this, GroupDetailsActivity.class);
				i.putExtra("group", mGroupName.getText().toString());

				startActivity(i);
				finish();
			}else{
				item.setEnabled(false);
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_group_already_exists), Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		invalidateOptionsMenu();

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}
}
