package ru.mail.techotrack.lection13

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ResAnimationFragment : ColorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.view_animation_fragment, container, false)

        val button = rootView.findViewById<View>(R.id.test_button)

        val set = AnimatorInflater.loadAnimator(context, R.animator.button_anim) as AnimatorSet
        set.setTarget(button)

        button.setOnClickListener {
            if (!set.isStarted)
                set.start()
            else if (!set.isPaused)
                set.pause()
            else
                set.resume()
        }

        return rootView
    }

    companion object {
        private val TAG = "ResAnimationFragment"
    }
}
