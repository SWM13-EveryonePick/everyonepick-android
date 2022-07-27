package org.soma.everyonepick.camera.ui.camerafragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.camera.R
import org.soma.everyonepick.common_ui.PermissionFragment

class ParentPermissionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = PermissionFragment(arrayOf(Manifest.permission.CAMERA)) {
            val directions = ParentPermissionFragmentDirections.toPreviewFragment()
            findNavController().navigate(directions)
        }
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_permission, fragment)
            .commit()

        return inflater.inflate(R.layout.fragment_parent_permission, container, false)
    }
}