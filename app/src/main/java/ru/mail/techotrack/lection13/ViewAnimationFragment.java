package ru.mail.techotrack.lection13;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ViewAnimationFragment extends ColorFragment {
	public ViewAnimationFragment() {
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
				v.animate()
						.x(v.getX() == 100.0f ? 300.0f : 100.0f)
						.y(v.getY() == 100.0f ? 300.0f : 100.0f)
						.scaleY(v.getScaleY() >= 1.99f ? 1.0f : 2.0f)
						.rotation(v.getRotation() == 720.0f ? 0.0f : 720.0f)
						.setDuration(4000)
						.start();
			}
		});

		return rootView;
	}

}
