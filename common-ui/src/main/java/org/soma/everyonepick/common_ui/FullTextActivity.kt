package org.soma.everyonepick.common_ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import org.soma.everyonepick.common_ui.databinding.ActivityFullTextBinding

class FullTextActivity: AppCompatActivity() {
    private lateinit var binding: ActivityFullTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_text)

        intent.getStringExtra(TITLE_KEY)?.let { binding.customactionbar.setTitle(it) }
        binding.text.text = intent.getStringExtra(TEXT_KEY)

        supportActionBar?.hide()
        window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            it.statusBarColor = Color.WHITE
        }
    }

    companion object {
        private const val TITLE_KEY = "title"
        private const val TEXT_KEY = "text"

        fun Intent.putFullTextActivityExtras(title: String, contents: String) = this.apply {
            putExtra(TITLE_KEY, title)
            putExtra(TEXT_KEY, contents)
        }
    }
}