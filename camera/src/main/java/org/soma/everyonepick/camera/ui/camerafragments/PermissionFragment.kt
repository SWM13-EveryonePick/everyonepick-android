package org.soma.everyonepick.camera.ui.camerafragments

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common.Permission.Companion.hasAllPermissions

private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

class PermissionFragment : Fragment() {
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if(isGranted.all{ it.value }) {
            navigateToPreview()
        }else{
            Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasAllPermissions(requireContext(), PERMISSIONS_REQUIRED)) {
            requestPermissionsLauncher.launch(PERMISSIONS_REQUIRED)
        }else{
            navigateToPreview()
        }
    }

    private fun navigateToPreview() {
        lifecycleScope.launchWhenCreated {
            findNavController().navigate(
                PermissionFragmentDirections.actionPermissionToPreview()
            )
        }
    }
}