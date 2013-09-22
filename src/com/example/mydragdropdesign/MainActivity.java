package com.example.mydragdropdesign;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {

	// private Button draggableButton;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
	}

	private void initViews() {

		DraggableLayout draggableLayout = (DraggableLayout) findViewById(R.id.draggable_layout);
		draggableLayout.setChildrenLongClickEvent();
		// draggableButton = (Button) findViewById(R.id.button1);
		// draggableButton.setOnLongClickListener(new OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View v) {
		// DraggableLayout draggableLayout = (DraggableLayout) v
		// .getParent();
		// if (draggableLayout != null) {
		// draggableLayout.draggingView = v;
		//
		// }
		// return false;
		// }
		// });
	}

	public void clickToSay(View v) {
		Log.i("clickToSay", "" + v.toString());
		ViewGroup group = (ViewGroup) v.getParent();
		group.removeView(v);
	}

}
