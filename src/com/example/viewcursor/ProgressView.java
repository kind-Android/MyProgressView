package com.example.viewcursor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class ProgressView extends View {

	private Paint mProgressPaint;
	private Paint mMinTagPaint;
	private Paint mFlightingPaint;
	private int mMaxProgress;
	private int mCurringProgress;
	private long mStartTime;
	private long mPauseTime;
	private int mVLineWidth;
	private boolean isRecoding = false;
	private boolean mStop = false;
	private boolean mActiveState;
	private boolean isPause;

	/**
	 * 闪动
	 */
	private final static int HANDLER_INVALIDATE_ACTIVE = 0;
	private final static int HANDLER_INVALIDATE_RECODING = 1;
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case HANDLER_INVALIDATE_ACTIVE:
				invalidate();
				mActiveState = !mActiveState;
				if (!mStop)
					 sendEmptyMessageDelayed(HANDLER_INVALIDATE_ACTIVE, 300);
					break;
			case HANDLER_INVALIDATE_RECODING:
				invalidate();
				sendEmptyMessage(HANDLER_INVALIDATE_RECODING);
			}

		}
	};

	public void setMaxProgress(int maxProgress) {
		this.mMaxProgress = maxProgress;
	}

	public ProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProgressView(Context context) {
		super(context);
		init();
	}

	private void init() {

		mProgressPaint = new Paint();
		mMinTagPaint = new Paint();
		mFlightingPaint = new Paint();
		setBackgroundColor(getResources().getColor(R.color.camera_bg));

		mProgressPaint.setColor(0xFFFEEF00);
		mProgressPaint.setStyle(Paint.Style.FILL);

		mMinTagPaint.setColor(getResources().getColor(android.R.color.white));
		mMinTagPaint.setStyle(Paint.Style.FILL);

		mFlightingPaint.setColor(getResources().getColor(android.R.color.white));
		mFlightingPaint.setStyle(Paint.Style.FILL);

		mVLineWidth = UIUtils.dipToPx(getContext(), 1);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = getMeasuredWidth(), height = getMeasuredHeight();
		int left = 0, right = 0;

		if (isRecoding) {
			mCurringProgress = (int) (System.currentTimeMillis() - mStartTime + mPauseTime);
			left = right;
			right = left + (int) (mCurringProgress * 1.0F / mMaxProgress * width);
			canvas.drawRect(left, 0.0F, right, height, mProgressPaint);
		} else {
			if (isPause) {
				right = left + (int) (mCurringProgress * 1.0F / mMaxProgress * width);
				canvas.drawRect(0.0F, 0.0F, right, height, mProgressPaint);
			}
		}
		// 画三秒
		if (mCurringProgress < 5000) {
			left = (int) (5000F / mMaxProgress * width);
			canvas.drawRect(left, 0.0F, left + mVLineWidth, height, mMinTagPaint);
		}

		if (mActiveState) {
			if (right + 8 >= width)
				right = width - 8;
			canvas.drawRect(right, 0.0F, right + 8, getMeasuredHeight(), mFlightingPaint);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mStop = false;
		mHandler.sendEmptyMessage(HANDLER_INVALIDATE_ACTIVE);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mStop = true;
		mHandler.removeMessages(HANDLER_INVALIDATE_ACTIVE);
	}

	public void startRecode() {
		mStartTime = System.currentTimeMillis();
		mHandler.sendEmptyMessage(HANDLER_INVALIDATE_RECODING);
		isRecoding = true;
		isPause = false;
	}

	public void pauseRecode() {
		isPause = true;
		mPauseTime = mCurringProgress;
		isRecoding = false;
		mHandler.removeMessages(HANDLER_INVALIDATE_RECODING);
	}

	public void stopRecode() {
		mStartTime = 0;
		mCurringProgress = 0;
		mPauseTime = 0;
		isRecoding = false;
		isPause = false;
		mHandler.removeMessages(HANDLER_INVALIDATE_RECODING);
	}

}
