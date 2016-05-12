package ru.mail.techotrack.lection13;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.transition.TransitionManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class TransitionSimpleFragment extends ColorFragment{

	public TransitionSimpleFragment() {
		super();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.view_animation_fragment, container, false);
		final View button = rootView.findViewById(R.id.test_button);
		final Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

		final ScaleGestureDetector detector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				TransitionManager.beginDelayedTransition((ViewGroup)rootView);
				ViewGroup.LayoutParams params = button.getLayoutParams();
				if (detector.getScaleFactor() > 1) {
					params.width = 600;
					params.height = 600;
				} else {
					params.width = 200;
					params.height = 200;
				}
				button.setLayoutParams(params);
				return true;
			}
		});

		final GestureDetector detector1 = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				TransitionManager.beginDelayedTransition((ViewGroup)rootView);
				ViewGroup.LayoutParams params = button.getLayoutParams();
				params.width = 600;
				params.height = 600;
				button.setLayoutParams(params);
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				TransitionManager.beginDelayedTransition((ViewGroup)rootView);
				ViewGroup.LayoutParams params = button.getLayoutParams();
				params.width = 200;
				params.height = 200;
				button.setLayoutParams(params);
			}
		});

		button.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				detector.onTouchEvent(event);
				detector1.onTouchEvent(event);
				return true;
			}
		});

		return rootView;
	}
}
