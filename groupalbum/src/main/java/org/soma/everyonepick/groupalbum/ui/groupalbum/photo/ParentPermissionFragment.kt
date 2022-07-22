package org.soma.everyonepick.groupalbum.ui.groupalbum.photo

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common_ui.PermissionFragment
import org.soma.everyonepick.groupalbum.R

class ParentPermissionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = PermissionFragment(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val directions = ParentPermissionFragmentDirections.actionParentpermissionToImagepicker()
            findNavController().navigate(directions)
        }
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_permission, fragment)
            .commit()

        return inflater.inflate(R.layout.fragment_parentpermission, container, false)
    }
}