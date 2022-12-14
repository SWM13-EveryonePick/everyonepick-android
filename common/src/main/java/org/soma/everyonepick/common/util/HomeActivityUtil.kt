package org.soma.everyonepick.common.util

/**
 * 최상위 모듈인 app 모듈에 있는 HomeActivity의 함수에
 * 하위 모듈들이 접근할 수 있게끔 설계한 인터페이스입니다.
 */
interface HomeActivityUtil {
    fun hideBottomNavigationView()
    fun showBottomNavigationView()
    fun hideCameraNavigation()
    fun showCameraNavigation()
    fun showAreYouSureDialog()
    fun startLoginActivity()
    fun navigateToGroupAlbum()
}