package ru.mail.techotrack.lection13;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class Res2AnimationFragment extends ColorFragment {
	private static final String TAG = "ResAnimationFragment";

	public Res2AnimationFragment() {
		super();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.view_animation_fragment, container, false);
		View button = rootView.findViewById(R.id.test_button);
		final Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);


		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.startAnimation(shake);
			}
		});

		return rootView;
	}
}
