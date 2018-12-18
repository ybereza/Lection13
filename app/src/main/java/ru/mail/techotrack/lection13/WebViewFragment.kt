package ru.mail.techotrack.lection13

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.RelativeLayout

/**
 * Created by vlad on 12/05/16.
 */
class WebViewFragment : ColorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.webview_fragment, container, false)

        val vw = rootView.findViewById<View>(R.id.web_view) as WebView

        vw.setInitialScale(1)
        vw.settings.loadWithOverviewMode = true
        //_vw.setScrollContainer(false);
        vw.settings.useWideViewPort = true

        vw.isVerticalScrollBarEnabled = false
        vw.isHorizontalScrollBarEnabled = false

        vw.setOnTouchListener { v, event -> event.action == MotionEvent.ACTION_MOVE }

        val strb = "<html>\n" +
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
                "</html>"
        vw.loadDataWithBaseURL("file:///android_asset/", strb, "text/html", "utf-8", null)


        return rootView
    }
}
