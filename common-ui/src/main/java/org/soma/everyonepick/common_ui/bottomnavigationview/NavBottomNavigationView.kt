package org.soma.everyonepick.common_ui.bottomnavigationview

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * 각 Item이 Fragment가 아니라, Navigation으로 이루어졌을 때의 [BottomNavigationView]입니다.
 * 해당 커스텀 뷰는 다음과 같은 문제를 해결합니다.
 *  - 현재 위치한 Item을 눌렀을 경우 해당 Item 내부 페이지가 초기화가 되지 않는 문제
 *  - setOnItemSelectedListener()에 추가 동작을 설정하려면 NavigationUI.onNavDestinationSelected()을
 *    내부에서 호출해주어야 하는 불편함
 * 단, 내부 Item이 모두 Navigation라는 것을 가정하고 작성한 코드임에 주의해야 합니다.
 */

class NavBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): IndicatorBottomNavigationView(context, attrs, defStyle) {

    fun addOnItemSelectedListener(navController: NavController, listener: (MenuItem) -> Unit) {
        setOnItemSelectedListener { item ->
            listener.invoke(item)
            return@setOnItemSelectedListener NavigationUI
                .onNavDestinationSelected(item, navController)
        }

        setOnItemReselectedListener { item ->
            // 내비게이션의 초기 상태(startDestination)로 돌아갑니다.
            val navOptions = NavOptions.Builder()
                .setPopUpTo(item.itemId, inclusive = true)
                .build()
            navController.navigate(item.itemId, null, navOptions)
        }

        navController.addOnDestinationChangedListener { _, _, _ ->
            startIndicatorAnimation(selectedItemId)
        }
    }
}