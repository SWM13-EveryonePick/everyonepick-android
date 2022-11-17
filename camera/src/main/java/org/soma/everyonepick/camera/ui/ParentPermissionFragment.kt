package org.soma.everyonepick.camera.ui

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.camera.R
import org.soma.everyonepick.common_ui.PermissionFragment
import org.soma.everyonepick.common_ui.PermissionFragment.Companion.PERMISSION_FRAGMENT_REQUEST_KEY

class ParentPermissionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = PermissionFragment.newInstance(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE))
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_permission, fragment)
            .commit()

        activity?.supportFragmentManager?.setFragmentResultListener(PERMISSION_FRAGMENT_REQUEST_KEY, viewLifecycleOwner) { _, _ ->
            val directions = ParentPermissionFragmentDirections.toPreviewFragment()
            findNavController().navigate(directions)
        }

        return inflater.inflate(R.layout.fragment_parent_permission, container, false)
    }
}