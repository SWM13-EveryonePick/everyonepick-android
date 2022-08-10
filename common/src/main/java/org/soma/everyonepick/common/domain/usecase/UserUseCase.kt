package org.soma.everyonepick.common.domain.usecase

import org.soma.everyonepick.common.data.repository.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun getUser(token: String) = userRepository.getUser(token)
}