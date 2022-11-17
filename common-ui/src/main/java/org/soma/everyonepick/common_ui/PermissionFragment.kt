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
import androidx.core.os.bundleOf
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
 */

class PermissionFragment : Fragment() {
    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if (isGranted.all{ it.value }) {
            setFragmentResult()
        } else {
            binding.button.visibility = View.VISIBLE
            Toast.makeText(requireContext(), getString(R.string.toast_need_to_get_permission), Toast.LENGTH_SHORT).show()
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

        val requiredPermissions = arguments?.getStringArray(REQUIRED_PERMISSIONS_KEY) ?: arrayOf()
        if (!hasAllPermissions(requireContext(), requiredPermissions)) {
            requestPermissionsLauncher.launch(requiredPermissions)
        } else {
            setFragmentResult()
        }
    }

    private fun hasAllPermissions(context: Context, requiredPermissions: Array<String>) = requiredPermissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setFragmentResult() {
        activity?.supportFragmentManager?.setFragmentResult(PERMISSION_FRAGMENT_REQUEST_KEY, bundleOf())
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    companion object {
        private const val REQUIRED_PERMISSIONS_KEY = "required_permissions_key"
        const val PERMISSION_FRAGMENT_REQUEST_KEY = "permission_fragment_request_key"

        /**
         * @param requiredPermissions 요구되는 권한들의 리스트입니다. [Manifest.permission]의 상수들을 참조합니다.
         */
        @JvmStatic
        fun newInstance(requiredPermissions: Array<String>) : PermissionFragment {
            val fragment = PermissionFragment()
            val bundle = Bundle()
            bundle.putStringArray(REQUIRED_PERMISSIONS_KEY, requiredPermissions)
            fragment.arguments = bundle
            return fragment
        }
    }
}