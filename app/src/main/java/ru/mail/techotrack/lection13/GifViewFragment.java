package ru.mail.techotrack.lection13;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vlad on 12/05/16.
 */
public class GifViewFragment extends ColorFragment {
	public GifViewFragment() {
		super();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.gif_view_fragment, container, false);

		return rootView;
	}
}
