package com.shengshijie.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.shengshijie.pwdinput.view.PwdInputDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            PwdInputDialog.newInstance(this)
                    .setTitle("本次交易需要辅助验证")
                    .setMessage("请验证用户编号后6位")
                    .setPasswordLength(4)
                    .setUser("用户:张*")
                    .setOnDismiss { Log.e("DDD","AAA") }
                    .setOnPwdInputComplete { text, dialog ->
                        if ("1234" == text) {
                            dialog.dismiss()
                        } else {
                            dialog.setResult("验证失败")
                        }
                    }.show()
        }
    }
}