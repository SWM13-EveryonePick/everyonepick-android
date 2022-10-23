package org.soma.everyonepick.app

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import org.soma.everyonepick.common.util.NATIVE_APP_KEY

@HiltAndroidApp
class EveryonePickApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, NATIVE_APP_KEY)
    }
}