package com.dam18.project.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            val usuario = usuario.text.toString()
            startActivity(intentFor<MainActivity>("usuario" to usuario).singleTop())
        }
    }
}
