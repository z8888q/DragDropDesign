/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mydragdropdesign;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This application demonstrates how to use LayoutTransition to automate
 * transition animations as items are removed from or added to a container.
 */
public class LayoutAnimations extends Activity {

	private int numButtons = 1;
	ViewGroup container = null;
	private int screenWidth;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_animations);

		container = new FixedGridLayout(this);
		container.setClipChildren(false);
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		((FixedGridLayout) container).setCellHeight(screenWidth / 4);
		((FixedGridLayout) container).setCellWidth(screenWidth / 4);

		setupContainerTransaction();

		ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
		parent.addView(container);
		parent.setClipChildren(false);
		Button addButton = (Button) findViewById(R.id.addNewButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LinearLayout childLayout = new LinearLayout(
						LayoutAnimations.this);
				childLayout.setGravity(Gravity.CENTER);
				childLayout.setPadding(8, 8, 8, 8);

				// childLayout.setBackgroundColor(Color.parseColor("#7e8f95"));
				childLayout.setClickable(true);
				childLayout.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						container.removeView(v);
					}
				});

				LinearLayout childLayout1 = new LinearLayout(
						LayoutAnimations.this);
				childLayout1.setBackgroundColor(Color.parseColor("#208c8c"));
				childLayout1.setPadding(10, 10, 10, 10);
				childLayout1.setGravity(Gravity.CENTER);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				layoutParams.width = screenWidth / 4;
				layoutParams.height = screenWidth / 4;
				childLayout.addView(childLayout1, layoutParams);
				TextView newButton = new TextView(LayoutAnimations.this);
				newButton.setTextColor(Color.parseColor("#0a6873"));
				newButton.setText(String.valueOf(numButtons++));
				childLayout1.addView(newButton);
				container.addView(childLayout,
						Math.min(1, container.getChildCount()));
			}
		});

	}

	@SuppressLint("NewApi")
	private void setupContainerTransaction() {
		if (Build.VERSION.SDK_INT >= 11) {
			LayoutTransition lt = new LayoutTransition();
			if (Build.VERSION.SDK_INT >= 16) {
				lt.disableTransitionType(LayoutTransition.APPEARING);
				lt.enableTransitionType(LayoutTransition.CHANGING);
				lt.disableTransitionType(LayoutTransition.DISAPPEARING);
			}
			container.setLayoutTransition(lt);
		}

	}

}