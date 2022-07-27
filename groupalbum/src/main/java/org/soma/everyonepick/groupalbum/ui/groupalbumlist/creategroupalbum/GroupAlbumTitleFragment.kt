package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.groupalbum.R

class GroupAlbumTitleFragment : Fragment() {
    private val args: GroupAlbumTitleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        for(inviteFriend in args.inviteFriends) {
            Log.e("TAG", inviteFriend.profileNickname.toString())
        }
        return inflater.inflate(R.layout.fragment_group_album_title, container, false)
    }
}