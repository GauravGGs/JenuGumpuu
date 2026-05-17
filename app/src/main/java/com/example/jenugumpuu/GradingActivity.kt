package com.example.jenugumpuu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class GradingActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getPersistedLocale(newBase)
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grading)

        findViewById<CardView>(R.id.cardMoistureInfo).setOnClickListener {
            startActivity(Intent(this, MoistureInfoActivity::class.java))
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}