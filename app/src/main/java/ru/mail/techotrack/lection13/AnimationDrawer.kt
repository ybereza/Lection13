package ru.mail.techotrack.lection13

import android.animation.Animator
import android.animation.FloatEvaluator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator

/**
 * Created by vlad on 11/05/16.
 */
class AnimationDrawer : View {
    private val paintLines = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintPoint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintTracer = Paint(Paint.ANTI_ALIAS_FLAG)
    private val coordsRect = RectF()
    private var path = Path()
    private var trace = Path()

    private var pointY: Float = 0.toFloat() // Value of animation
    private var pointX: Float = 0.toFloat() // Time
    private var writeTrace = true

    private var lastW: Int = 0
    private var lastH: Int = 0

    private var animator: ValueAnimator? = null

    var intepolatorType = LINEAR_INTERPOLATOR
        private set
    private var defDuration = 300
    private var factor = 1.0f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.AnimationDrawer,
                0, 0
        )
        try {
            intepolatorType = a.getInteger(R.styleable.AnimationDrawer_interpolator, 0)
            defDuration = a.getInteger(R.styleable.AnimationDrawer_duration, 300)
            factor = a.getFloat(R.styleable.AnimationDrawer_factor, 1.0f)
        } finally {
            a.recycle()
        }
        init()
    }

    private fun init() {
        paintLines.style = Paint.Style.STROKE
        paintLines.color = Color.BLACK
        paintLines.strokeWidth = 4f

        paintPoint.style = Paint.Style.FILL
        paintPoint.color = Color.RED
        paintPoint.strokeWidth = 6f

        paintTracer.style = Paint.Style.STROKE
        paintTracer.color = Color.BLUE
        paintTracer.strokeWidth = 3f

        if (!isInEditMode) {
            paintTracer.pathEffect = CornerPathEffect(10f)
        }
    }

    fun setInterpolatorType(type: Int) {
        intepolatorType = type
        onSizeChanged(lastW, lastH, lastW, lastH)
    }

    internal inner class MyEvaluator : FloatEvaluator() {
        private val TAG = "MyEvaluator"

        override fun evaluate(fraction: Float, startValue: Number, endValue: Number): Float? {
            Log.d(TAG, "evaluate() called with: fraction = [$fraction], startValue = [$startValue], endValue = [$endValue]")
            return super.evaluate(fraction, startValue, endValue)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Try for a width based on our minimum
        val minw = paddingLeft + paddingRight + 150//getSuggestedMinimumWidth();
        val w = Math.max(minw, View.MeasureSpec.getSize(widthMeasureSpec))

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        val minh = paddingBottom + paddingTop + 100
        val h = Math.max(View.MeasureSpec.getSize(heightMeasureSpec), minh)

        val size = Math.min(w, h)

        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        var w = w
        var h = h
        super.onSizeChanged(w, h, oldw, oldh)

        lastH = h
        lastW = w

        Log.d(TAG, "onSizeChanged() called with: w = [$w], h = [$h], oldw = [$oldw], oldh = [$oldh]")

        writeTrace = true
        trace = Path()

        val size = Math.min(w - 40, h)
        w = size
        h = size

        val xpad = (paddingLeft + paddingRight).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

        val ww = w.toFloat() - xpad
        val hh = h.toFloat() - ypad

        coordsRect.left = paddingLeft.toFloat()
        coordsRect.right = ww
        coordsRect.top = paddingTop.toFloat()
        coordsRect.bottom = hh

        path = Path()
        path.moveTo(coordsRect.left, coordsRect.top)
        path.lineTo(coordsRect.left - 4, coordsRect.top + 10)
        path.lineTo(coordsRect.left + 4, coordsRect.top + 10)
        path.lineTo(coordsRect.left, coordsRect.top)
        path.lineTo(coordsRect.left, coordsRect.bottom)
        path.lineTo(coordsRect.right, coordsRect.bottom)
        path.lineTo(coordsRect.right - 10, coordsRect.bottom - 4)
        path.lineTo(coordsRect.right - 10, coordsRect.bottom + 4)
        path.lineTo(coordsRect.right, coordsRect.bottom)


        if (animator != null) {
            animator!!.cancel()
        }

        val interp : TimeInterpolator = when (intepolatorType) {
            ACCELERATE_INTERPOLATOR -> AccelerateInterpolator(factor)
            DECELERATE_INTERPOLATOR -> DecelerateInterpolator(factor)
            ACCELERATEDECELERATE_INTERPOLATOR -> AccelerateDecelerateInterpolator()
            ANTICIPATE_INTERPOLATOR -> AnticipateInterpolator(factor)
            OVERSHOOT_INTERPOLATOR -> OvershootInterpolator(factor)
            ANTICIPATEOVERSHOOT_INTERPOLATOR -> AnticipateOvershootInterpolator(factor)
            BOUNCE_INTERPOLATOR -> BounceInterpolator()
            CYCLE_INTERPOLATOR -> CycleInterpolator(factor)
            LINEAR_INTERPOLATOR -> LinearInterpolator()
            else -> LinearInterpolator()
        }

        val listener = ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            pointY = value
            if (intepolatorType == CYCLE_INTERPOLATOR)
                pointY = (pointY - coordsRect.top) / 2.0f + coordsRect.top
            pointX = coordsRect.left + (coordsRect.right - coordsRect.left) * animation.currentPlayTime.toFloat() / defDuration.toFloat()
            if (writeTrace)
                trace.lineTo(pointX, pointY)
            invalidate()
        }

        animator = ValueAnimator.ofFloat(coordsRect.bottom, coordsRect.top).apply {
            duration = defDuration.toLong()
            interpolator = interp
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener(listener)
        }
        //animator.setEvaluator(new MyEvaluator());


        animator!!.addListener(object : Animator.AnimatorListener {
            private val TAG = "AnimationDrawer"
            override fun onAnimationStart(animation: Animator) {
                Log.d(TAG, "onAnimationStart() called with: animation = [$animation]")
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.d(TAG, "onAnimationEnd() called with: animation = [$animation]")
            }

            override fun onAnimationCancel(animation: Animator) {
                Log.d(TAG, "onAnimationCancel() called with: animation = [$animation]")
            }

            override fun onAnimationRepeat(animation: Animator) {
                //Log.d(TAG, "onAnimationRepeat() called with: " + "animation = [" + animation + "]");
                writeTrace = false
            }
        })
        trace.moveTo(coordsRect.left, coordsRect.bottom)
        animator!!.start()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paintLines!!)
        canvas.drawPath(trace, paintTracer!!)
        canvas.drawOval(pointX - 10, pointY - 10, pointX + 10, pointY + 10, paintPoint!!)
        canvas.drawOval(coordsRect.right + 10, pointY - 20, coordsRect.right + 50, pointY + 20, paintPoint!!)
    }

    companion object {

        private val TAG = "AnimationDrawer"

        private val LINEAR_INTERPOLATOR = 0
        private val ACCELERATE_INTERPOLATOR = 1
        private val DECELERATE_INTERPOLATOR = 2
        private val ACCELERATEDECELERATE_INTERPOLATOR = 3
        private val ANTICIPATE_INTERPOLATOR = 4
        private val OVERSHOOT_INTERPOLATOR = 5
        private val ANTICIPATEOVERSHOOT_INTERPOLATOR = 6
        private val BOUNCE_INTERPOLATOR = 7
        private val CYCLE_INTERPOLATOR = 8
    }

}
