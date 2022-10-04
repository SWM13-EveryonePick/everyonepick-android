package org.soma.everyonepick.login.ui.faceinformation.camerafragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common_ui.PermissionFragment
import org.soma.everyonepick.login.R

class ParentPermissionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = PermissionFragment.newInstance(arrayOf(Manifest.permission.CAMERA)) {
            val directions = ParentPermissionFragmentDirections.toFaceInformationPreviewFragment()
            findNavController().navigate(directions)
        }
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_permission, fragment)
            .commit()

        return inflater.inflate(R.layout.fragment_parent_permission, container, false)
    }
}