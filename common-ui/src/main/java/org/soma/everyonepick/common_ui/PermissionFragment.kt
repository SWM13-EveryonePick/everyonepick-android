package org.soma.everyonepick.common_ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.soma.everyonepick.common_ui.databinding.FragmentPermissionBinding

/**
 * PermissionFragment는, 권한이 필요한 페이지에 진입하기 전에 방문하여 권한을 얻고 가는 중간 프래그먼트입니다.
 * 해당 프래그먼트의 플로우는 다음과 같습니다.
 * ```
 * "hasAllPermissions?" --(O) - Destination
 *                      `-(X) - Request permissions --(O) - Destination
 *                                                  `-(X) - Stay here
 * ```
 *
 * 외부 저장소를 사용한다고 했을 때의 사용 예시는 다음과 같습니다.
 * ```
 * val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
 * val permissionFragment = PermissionFragment(requiredPermissions) {
 *     // Navigate to the destination fragment which requires READ_EXTERNAL_STORAGE
 * }
 * // Navigate to permissionFragment
 * ```
 *
 * @param requiredPermissions 요구되는 권한들의 리스트입니다. [Manifest.permission]의 상수들을 참조합니다.
 * @param onSuccess 권한을 얻었을 때 수행하게 되는 콜백입니다. 일반적으로 권한이 필요한 페이지에 진입하는 코드를 담게 됩니다.
 *
 */

class PermissionFragment(
    private val requiredPermissions: Array<String>,
    private val onSuccess: () -> Unit
) : Fragment() {
    private var _binding: FragmentPermissionBinding? = null

    private val binding get() = _binding!!
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if (isGranted.all{ it.value }) {
            onSuccess.invoke()
        } else {
            binding.button.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onRequestPermissionListener = View.OnClickListener {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${activity?.packageName}")
                )
                startActivity(intent)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (!hasAllPermissions(requireContext(), requiredPermissions)) {
            requestPermissionsLauncher.launch(requiredPermissions)
        } else {
            onSuccess.invoke()
        }
    }

    private fun hasAllPermissions(context: Context, requiredPermissions: Array<String>) = requiredPermissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}