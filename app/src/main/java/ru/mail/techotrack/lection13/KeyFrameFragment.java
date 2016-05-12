package ru.mail.techotrack.lection13;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by vlad on 12/05/16.
 */
public class KeyFrameFragment extends ColorFragment {
	public KeyFrameFragment() {
		super();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.keyframe_animation_fragment, container, false);

	    final ImageView image = (ImageView)rootView.findViewById(R.id.test_button);

		((AnimationDrawable)image.getDrawable()).start();

		image.setOnClickListener(new View.OnClickListener() {
			boolean bstart = true;
			@Override
			public void onClick(View v) {

				AnimationDrawable dd = (AnimationDrawable)image.getDrawable();
				if (bstart)
					dd.stop();
				else
					dd.start();

				bstart = !bstart;
			}
		});

		return rootView;
	}
}
