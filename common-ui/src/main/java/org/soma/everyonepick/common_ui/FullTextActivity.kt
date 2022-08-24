package org.soma.everyonepick.common_ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import org.soma.everyonepick.common_ui.databinding.ActivityFullTextBinding

/**
 * 스크롤 가능한 TextView로 화면을 채우는 액티비티입니다. [CustomActionBar] 또한 포함되어 있고 [TITLE_KEY]를 통해 얻은
 * String으로 [CustomActionBar.setTitle]를 호출합니다. 또한 [TEXT_KEY]로 얻은 String으로 TextView를 채웁니다.
 */
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