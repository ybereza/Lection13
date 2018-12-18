package ru.mail.techotrack.lection13

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ObjectAnimationFragment : ColorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.view_animation_fragment, container, false)

        val button = rootView.findViewById<View>(R.id.test_button)

        button.setOnClickListener { v ->
            val x = v.x
            val y = v.y
            val animX1 = ObjectAnimator.ofFloat(v, "x", 100.0f)
            val animY1 = ObjectAnimator.ofFloat(v, "y", 100.0f)
            val rot1 = ObjectAnimator.ofFloat(v, "rotation", 720.0f)
            val set1 = AnimatorSet()
            set1.playTogether(animX1, animY1, rot1)
            val animX2 = ObjectAnimator.ofFloat(v, "x", x)
            val animY2 = ObjectAnimator.ofFloat(v, "y", y)
            val rot2 = ObjectAnimator.ofFloat(v, "rotation", 0.0f)
            val set2 = AnimatorSet()
            set2.playTogether(animX2, animY2, rot2)

            val set = AnimatorSet()
            set.play(set2).after(set1)

            set.duration = 4000
            set.start()
        }

        return rootView
    }
}
