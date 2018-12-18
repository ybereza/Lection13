package ru.mail.techotrack.lection13

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView

/**
 * Created by vlad on 12/05/16.
 */
class KeyFrameFragment : ColorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.keyframe_animation_fragment, container, false)

        val image = rootView.findViewById<View>(R.id.test_button) as ImageView

        (image.drawable as AnimationDrawable).start()

        image.setOnClickListener(object : View.OnClickListener {
            internal var bstart = true
            override fun onClick(v: View) {

                val dd = image.drawable as AnimationDrawable
                if (bstart)
                    dd.stop()
                else
                    dd.start()

                bstart = !bstart
            }
        })

        return rootView
    }
}
