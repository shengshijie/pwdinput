package com.shengshijie.pwdinput.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.shengshijie.pwdinput.R
import kotlin.math.min

class PwdInputView(private val mContext: Context, attrs: AttributeSet?) : AppCompatEditText(mContext, attrs) {

    private var borderColor = 0
    private var borderRespondingColor = 0
    private var borderWidth = 0f
    private var borderRadius = 0f
    var passwordLength = 0
    private var passwordColor = 0
    private var passwordWidth = 0f
    private var itemPadding = 0f
    private var itemHeight = 0f
    private val passwordPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textLength = 0
    private var paddingVertical = 0f
    private val rect = RectF()

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val a = mContext.theme.obtainStyledAttributes(attrs, R.styleable.PwdInputView, 0, 0)
        borderColor = a.getColor(R.styleable.PwdInputView_borderColor, ContextCompat.getColor(mContext, R.color.textMedium))
        borderRespondingColor = a.getColor(R.styleable.PwdInputView_borderRespondingColor, ContextCompat.getColor(mContext, R.color.colorBlue))
        borderWidth = a.getDimension(R.styleable.PwdInputView_borderWidth, 0.5f)
        borderRadius = a.getDimension(R.styleable.PwdInputView_borderRadius, 2f)
        passwordColor = a.getColor(R.styleable.PwdInputView_passwordColor, ContextCompat.getColor(mContext, R.color.textDark))
        passwordWidth = a.getDimension(R.styleable.PwdInputView_passwordWidth, 6f)
        passwordLength = a.getInteger(R.styleable.PwdInputView_passwordLength, 6)
        itemPadding = a.getDimension(R.styleable.PwdInputView_itemPadding, 8f)
        itemHeight = a.getDimension(R.styleable.PwdInputView_itemHeight, 36f)
        passwordPaint.style = Paint.Style.FILL
        passwordPaint.color = passwordColor
        a.recycle()
        isCursorVisible = false
        filters = arrayOf<InputFilter>(LengthFilter(passwordLength))
        setSingleLine(true)
        inputType = InputType.TYPE_CLASS_NUMBER
        initListener()
    }

    override fun onDraw(canvas: Canvas) {
        for (i in 0 until passwordLength) {
            rect[(itemHeight + itemPadding) * i + itemPadding, paddingVertical, (itemHeight + itemPadding) * (i + 1)] = paddingVertical + itemHeight
            if (i == textLength && hasFocus()) {
                borderPaint.color = borderRespondingColor
            } else {
                borderPaint.color = borderColor
            }
            borderPaint.strokeWidth = borderWidth
            borderPaint.style = Paint.Style.STROKE
            canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint)
        }
        var cx: Float
        val cy = itemHeight / 2 + paddingVertical
        val half = itemHeight / 2 + itemPadding
        for (i in 0 until textLength) {
            cx = (itemHeight + itemPadding) * i + half
            canvas.drawCircle(cx, cy, passwordWidth, passwordPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val width = itemHeight.toInt() * passwordLength + itemPadding.toInt() * (passwordLength + 1) + paddingLeft + paddingRight
        val height = (itemHeight + 2 * itemPadding).toInt() + paddingTop + paddingBottom
        val mHeight: Int
        val mWidth: Int
        mWidth = when (widthSpecMode) {
            MeasureSpec.EXACTLY -> {
                widthSpecSize
            }
            MeasureSpec.AT_MOST -> {
                min(width, widthSpecSize)
            }
            else -> {
                widthSpecSize
            }
        }
        mHeight = when (heightSpecMode) {
            MeasureSpec.EXACTLY -> {
                heightSpecSize
            }
            MeasureSpec.AT_MOST -> {
                min(height, heightSpecSize)
            }
            else -> {
                heightSpecSize
            }
        }
        paddingVertical = (mHeight - itemHeight) / 2
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        textLength = text.length
        invalidate()
    }

    private fun initListener() {
        setOnClickListener { view ->
            view.post {
                val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this@PwdInputView.windowToken, 0)
            }
        }
        onFocusChangeListener = OnFocusChangeListener { view, isFocus ->
            if (isFocus) {
                view.post {
                    val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(this@PwdInputView.windowToken, 0)
                }
            }
        }
    }

}