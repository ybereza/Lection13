package ru.mail.techotrack.lection13;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)button.getLayoutParams();
				if (!scaled) {
					params.width = (int)(params.width * 1.5);
					params.height = (int)(params.height * 1.5);
					params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					params.removeRule(RelativeLayout.ALIGN_PARENT_END);
					params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
					params.addRule(RelativeLayout.ALIGN_PARENT_START);
				}
				else {
					params.width = (int)(params.width / 1.5);
					params.height = (int)(params.height / 1.5);
					params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
					params.removeRule(RelativeLayout.ALIGN_PARENT_START);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					params.addRule(RelativeLayout.ALIGN_PARENT_END);
				}

				scaled = !scaled;
				button.setLayoutParams(params);
			}
		});

		return rootView;
	}
}
