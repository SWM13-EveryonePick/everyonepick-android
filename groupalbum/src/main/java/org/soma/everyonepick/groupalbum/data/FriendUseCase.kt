package org.soma.everyonepick.groupalbum.data

import javax.inject.Inject

class FriendUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    fun getFriends() = friendRepository.getFriends()
}