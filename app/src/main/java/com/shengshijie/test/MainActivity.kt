package com.shengshijie.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shengshijie.pwdinput.view.PwdInputDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            var verifyTime = 3
            PwdInputDialog.newInstance(this)
                    .setTitle("本次交易需要辅助验证")
                    .setMessage("请验证用户编号后6位")
                    .setPasswordLength(4)
                    .setUser("用户:张*")
                    .setTimeout(5)
                    .setOnDismissListener { Log.e("DDD", "setOnDismissListener") }
                    .setOnTimeoutListener { Log.e("DDD", "setOnTimeoutListener") }
                    .setOnPwdInputComplete { text, dialog ->
                        if ("1234" == text) {
                            dialog.dismiss()
                            Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show()
                        } else {
                            if (verifyTime <= 1) {
                                dialog.dismiss()
                                Toast.makeText(this, "验证次数已达上限", Toast.LENGTH_SHORT).show()
                                return@setOnPwdInputComplete
                            }
                            dialog.clearText()
                            verifyTime--
                            dialog.setResult("验证失败,剩余验证次数:$verifyTime")
                        }
                    }.show()
        }
    }
}