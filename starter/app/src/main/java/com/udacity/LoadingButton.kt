package com.udacity

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
    private val pointPosition: PointF = PointF((width / 2).toFloat(), (height / 2).toFloat())

    private var progress: Float = 0f
    private var buttonText = ""

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
        typeface = Typeface.create("", Typeface.BOLD)
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
                valueAnimator = ValueAnimator.ofInt(0, measuredWidth).apply {
                    addUpdateListener {
                        progress = animatedValue as Float
                        invalidate()
                    }
                    duration = 2000
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE
                    start()
                }
            }

            ButtonState.Completed -> {
                valueAnimator.cancel()
                progress = 0f
                invalidate()
            }

            else -> {
                progress = 0f
                invalidate()
            }
        }
    }


    init {
        isClickable = true
        //buttonText = getString(R.styleable.LoadingButton_text)
        ButtonState.Idle

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Toast.makeText(context, "onDraw()", Toast.LENGTH_SHORT).show()

        //canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), buttonPaint)
        //canvas.drawRoundRect(5f, 5f, (width - 10).toFloat(), (height - 10).toFloat(), 100F, 100F, buttonPaint)
        //    left The X coordinate of the left side of the rectangle
        //
        //    top The Y coordinate of the top of the rectangle
        //
        //    right The X coordinate (width) of the right side of the rectangle
        //
        //    bottom The Y coordinate (height) of the bottom of the rectangle
        //so, the top left corner is 0,0 and the bottom right is some other number

        when (buttonState) {
            ButtonState.Loading -> {
                Toast.makeText(context, "buttonState loading", Toast.LENGTH_SHORT).show()

                canvas.drawArc(
                    (width - 200).toFloat(), (height - 150).toFloat(), (width - 30).toFloat(),
                    (height - 15).toFloat(), 15f, 180f, true, circlePaint
                )
            } else -> {
                canvas.drawColor(resources.getColor(R.color.button_background))
            }
        }
        textPaint.color = resources.getColor(R.color.white)
        canvas.drawText(buttonText, pointPosition.x, pointPosition.y, textPaint)

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

    fun setText(text: String) {
        buttonText = text
        invalidate()
    }

    fun updateStatus(state: ButtonState) {
        buttonState = state
    }
}


