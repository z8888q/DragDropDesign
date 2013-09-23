/*
 * Copyright (C) 2009 The Android Open Source Project
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * A layout that arranges its children in a grid. The size of the cells is set
 * by the {@link #setCellSize} method and the android:cell_width and
 * android:cell_height attributes in XML. The number of rows and columns is
 * determined at runtime. Each cell contains exactly one view, and they flow in
 * the natural child order (the order in which they were added, or the index in
 * {@link #addViewAt}. Views can not span multiple cells.
 * 
 * <p>
 * This class was copied from the FixedGridLayout Api demo; see that demo for
 * more information on using the layout.
 * </p>
 */
public class FixedGridLayout extends ViewGroup {
	int mCellWidth;
	int mCellHeight;

	public FixedGridLayout(Context context) {
		super(context);
	}

	public void setCellWidth(int px) {
		mCellWidth = px;
		requestLayout();
	}

	public void setCellHeight(int px) {
		mCellHeight = px;
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth,
				MeasureSpec.AT_MOST);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight,
				MeasureSpec.AT_MOST);

		int count = getChildCount();
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);
			child.measure(cellWidthSpec, cellHeightSpec);
		}
		// Use the size our parents gave us, but default to a minimum size to
		// avoid
		// clipping transitioning children
		int minCount = count > 3 ? count : 3;
		setMeasuredDimension(
				resolveSize(mCellWidth * minCount, widthMeasureSpec),
				resolveSize(mCellHeight * minCount, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int cellWidth = mCellWidth;
		int cellHeight = mCellHeight;
		int columns = (r - l) / cellWidth;
		if (columns < 0) {
			columns = 1;
		}
		int x = 0;
		int y = 0;
		int i = 0;
		int count = getChildCount();
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);

			int w = child.getMeasuredWidth();
			int h = child.getMeasuredHeight();

			w = cellWidth;
			h = cellHeight;

			int left = x + ((cellWidth - w) / 2);
			int top = y + ((cellHeight - h) / 2);

			child.layout(left, top, left + w, top + h);
			if (i >= (columns - 1)) {
				// advance to next row
				i = 0;
				x = 0;
				y += cellHeight;
			} else {
				i++;
				x += cellWidth;
			}
		}
	}

	// @Override
	protected void onLayout0(boolean changed, int l, int t, int r, int b) {
		int cellWidth = mCellWidth;
		int cellHeight = mCellHeight;
		int columns = (r - l) / cellWidth;
		if (columns < 0) {
			columns = 1;
		}
		int x = 0;
		int y = 0;
		int i = 0;
		int count = getChildCount();
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);

			int w = child.getMeasuredWidth();
			int h = child.getMeasuredHeight();

			int left = x + ((cellWidth - w) / 2);
			int top = y + ((cellHeight - h) / 2);

			child.layout(left, top, left + w, top + h);
			if (i >= (columns - 1)) {
				// advance to next row
				i = 0;
				x = 0;
				y += cellHeight;
			} else {
				i++;
				x += cellWidth;
			}
		}
	}

	View dragOverView;
	View draggingView;
	Rect initRect = new Rect();

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (draggingView != null) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:
				return true;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
			default:
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (draggingView != null) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:
				drag(event);
				break;
			case MotionEvent.ACTION_UP:
				dropView(event);
				break;
			case MotionEvent.ACTION_CANCEL:
				dropView(event);
				break;
			default:
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	// private void drag(int x, int y) {
	// Log.i("drAg", "drag:" + x + "," + y);
	// int width = draggingView.getMeasuredWidth();
	// int height = draggingView.getMeasuredHeight();
	//
	// int l = x - (1 * width / 2);
	// int t = y - (1 * height / 2);
	//
	// draggingView.layout(l, t, l + width, t + height);
	//
	// // if (!initRect.contains(x, y)) {
	// // LayoutParams layoutParams = (LayoutParams) draggingView
	// // .getLayoutParams();
	// // removeView(draggingView);
	// // addView(draggingView, layoutParams);
	// // }
	// }

	private void dropView(MotionEvent ev) {
		Log.i("drop", "drop:" + ev.getX() + "," + ev.getY());
		// LayoutParams layoutParams = (LayoutParams) draggingView
		// .getLayoutParams();
		// // layoutParams.leftMargin = (int) ev.getX();
		// // layoutParams.topMargin = (int) ev.getY();
		// draggingView.setLayoutParams(layoutParams);

		// draggingView.setVisibility(View.VISIBLE);
		// // TODO 动画
		// mWindowManager.removeView(dragOverView);

		int[] location = new int[2];
		draggingView.getLocationOnScreen(location);
		float toX = location[0];
		float toY = location[1];
		float fromX = mWindowParams.x;
		float fromY = mWindowParams.y;

		Log.i("TranslateAnimation", (toX - fromX) + "," + (toY - fromY));

		Animation translate = new TranslateAnimation(0, toX - fromX, 0, toY
				- fromY);
		translate.setInterpolator(new AccelerateDecelerateInterpolator());
		translate.setDuration(300);
		translate.setFillAfter(true);

		translate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				draggingView.setVisibility(View.VISIBLE);
				// TODO 动画
				mWindowManager.removeView(dragOverView);

				draggingView = null;
			}
		});

		dragOverView.startAnimation(translate);

	}

	public void setChildrenLongClickEvent() {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setOnLongClickListener(getChildLongClickListener());
		}

	}

	protected OnLongClickListener getChildLongClickListener() {
		return new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				startDragging(v);
				return false;
			}
		};
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}

	WindowManager.LayoutParams mWindowParams;
	WindowManager mWindowManager;

	private void startDragging(View view) {

		view.setDrawingCacheEnabled(true);
		draggingView = view;
		initRect = new Rect(draggingView.getLeft(), draggingView.getTop(),
				draggingView.getRight(), draggingView.getBottom());
		lastPosition = getDragPosition(view.getLeft() + view.getMeasuredWidth()
				/ 2, view.getTop() + view.getMeasuredHeight() / 2);

		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());

		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		mWindowParams.x = location[0];
		mWindowParams.y = location[1];

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;

		Context context = getContext();
		ImageView v = new ImageView(context);
		v.setPadding(0, 0, 0, 0);
		v.setImageBitmap(bitmap);

		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(v, mWindowParams);
		dragOverView = v;
		draggingView.setVisibility(View.GONE);

	}

	int lastPosition;

	private void drag(MotionEvent event) {
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();

		if (dragOverView.getVisibility() != View.VISIBLE)
			dragOverView.setVisibility(View.VISIBLE);

		int width = dragOverView.getMeasuredWidth();
		int height = dragOverView.getMeasuredHeight();

		int l = x - (1 * width / 2);
		int t = y - (1 * height / 2);

		mWindowParams.x = l;
		mWindowParams.y = t;
		mWindowManager.updateViewLayout(dragOverView, mWindowParams);

		int addPosition = getDragPosition((int) event.getX(),
				(int) event.getY());
		if (addPosition < 0 || addPosition > getChildCount()) {
			addPosition = getChildCount();
		}
		if (!initRect.contains((int) event.getX(), (int) event.getY())
				&& holdEnough(addPosition)) {
			LayoutParams layoutParams = (LayoutParams) draggingView
					.getLayoutParams();
			removeView(draggingView);

			lastPosition = addPosition;
			addView(draggingView, addPosition, layoutParams);
			initRect = updateInitRect(addPosition);
		}

		// Log.i("drag mWindowManager", mWindowManager.)
	}

	int holdPositioin;
	long holdStartTime;
	final int DEFAULT_HOLD_TIME = 300;

	private boolean holdEnough(int addPosition) {
		if (holdPositioin == addPosition
				&& holdStartTime != 0
				&& (System.currentTimeMillis() - holdStartTime) > DEFAULT_HOLD_TIME) {
			return true;
		}

		if (holdPositioin != addPosition) {
			holdPositioin = addPosition;
			holdStartTime = System.currentTimeMillis();
		}

		return false;
	}

	private Rect updateInitRect(int addPosition) {
		int xP = addPosition % 4;
		int yP = addPosition / 4;
		return new Rect(xP * mCellWidth, yP * mCellHeight, xP * mCellWidth
				+ mCellWidth, yP * mCellHeight + mCellHeight);

	}

	private int getDragPosition(int x, int y) {
		int xP = x / mCellWidth;
		int yP = y / mCellHeight;
		if (x % mCellWidth == 0 && xP > 0) {
			xP = xP - 1;
		}
		if (y % mCellHeight == 0 && yP > 0) {
			yP = yP - 1;
		}
		return yP * 4 + xP;
	}

}
