package ru.mail.techotrack.lection13;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ResAnimationFragment extends ColorFragment {
	private static final String TAG = "ResAnimationFragment";

	public ResAnimationFragment() {
		super();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.view_animation_fragment, container, false);

		View button = rootView.findViewById(R.id.test_button);

		final AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.button_anim);
		set.setTarget(button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!set.isStarted())
					set.start();
				else if (!set.isPaused())
					set.pause();
				else
					set.resume();
			}
		});

		return rootView;
	}
}
