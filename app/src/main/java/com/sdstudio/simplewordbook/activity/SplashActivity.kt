package com.sdstudio.simplewordbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sdstudio.simplewordbook.R

class SplashActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT: Long = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            //어떤 액티비티로 넘어 갈지 설정 - 당연히 메인액티비로 넘어 가면 된다.
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)

    }
}