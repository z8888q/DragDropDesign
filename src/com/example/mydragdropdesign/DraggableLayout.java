package com.example.mydragdropdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class DraggableLayout extends RelativeLayout {
	public DraggableLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DraggableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DraggableLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	View draggingView;

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
				drag((int) event.getX(), (int) event.getY());
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

	private void drag(int x, int y) {
		Log.i("drAg", "drag:" + x + "," + y);
		int width = draggingView.getMeasuredWidth();
		int height = draggingView.getMeasuredHeight();

		int l = x - (1 * width / 2);
		int t = y - (1 * height / 2);

		draggingView.layout(l, t, l + width, t + height);

	}

	private void dropView(MotionEvent ev) {
		Log.i("drop", "drop:" + ev.getX() + "," + ev.getY());
		LayoutParams layoutParams = (LayoutParams) draggingView
				.getLayoutParams();
		layoutParams.leftMargin = (int) ev.getX();
		layoutParams.topMargin = (int) ev.getY();
		draggingView.setLayoutParams(layoutParams);
		draggingView = null;
	}

	public void setChildrenLongClickEvent() {
		getChildCount();
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// if (!tempFirst) {
					// tempFirst = true;
					// LayoutParams layoutParams = (LayoutParams) v
					// .getLayoutParams();
					// removeView(v);
					// // v.setVisibility(View.GONE);
					// addView(v, layoutParams);
					// setChildrenLongClickEvent();
					// return onLongClick(v);
					// } else {
					// draggingView = v;
					// return false;
					// }
					// // return false;
					LayoutParams layoutParams = (LayoutParams) v
							.getLayoutParams();
					removeView(v);
					addView(v, layoutParams);
					draggingView = v;
					return false;
				}
			});
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}

}
