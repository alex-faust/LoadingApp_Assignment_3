package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private val pointPosition: PointF = PointF(450.0f, 100.0f)
    private val radioGroup = R.id.radio_btn_group
    private val plus = R.drawable.tester
    private var radius = 30.0f
    private lateinit var button: Rect

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }


    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()

        val animator = ObjectAnimator.ofFloat()

        invalidate() //to tell(forces) the android system to redraw the view

        //download()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Toast.makeText(context, "onDraw()", Toast.LENGTH_SHORT).show()
        paint.color = Color.BLACK
        canvas.drawColor(Color.CYAN)
        canvas.drawText("hello", pointPosition.x, pointPosition.y, paint)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }





}