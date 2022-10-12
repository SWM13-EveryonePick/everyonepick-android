package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.soma.everyonepick.common_ui.util.KeyboardUtil
import org.soma.everyonepick.groupalbum.databinding.DialogFragmentUpdateTitleDialogBinding

/**
 * EditText에 새 title을 입력하고 확인 버튼을 누름으로써 단체공유앨범의 title을 변경합니다.
 * @param onClickConfirmButton 단체공유앨범의 title을 변경하는 코드
 */
class UpdateTitleDialogFragment(
    private val onClickConfirmButton: (String) -> Unit
): DialogFragment() {
    private var _binding: DialogFragmentUpdateTitleDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentUpdateTitleDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonConfirm.setOnClickListener {
            onClickConfirmButton(binding.edittextTitle.text.toString())
            dismiss()
        }
        binding.buttonCancel.setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        // onResume()에 showKeyboard()를 넣어도 키보드가 올라오지 않는 문제가 있었고,
        // 약간의 딜레이를 넣음으로써 이를 해결하였습니다.
        Handler(Looper.getMainLooper()).postDelayed({
            KeyboardUtil.showKeyboard(binding.edittextTitle, requireActivity())
        }, 100)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}