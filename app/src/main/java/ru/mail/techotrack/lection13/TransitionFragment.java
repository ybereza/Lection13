package ru.mail.techotrack.lection13;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TransitionFragment extends ColorFragment {
	public TransitionFragment() {
		super();
	}

	private boolean bscene1 = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.transition_fragment, container, false);


		final ViewGroup sceneRoot = (ViewGroup)rootView.findViewById(R.id.scene_root);
		final Scene scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.scene2, getContext());
		final Scene scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.scene1, getContext());

		Button button = (Button)rootView.findViewById(R.id.btn_change_scene);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TransitionManager.go(bscene1 ? scene2 : scene1);
				// TODO: 12/05/16 опишем свой аналог AutoTransition
				/*
				TransitionSet set = new TransitionSet();
				set.addTransition(new Fade());
				set.addTransition(new ChangeBounds());
				// выполняться они будут одновременно
				set.setOrdering(TransitionSet.ORDERING_TOGETHER);
				// уставим свою длительность анимации
				set.setDuration(5000);
				// и изменим Interpolator
				set.setInterpolator(new AccelerateInterpolator());
				TransitionManager.go(bscene1 ? scene2 : scene1, set);
				*/
				bscene1 = !bscene1;
			}
		});

		return rootView;
	}
}
