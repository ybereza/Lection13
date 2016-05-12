package ru.mail.techotrack.lection13;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ValueAnimationFragment extends Fragment {
	private static final String TAG = "ValueAnimationFragment";

	public ValueAnimationFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner);
		Resources res = getActivity().getResources();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, res.getStringArray(R.array.interpolators));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final AnimationDrawer animationDrawer = (AnimationDrawer)rootView.findViewById(R.id.animation_canvas);

		spinner.setAdapter(adapter);
		spinner.setSelection(0);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				animationDrawer.setInterpolatorType(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		rootView.setBackgroundColor(Color.LTGRAY);

		return rootView;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}
}
