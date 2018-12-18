package ru.mail.techotrack.lection13

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button

class Res2AnimationFragment : ColorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.view_animation_fragment, container, false)
        val button = rootView.findViewById<View>(R.id.test_button)
        val shake = AnimationUtils.loadAnimation(activity, R.anim.shake)


        button.setOnClickListener { v -> v.startAnimation(shake) }

        return rootView
    }

    companion object {
        private val TAG = "ResAnimationFragment"
    }
}
