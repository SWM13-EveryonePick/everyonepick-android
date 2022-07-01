package org.soma.everyonepick.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.login.databinding.FragmentLoginBinding
import java.lang.Exception

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.fragment = this
        binding.lifecycleOwner = this

        return binding.root
    }

    fun onClickLogin() {
        val intent = Intent(
            requireContext(),
            Class.forName("org.soma.everyonepick.app.HomeActivity")
        )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}