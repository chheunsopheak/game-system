package common

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.role.RoleRepository
import repository.user.UserRepository
import repository.user.UserRoleRepository
import wrapper.ApiResult

@Service
class UserRoleHelper(
    private val userRoleRepository: UserRoleRepository,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository
) {

    fun getUserRole(userId: String): ApiResult<String> {
        val user = userRepository.findById(userId)
            ?: return ApiResult.error(
                HttpStatus.NOT_FOUND.value(), "User not found"
            )

        val userRole = userRoleRepository.findByUserId(userId)
            ?: return ApiResult.error(
                HttpStatus.NOT_FOUND.value(), "User role not found"
            )

        val role = roleRepository.findById(userRole.role?.id!!)
            ?: return ApiResult.error(
                HttpStatus.NOT_FOUND.value(), "Role not found"
            )

        return ApiResult.success(role.get().name, "User role retrieved successfully")
    }
}