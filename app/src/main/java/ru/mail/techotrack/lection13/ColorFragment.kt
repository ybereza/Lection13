package ru.mail.techotrack.lection13

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.Random

/**
 * Created by vlad on 12/05/16.
 */
open class ColorFragment : Fragment() {

    private val mColor: Int

    init {
        val rnd = Random()
        mColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    override fun onStart() {
        view!!.setBackgroundColor(mColor)
        super.onStart()
    }

}

