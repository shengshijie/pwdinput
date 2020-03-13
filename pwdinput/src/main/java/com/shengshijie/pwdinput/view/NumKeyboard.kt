package com.shengshijie.pwdinput.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shengshijie.pwdinput.R
import java.util.*

class NumKeyboard : LinearLayout {
    private var mContext: Context
    private var rvKeyboard: RecyclerView? = null
    private var mAdapter: KeyboardAdapter? = null

    constructor(context: Context) : super(context) {
        mContext = context
        initKeyboardView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        initKeyboardView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        initKeyboardView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measuredWidth, measuredHeight)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, measuredHeight)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measuredWidth, heightSpecSize)
        }
    }

    private fun initKeyboardView() {
        val view = View.inflate(mContext, R.layout.layout_keyboard, this)
        rvKeyboard = view.findViewById(R.id.rv_keyboard)
        mAdapter = KeyboardAdapter(mContext)
        mAdapter?.setSelectedData(convertKeyBoardItem())
        rvKeyboard?.layoutManager = GridLayoutManager(mContext, 3)
        rvKeyboard?.adapter = mAdapter
    }

    fun bindTextView(textView: PwdInputView?) {
        mAdapter?.bindTextView(textView)
    }

    private fun convertKeyBoardItem(): List<KeyBoardItem> {
        val keyBoardItems: MutableList<KeyBoardItem> = ArrayList()
        for (i in 1..9) {
            keyBoardItems.add(KeyBoardItem(KeyboardAdapter.TYPE_NUM, i))
        }
        keyBoardItems.add(KeyBoardItem(KeyboardAdapter.TYPE_EMPTY))
        keyBoardItems.add(KeyBoardItem(KeyboardAdapter.TYPE_NUM, 0))
        keyBoardItems.add(KeyBoardItem(KeyboardAdapter.TYPE_DELETE))
        return keyBoardItems
    }
}


class KeyboardAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mSelectedData: List<KeyBoardItem> = ArrayList()
    private var mPwdInputView: PwdInputView? = null

    fun bindTextView(pwdInputDialog: PwdInputView?) {
        mPwdInputView = pwdInputDialog
    }

    fun setSelectedData(mSelectedData: List<KeyBoardItem>) {
        this.mSelectedData = mSelectedData
    }

    companion object {
        const val TYPE_NUM = 1001
        const val TYPE_DELETE = 1002
        const val TYPE_EMPTY = 1003
    }

    override fun getItemViewType(position: Int): Int {
        return mSelectedData[position].itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NUM -> NumViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_num, parent, false))
            TYPE_DELETE -> DelViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_del, parent, false))
            TYPE_EMPTY -> EmptyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_empty, parent, false))
            else -> EmptyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_empty, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return mSelectedData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val keyBoardItem: KeyBoardItem = mSelectedData[position]
        when (holder) {
            is NumViewHolder -> {
                holder.tvNum?.text = keyBoardItem.value.toString()
                holder.flRoot?.setOnClickListener {
                    if (mPwdInputView == null) {
                        return@setOnClickListener
                    }
                    if (mPwdInputView!!.length() >= mPwdInputView!!.passwordLength) {
                        return@setOnClickListener
                    }
                    mPwdInputView?.append(keyBoardItem.value.toString() + "")
                }
            }
            is DelViewHolder -> {
                holder.flRoot?.setOnClickListener {
                    if (mPwdInputView == null || mPwdInputView?.length() == 0) {
                        return@setOnClickListener
                    }
                    mPwdInputView?.setText(mPwdInputView?.text?.subSequence(0, mPwdInputView!!.length() - 1))
                }
            }
        }
    }

    class NumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNum: TextView? = itemView.findViewById(R.id.tv_num)
        var flRoot: FrameLayout? = itemView.findViewById(R.id.fl_root)
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var flRoot: FrameLayout? = itemView.findViewById(R.id.fl_root)
    }

    class DelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivDel: ImageView? = itemView.findViewById(R.id.iv_del)
        var flRoot: FrameLayout? = itemView.findViewById(R.id.fl_root)
    }

}

class KeyBoardItem {
    var itemType: Int
    var value: Int

    constructor(itemType: Int) {
        this.itemType = itemType
        value = 0
    }

    constructor(itemType: Int, value: Int) {
        this.itemType = itemType
        this.value = value
    }

}