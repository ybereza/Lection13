package ru.mail.techotrack.lection13;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.felipecsl.gifimageview.library.GifImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vlad on 12/05/16.
 */
public class GifLibViewFragment extends ColorFragment {
	public GifLibViewFragment() {
		super();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.gif_view_fragment, container, false);

		GifImageView gifImageView = (GifImageView)rootView.findViewById(R.id.gifImageView);

		try {
			InputStream is= null;
			byte[] fileBytes= new byte[0];
			is = getActivity().getAssets().open("simpson.gif");
			fileBytes = new byte[is.available()];
			is.read( fileBytes);
			is.close();
			gifImageView.setBytes(fileBytes);
			gifImageView.startAnimation();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return rootView;
	}
}
