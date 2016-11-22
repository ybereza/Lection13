package ru.mail.techotrack.lection13;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TransitionSimpleFragment extends ColorFragment{
	private boolean scaled;

	public TransitionSimpleFragment() {
		super();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.view_animation_fragment, container, false);
		final ViewGroup layout = (ViewGroup)rootView.findViewById(R.id.container);
		final View button = rootView.findViewById(R.id.test_button);

		scaled = false;

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TransitionManager.beginDelayedTransition(layout);

				ViewGroup.LayoutParams params = button.getLayoutParams();
				if (!scaled) {
					Log.e("Animations", "Inc");
					params.width = (int)(params.width * 1.5);
					params.height = (int)(params.height * 1.5);
				}
				else {
					Log.e("Animations", "Dec");
					params.width = (int)(params.width / 1.5);
					params.height = (int)(params.height / 1.5);
				}

				scaled = !scaled;
				button.setLayoutParams(params);
			}
		});

		return rootView;
	}
}
