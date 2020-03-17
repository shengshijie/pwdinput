package com.shengshijie.pwdinput.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.shengshijie.pwdinput.R

class PwdInputDialog(private var activity: AppCompatActivity) : DialogFragment() {
    private var onPwdInputComplete: (String, PwdInputDialog) -> Unit? = { _, _ -> }
    private var title: String = ""
    private var message: String = ""
    private var user: String = ""
    private var length = 6
    private var tvResult: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.attributes?.windowAnimations = R.style.AnimBottomPushWindow
        return inflater.inflate(R.layout.dialog_pwd_input, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(true)
        val keyboardView: NumKeyboard = view.findViewById(R.id.keyboardView)
        val pwdInputView: PwdInputView = view.findViewById(R.id.passwordInputView)
        pwdInputView.passwordLength = length
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        tvTitle.text = title
        val tvMessage: TextView = view.findViewById(R.id.tv_message)
        tvMessage.text = message
        val tvUser: TextView = view.findViewById(R.id.tv_user)
        tvUser.text = user
        tvResult = view.findViewById(R.id.tv_result)
        keyboardView.bindTextView(pwdInputView) { dismiss() }
        pwdInputView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().length == pwdInputView.passwordLength) {
                    onPwdInputComplete(editable.toString(), this@PwdInputDialog)
                } else {
                    tvResult?.text = ""
                }
            }
        })
    }

    override fun onResume() {
        val window: Window? = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(R.color.transparent)
        window?.setGravity(Gravity.CENTER)
        super.onResume()
    }

    fun show() {
        show(activity.supportFragmentManager, "")
    }

    fun setTextChangeListener(onPwdInputComplete: (String, PwdInputDialog) -> Unit): PwdInputDialog {
        this.onPwdInputComplete = onPwdInputComplete
        return this
    }

    fun setTitle(title: String): PwdInputDialog {
        this.title = title
        return this
    }

    fun setMessage(message: String): PwdInputDialog {
        this.message = message
        return this
    }

    fun setUser(user: String): PwdInputDialog {
        this.user = user
        return this
    }

    fun setPasswordLength(length: Int): PwdInputDialog {
        this.length = length
        return this
    }

    fun setResult(result: String) {
        tvResult?.text = result
    }

    companion object {
        fun newInstance(activity: AppCompatActivity): PwdInputDialog {
            return PwdInputDialog(activity)
        }
    }
}