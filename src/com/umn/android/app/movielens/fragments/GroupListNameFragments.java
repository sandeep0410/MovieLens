package com.umn.android.app.movielens.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umn.android.app.movielens.R;
import com.umn.android.app.movielens.activity.GroupCreationActivity;
import com.umn.android.app.movielens.activity.GroupDetailsActivity;
import com.umn.android.app.movielens.data.GroupData;
import com.umn.android.app.movielens.data.MovieGroup;

public class GroupListNameFragments extends Fragment{
	public interface GroupListEmptyListener{
		public void changefragment();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.grouplist_fragments, container, false);
		ListView lv = (ListView)rootView.findViewById(R.id.group_list);
		Log.d("sandeep", " " +getActivity());
		lv.setAdapter(new ListviewContactAdapter(getActivity()));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getActivity(), GroupDetailsActivity.class);
				i.putExtra("group", (String)view.getTag());
				startActivity(i);
			}
		});

		return rootView;
	}

	private class ListviewContactAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		MovieGroup mg;
		private OnClickListener  itemOnclickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mg = GroupData.mMoviegroupList.get((Integer)v.getTag());
				switch(v.getId()){
				case R.id.edit_icon:					
					Intent i = new Intent(getActivity(), GroupCreationActivity.class);
					i.putExtra("EditGroup", mg);
					startActivity(i);				
					break;
				case R.id.delete_icon:
					GroupData.mGroupIds.remove((Integer)mg.getId());
					GroupData.mMoviegroupList.remove(mg);					
					if(getCount()==0){
						GroupListEmptyListener gl = (GroupListEmptyListener)getActivity();
						gl.changefragment();
					}
					notifyDataSetChanged();
					break;
				case R.id.favorite_icon:
					break;
				default:
					break;
				}

			}
		};

		public ListviewContactAdapter(Activity activity) {
			mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return GroupData.mMoviegroupList.size();
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
			Log.d("sandeep","getview: " +position);
			convertView = mInflater.inflate(R.layout.group_list_fragment_item, parent, false);
			TextView tv = (TextView) convertView.findViewById(R.id.group_name); 
			ImageView edit = (ImageView)convertView.findViewById(R.id.edit_icon);
			ImageView delete = (ImageView)convertView.findViewById(R.id.delete_icon);
			ImageView fav = (ImageView)convertView.findViewById(R.id.favorite_icon);
			tv.setText(GroupData.mMoviegroupList.get(position).getName());
			convertView.setTag(GroupData.mMoviegroupList.get(position).getName());
			edit.setTag(position);
			delete.setTag(position);
			fav.setTag(position);
			edit.setOnClickListener(itemOnclickListener);
			delete.setOnClickListener(itemOnclickListener);
			fav.setOnClickListener(itemOnclickListener);

			return convertView;
		}

	}

}
