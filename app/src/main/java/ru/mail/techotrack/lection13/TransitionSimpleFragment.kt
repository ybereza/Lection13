package ru.mail.techotrack.lection13

import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

class TransitionSimpleFragment : ColorFragment() {
    private var scaled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.view_animation_fragment, container, false)
        val layout = rootView.findViewById<View>(R.id.container) as ViewGroup
        val button = rootView.findViewById<View>(R.id.test_button)

        scaled = false

        button.setOnClickListener {
            TransitionManager.beginDelayedTransition(layout)

            val params = button.layoutParams as RelativeLayout.LayoutParams
            if (!scaled) {
                params.width = (params.width * 1.5).toInt()
                params.height = (params.height * 1.5).toInt()
                params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                params.removeRule(RelativeLayout.ALIGN_PARENT_END)
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                params.addRule(RelativeLayout.ALIGN_PARENT_START)
            } else {
                params.width = (params.width / 1.5).toInt()
                params.height = (params.height / 1.5).toInt()
                params.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
                params.removeRule(RelativeLayout.ALIGN_PARENT_START)
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
            }

            scaled = !scaled
            button.layoutParams = params
        }

        return rootView
    }
}
