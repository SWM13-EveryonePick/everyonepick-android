package org.soma.everyonepick.common_ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import org.soma.everyonepick.common_ui.databinding.ActivityScrollableWebViewBinding

/**
 * 스크롤 가능한 WebView로 화면을 채우는 액티비티입니다. [CustomActionBar] 또한 포함되어 있고 [TITLE_KEY]를 통해 얻은
 * String으로 [CustomActionBar.setTitle]를 호출합니다. 또한 [URL_KEY]로 얻은 String으로 WebView를 채웁니다.
 */
class ScrollableWebViewActivity: AppCompatActivity() {
    private lateinit var binding: ActivityScrollableWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scrollable_web_view)

        intent.getStringExtra(TITLE_KEY)?.let { binding.customactionbar.setTitle(it) }
        binding.webview.loadUrl(intent.getStringExtra(URL_KEY)?: "")

        supportActionBar?.hide()
        window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            it.statusBarColor = Color.WHITE
        }
    }

    companion object {
        private const val TITLE_KEY = "title"
        private const val URL_KEY = "url"

        fun Intent.putScrollableWebViewActivityExtras(title: String, contents: String) = this.apply {
            putExtra(TITLE_KEY, title)
            putExtra(URL_KEY, contents)
        }
    }
}