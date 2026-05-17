package com.example.jenugumpuu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class SettingsActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getPersistedLocale(newBase)
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<CardView>(R.id.cardEnglish).setOnClickListener {
            updateLanguage("en")
        }

        findViewById<CardView>(R.id.cardKannada).setOnClickListener {
            updateLanguage("kn")
        }

        findViewById<Button>(R.id.btnBackSettings).setOnClickListener {
            finish()
        }
    }

    private fun updateLanguage(code: String) {
        LocaleHelper.setLocale(this, code)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}