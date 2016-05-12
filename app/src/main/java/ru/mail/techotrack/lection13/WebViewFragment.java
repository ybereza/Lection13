package ru.mail.techotrack.lection13;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

/**
 * Created by vlad on 12/05/16.
 */
public class WebViewFragment extends ColorFragment{
	public WebViewFragment() {
		super();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.webview_fragment, container, false);

		WebView vw = (WebView)rootView.findViewById(R.id.web_view);

		vw.setInitialScale(1);
		vw.getSettings().setLoadWithOverviewMode(true);
		//_vw.setScrollContainer(false);
		vw.getSettings().setUseWideViewPort(true);

		vw.setVerticalScrollBarEnabled(false);
		vw.setHorizontalScrollBarEnabled(false);

		vw.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return (event.getAction() == MotionEvent.ACTION_MOVE);
			}
		});

		String strb = "<html>\n" +
				"    <head>\n" +
				"<style>" +
				"body{\n" +
				"height: 100%;" +
				"width: 100%;" +
				"   }" +
				"img\n" +
				"{\n" +
				"width: 90%;" +
				"display: block;\n" +
				"    margin-left: auto;\n" +
				"    margin-right: auto;" +
				"}\n" +
				"</style>\n" +
				"    </head>\n" +
				"    <body>\n" +
				"        <img src='simpson.gif'/>\n" +
				"   </body>\n" +
				"</html>";
		vw.loadDataWithBaseURL("file:///android_asset/", strb, "text/html", "utf-8", null);


		return rootView;
	}
}
