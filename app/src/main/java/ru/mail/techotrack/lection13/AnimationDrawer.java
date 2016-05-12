package ru.mail.techotrack.lection13;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by vlad on 11/05/16.
 */
public class AnimationDrawer extends View {

	private static final String TAG = "AnimationDrawer";
	private Paint paintLines;
	private Paint paintPoint;
	private Paint paintTracer;
	private RectF coordsRect = new RectF();
	private Path path = new Path();
	private Path trace = new Path();

	private float pointY; // Value of animation
	private float pointX; // Time
	private boolean writeTrace = true;

	private int lastW;
	private int lastH;

	private ValueAnimator animator;

	private final static int LINEAR_INTERPOLATOR = 0;
	private final static int ACCELERATE_INTERPOLATOR = 1;
	private final static int DECELERATE_INTERPOLATOR = 2;
	private final static int ACCELERATEDECELERATE_INTERPOLATOR = 3;
	private final static int ANTICIPATE_INTERPOLATOR = 4;
	private final static int OVERSHOOT_INTERPOLATOR = 5;
	private final static int ANTICIPATEOVERSHOOT_INTERPOLATOR = 6;
	private final static int BOUNCE_INTERPOLATOR = 7;
	private final static int CYCLE_INTERPOLATOR = 8;

	private int interpolatorType = LINEAR_INTERPOLATOR;
	private int duration = 300;
	private float factor = 1.0f;

	public AnimationDrawer(Context context) {
		super(context);
		init();
	}

	public AnimationDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.AnimationDrawer,
				0, 0
		);
		try {
			interpolatorType = a.getInteger(R.styleable.AnimationDrawer_interpolator, 0);
			duration = a.getInteger(R.styleable.AnimationDrawer_duration, 300);
			factor = a.getFloat(R.styleable.AnimationDrawer_factor, 1.0f);
		} finally {
			a.recycle();
		}
		init();
	}

	private void init() {
		paintLines = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintLines.setStyle(Paint.Style.STROKE);
		paintLines.setColor(Color.BLACK);
		paintLines.setStrokeWidth(4);

		paintPoint = new Paint();
		paintPoint.setStyle(Paint.Style.FILL);
		paintPoint.setColor(Color.RED);
		paintPoint.setStrokeWidth(6);

		paintTracer = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintTracer.setStyle(Paint.Style.STROKE);
		paintTracer.setColor(Color.BLUE);
		paintTracer.setStrokeWidth(3);
		if (!this.isInEditMode()) {
			paintTracer.setPathEffect(new CornerPathEffect(10));
		}
	}

	public int getIntepolatorType() {
		return interpolatorType;
	}

	public void setInterpolatorType(int type) {
		interpolatorType = type;
		onSizeChanged(lastW, lastH, lastW, lastH);
	}

	class MyEvaluator extends FloatEvaluator {
		private static final String TAG = "MyEvaluator";
		@Override
		public Float evaluate(float fraction, Number startValue, Number endValue) {
			Log.d(TAG, "evaluate() called with: " + "fraction = [" + fraction + "], startValue = [" + startValue + "], endValue = [" + endValue + "]");
			return super.evaluate(fraction, startValue, endValue);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Try for a width based on our minimum
		int minw = getPaddingLeft() + getPaddingRight() + 150;//getSuggestedMinimumWidth();
		int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

		// Whatever the width ends up being, ask for a height that would let the pie
		// get as big as it can
		int minh = getPaddingBottom() + getPaddingTop() + 100;
		int h = Math.max(MeasureSpec.getSize(heightMeasureSpec), minh);

		int size = Math.min(w, h);

		setMeasuredDimension(size, size);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		lastH = h;
		lastW = w;

		Log.d(TAG, "onSizeChanged() called with: " + "w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");

		writeTrace = true;
		trace = new Path();

		int size = Math.min(w-40, h);
		w = size;
		h = size;

		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		float ww = (float) w - xpad;
		float hh = (float) h - ypad;

		coordsRect.left = getPaddingLeft();
		coordsRect.right = ww;
		coordsRect.top = getPaddingTop();
		coordsRect.bottom = hh;

		path = new Path();
		path.moveTo(coordsRect.left, coordsRect.top);
		path.lineTo(coordsRect.left-4, coordsRect.top+10);
		path.lineTo(coordsRect.left+4, coordsRect.top+10);
		path.lineTo(coordsRect.left, coordsRect.top);
		path.lineTo(coordsRect.left, coordsRect.bottom);
		path.lineTo(coordsRect.right, coordsRect.bottom);
		path.lineTo(coordsRect.right-10, coordsRect.bottom-4);
		path.lineTo(coordsRect.right-10, coordsRect.bottom+4);
		path.lineTo(coordsRect.right, coordsRect.bottom);


		if (animator != null) {
			animator.cancel();
		}

		TimeInterpolator interpolator;

		switch (interpolatorType) {
			case ACCELERATE_INTERPOLATOR:
				interpolator = new AccelerateInterpolator(factor);
				break;
			case DECELERATE_INTERPOLATOR:
				interpolator = new DecelerateInterpolator(factor);
				break;
			case ACCELERATEDECELERATE_INTERPOLATOR:
				interpolator = new AccelerateDecelerateInterpolator();
				break;
			case ANTICIPATE_INTERPOLATOR:
				interpolator = new AnticipateInterpolator(factor);
				break;
			case OVERSHOOT_INTERPOLATOR:
				interpolator = new OvershootInterpolator(factor);
				break;
			case ANTICIPATEOVERSHOOT_INTERPOLATOR:
				interpolator = new AnticipateOvershootInterpolator(factor);
				break;
			case BOUNCE_INTERPOLATOR:
				interpolator = new BounceInterpolator();
				break;
			case CYCLE_INTERPOLATOR:
				interpolator = new CycleInterpolator(factor);
				break;
			case LINEAR_INTERPOLATOR:
			default:
				interpolator = new LinearInterpolator();
				break;
		}


		animator = ValueAnimator.ofFloat(coordsRect.bottom, coordsRect.top);
		animator.setDuration(duration);
		animator.setInterpolator(interpolator);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		//animator.setEvaluator(new MyEvaluator());

		ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator animation) {
				Float value = (Float) animation.getAnimatedValue();
				pointY = value.floatValue();
				if (interpolatorType == CYCLE_INTERPOLATOR)
					pointY = (pointY - coordsRect.top)/2.0f + coordsRect.top;
				pointX = coordsRect.left + (coordsRect.right - coordsRect.left) * (float)animation.getCurrentPlayTime()/(float)duration;
				if (writeTrace)
					trace.lineTo(pointX, pointY);
				invalidate();
			}
		};

		animator.addUpdateListener(listener);

		animator.addListener(new Animator.AnimatorListener() {
			private static final String TAG = "AnimationDrawer";
			@Override
			public void onAnimationStart(Animator animation) {
				Log.d(TAG, "onAnimationStart() called with: " + "animation = [" + animation + "]");
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				Log.d(TAG, "onAnimationEnd() called with: " + "animation = [" + animation + "]");
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				Log.d(TAG, "onAnimationCancel() called with: " + "animation = [" + animation + "]");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				//Log.d(TAG, "onAnimationRepeat() called with: " + "animation = [" + animation + "]");
				writeTrace = false;
			}
		});
		trace.moveTo(coordsRect.left, coordsRect.bottom);
		animator.start();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(path, paintLines);
		canvas.drawPath(trace, paintTracer);
		canvas.drawOval(pointX-10, pointY-10, pointX+10, pointY+10, paintPoint);
		canvas.drawOval(coordsRect.right+10, pointY-20, coordsRect.right+50, pointY+20, paintPoint);
	}

}
