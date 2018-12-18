package ru.mail.techotrack.lection13

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by vlad on 12/05/16.
 */
class GifViewFragment : ColorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.gif_view_fragment, container, false)
    }
}
