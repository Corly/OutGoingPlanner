package com.smartIntern.FavoritedPoints;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanning.MainActivity;
import com.example.eventplanning.R;

public class FavoritedPoints extends Activity{
	
	private ListView mListView;
	private Context cnt = this;
	private Button clear;
	
	OnTouchListener touchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1)
		{
			switch (arg1.getAction())
			{
				case MotionEvent.ACTION_DOWN:
				{
					arg0.setBackgroundResource(R.drawable.clearbuttonclick);
					return false;
				}
				case MotionEvent.ACTION_UP:
				{
					arg0.setBackgroundResource(R.drawable.clearbutton);
					return false;
				}
			}
			return false;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.saved_routes);
		
		clear = (Button)findViewById(R.id.clear_all_routes);
		clear.setOnTouchListener(touchListener);
		
		TextView tv = (TextView) findViewById(R.id.txt_saved);
		tv.setText("Favorited points:");
		
		Toast.makeText(getApplicationContext() , "Hint: Long click to delete a favorited point" , Toast.LENGTH_SHORT).show();
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty));
		
		ArrayList<String> mItems = new ArrayList<String>();
		for (int i=0;i<FavoritedPointsVector.getInstance().favPoints.size();i++){
			mItems.add(FavoritedPointsVector.getInstance().favPoints.get(i).name);
		}
		
		FavoritedPointsAdapter adapter = new FavoritedPointsAdapter(getApplicationContext(), mItems);
		mListView.setAdapter(adapter);
		
		final FavoritedPointsAdapter adap = adapter;
		
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				Intent i = new Intent(cnt, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("title", "Route from "+ "\"" + FavoritedPointsVector.getInstance().favPoints.get(arg2).name+"\"" + " point");
				i.putExtra("lat", FavoritedPointsVector.getInstance().favPoints.get(arg2).lat);
				i.putExtra("lng", FavoritedPointsVector.getInstance().favPoints.get(arg2).lng);
				cnt.startActivity(i);	
				
			}
		
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, 
					int pos, long id){
				
				final int position = pos;
				AlertDialog.Builder dialog = new AlertDialog.Builder(cnt);
				final AlertDialog.Builder auxDialog = dialog; 
				dialog.setMessage("Are you sure you want to delete " +"\""+ adap.mItems.get(position) + "\""+ " from your favorited points?");
				dialog.setNegativeButton("Delete", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						adap.mItems.remove(position);
						adap.notifyDataSetChanged();
						FavoritedPointsVector.getInstance().favPoints.remove(position);
						
						if ( FavoritedPointsVector.getInstance().favPoints.isEmpty()){
							Toast.makeText(getApplicationContext() , "All favorited points were deleted!" , Toast.LENGTH_SHORT).show();
							finish();
						}
						else {
							Toast.makeText(getApplicationContext() , "Deleted!" , Toast.LENGTH_SHORT).show();
						}
					}				
				});
				dialog.setPositiveButton("Cancel", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						AlertDialog d = auxDialog.show();
						d.dismiss();
					}				
				});
				dialog.show();
				return true;
			}
		});
		
		Button clear = (Button)findViewById(R.id.clear_all_routes);
		
		clear.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(FavoritedPoints.this);
				final AlertDialog.Builder auxDialog = dialog; 
				dialog.setMessage("Are you sure you want to delete all your favorited?");
				dialog.setNegativeButton("Delete", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						while ( !FavoritedPointsVector.getInstance().favPoints.isEmpty()){
							FavoritedPointsVector.getInstance().favPoints.remove(0);
						}
						Toast.makeText(getApplicationContext() , "All deleted!" , Toast.LENGTH_SHORT).show();
						finish();
					}				
				});
				dialog.setPositiveButton("Cancel", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						AlertDialog d = auxDialog.show();
						d.dismiss();
					}				
				});
				dialog.show();
				
			}
		});
	}
}
