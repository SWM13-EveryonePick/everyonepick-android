package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.timeout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.soma.everyonepick.common_ui.util.KeyboardUtil
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentTimeoutBinding

@AndroidEntryPoint
class TimeoutFragment : Fragment(), TimeoutFragmentListener {

    private var _binding: FragmentTimeoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TimeoutViewModel by viewModels()
    private val args: TimeoutFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeoutBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.listener = this
        }
        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            viewModel.toastMessage.collectLatest {
                if (it.isNotEmpty()) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.edittextHour.addTextChangedListener {
            if (it?.length == 1) {
                viewModel.setHour(it.toString().toInt())
                viewModel.plusFilledEditText()
            } else {
                viewModel.minusFilledEditText()
            }
        }
        binding.edittextMin1.addTextChangedListener {
            if (it?.length == 1) {
                viewModel.setMin1(it.toString().toInt())
                viewModel.plusFilledEditText()
            } else {
                viewModel.minusFilledEditText()
            }
        }
        binding.edittextMin2.addTextChangedListener {
            if (it?.length == 1) {
                viewModel.setMin2(it.toString().toInt())
                viewModel.plusFilledEditText()
            } else {
                viewModel.minusFilledEditText()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        KeyboardUtil.showKeyboard(binding.edittextHour, requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** [TimeoutFragmentListener] */
    override fun onClickWhatItTimeoutButton() {
        // TODO
    }

    override fun onClickConfirmButton() {
        if (viewModel.min1.value >= 6) {
            Toast.makeText(requireContext(), getString(R.string.toast_min_error), Toast.LENGTH_SHORT).show()
        } else {
            viewModel.createPick { pickId ->
                viewModel.createPickInfo(pickId) {
                    val directions = TimeoutFragmentDirections.toGroupAlbumFragment(args.groupAlbumId)
                    findNavController().navigate(directions)
                    Toast.makeText(requireContext(), getString(R.string.toast_create_pick_success), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

interface TimeoutFragmentListener {
    fun onClickWhatItTimeoutButton()
    fun onClickConfirmButton()
}