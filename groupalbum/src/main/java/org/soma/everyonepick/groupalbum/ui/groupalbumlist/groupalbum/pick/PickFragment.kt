package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentPickBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListFragment

/**
 * 마음에 드는 사진을 선택하는 프래그먼트입니다. 진입점은, [PhotoListFragment]에서 여러 사진을 선택하여 합성 작업을 시작하는
 * 시점과, 합성중 탭에서 내가 Pick하지 않은 사진을 눌렀을 때입니다. TODO: Type 관련 주석
 */
class PickFragment : Fragment() {

    private var _binding: FragmentPickBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}