package ru.mail.techotrack.lection13

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.felipecsl.gifimageview.library.GifImageView

import java.io.IOException
import java.io.InputStream

/**
 * Created by vlad on 12/05/16.
 */
class GifLibViewFragment : ColorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.gif_view_fragment, container, false)

        val gifImageView = rootView.findViewById<View>(R.id.gifImageView) as GifImageView

        try {
            var `is`: InputStream? = null
            var fileBytes = ByteArray(0)
            `is` = activity!!.assets.open("simpson.gif")
            fileBytes = ByteArray(`is`!!.available())
            `is`.read(fileBytes)
            `is`.close()
            gifImageView.setBytes(fileBytes)
            gifImageView.startAnimation()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        return rootView
    }
}
