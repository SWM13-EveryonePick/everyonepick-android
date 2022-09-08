package org.soma.everyonepick.common.domain.usecase

import org.soma.everyonepick.common.data.source.UserService
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userService: UserService
) {
    suspend fun readUser(token: String) = userService.readUser(token).data
}