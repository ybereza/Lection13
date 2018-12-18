package ru.mail.techotrack.lection13

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ViewAnimationFragment : ColorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.view_animation_fragment, container, false)

        val button = rootView.findViewById<View>(R.id.test_button)

        button.setOnClickListener { v ->
            v.animate()
                .x(if (v.x == 100.0f) 300.0f else 100.0f)
                .y(if (v.y == 100.0f) 300.0f else 100.0f)
                .scaleY(if (v.scaleY >= 1.99f) 1.0f else 2.0f)
                .rotation(if (v.rotation == 720.0f) 0.0f else 720.0f)
                .setDuration(4000)
                .start()
        }

        return rootView
    }

}
