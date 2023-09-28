package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
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
    private val pointPosition: PointF by lazy {
        PointF((width / 2).toFloat(), (height / 2).toFloat())
    }

    private var progressW = 0F
    private var progressC = 0F
    private var buttonText = "Download"

    private val buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = resources.getColor(R.color.button_overlay)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.NORMAL)
        color = resources.getColor(R.color.white)
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        typeface = Typeface.create("", Typeface.BOLD)
        color = resources.getColor(R.color.progress_circle)
    }

    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonText = "We are loading"
                valueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
                    addUpdateListener { animation ->
                        progressW = widthSize * animation.animatedValue as Float
                        progressC = 360 * animation.animatedValue as Float
                        invalidate()
                    }
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART
                }
                valueAnimator.addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        progressW = 0F
                        progressC = 0F
                    }
                })
                valueAnimator.start()
            } else -> {
                buttonText = "Download"
                valueAnimator.cancel()
                progressW = 0f
                progressC = 0f
                invalidate()
            }
        }
    }

    init {
        isClickable = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        textPaint.color = resources.getColor(R.color.white)
        canvas.drawText(buttonText, pointPosition.x, pointPosition.y, textPaint)

        when (buttonState) {
            ButtonState.Loading -> {
                canvas.drawColor(resources.getColor(R.color.button_background))
                canvas.drawRect(0f, 0f, progressW, height.toFloat(), buttonPaint)
                canvas.drawArc(
                    (width - 200).toFloat(), (height - 150).toFloat(), (width - 30).toFloat(),
                    (height - 15).toFloat(), 0f, progressC, true, circlePaint
                )
                canvas.drawText(buttonText, pointPosition.x, pointPosition.y, textPaint)
            }
            else -> {
                canvas.drawColor(resources.getColor(R.color.button_background))
                canvas.drawText(buttonText, pointPosition.x, pointPosition.y, textPaint)
            }
        }
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

    fun updateStatus(state: ButtonState) {
        buttonState = state
    }
}


