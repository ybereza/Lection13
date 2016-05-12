package ru.mail.techotrack.lection13;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

/**
 * Created by vlad on 12/05/16.
 */
public class ColorFragment extends Fragment {

    private int mColor;

	public ColorFragment() {
		Random rnd = new Random();
		mColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}

	@Override
	public void onStart() {
		getView().setBackgroundColor(mColor);
		super.onStart();
	}

}

