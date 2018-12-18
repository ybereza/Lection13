package ru.mail.techotrack.lection13

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class ValueAnimationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        val spinner = rootView.findViewById<View>(R.id.spinner) as Spinner
        val res = activity!!.resources
        val adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_item,
            res.getStringArray(R.array.interpolators)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val animationDrawer = rootView.findViewById<View>(R.id.animation_canvas) as AnimationDrawer

        spinner.adapter = adapter
        spinner.setSelection(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                animationDrawer.setInterpolatorType(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        rootView.setBackgroundColor(Color.LTGRAY)

        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    companion object {
        private val TAG = "ValueAnimationFragment"
    }
}
