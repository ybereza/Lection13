package ru.mail.techotrack.lection13;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ObjectAnimationFragment extends ColorFragment {
	public ObjectAnimationFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_animation_fragment, container, false);

		View button = rootView.findViewById(R.id.test_button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float x = v.getX();
				float y = v.getY();
				ObjectAnimator animX1 = ObjectAnimator.ofFloat(v, "x", 100.0f);
				ObjectAnimator animY1 = ObjectAnimator.ofFloat(v, "y", 100.0f);
				ObjectAnimator rot1 = ObjectAnimator.ofFloat(v, "rotation", 720.0f);
				AnimatorSet set1 = new AnimatorSet();
				set1.playTogether(animX1, animY1, rot1);
				ObjectAnimator animX2 = ObjectAnimator.ofFloat(v, "x", x);
				ObjectAnimator animY2 = ObjectAnimator.ofFloat(v, "y", y);
				ObjectAnimator rot2 = ObjectAnimator.ofFloat(v, "rotation", 0.0f);
				AnimatorSet set2 = new AnimatorSet();
				set2.playTogether(animX2, animY2, rot2);

				AnimatorSet set = new AnimatorSet();
				set.play(set2).after(set1);

				set.setDuration(4000);
				set.start();
			}
		});

		return rootView;
	}
}
