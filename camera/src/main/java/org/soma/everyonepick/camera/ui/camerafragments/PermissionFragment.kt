package org.soma.everyonepick.camera.ui.camerafragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.camera.databinding.FragmentPermission2Binding
import org.soma.everyonepick.camera.databinding.FragmentPreview2Binding
import org.soma.everyonepick.common.Permission.Companion.hasAllPermissions

private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

class PermissionFragment : Fragment() {
    private var _binding: FragmentPermission2Binding? = null
    private val binding get() = _binding!!

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if(isGranted.all{ it.value }) {
            navigateToPreview()
        }else{
            Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermission2Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()

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


    fun onClickStartSettingForPermissionButton() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${activity?.packageName}")
        )
        startActivity(intent)
    }
}