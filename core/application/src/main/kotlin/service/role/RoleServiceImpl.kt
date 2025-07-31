package service.role

import constant.ApplicationMessage
import entity.role.RoleEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.role.RoleRepository
import request.role.RoleRequest
import response.role.RoleResponse
import specification.role.RoleFilterSpecification
import util.NumGenerator
import wrapper.ApiResult
import wrapper.PaginatedResult

@Service
class RoleServiceImpl(private val roleRepository: RoleRepository) : RoleService {
    override fun createRole(request: RoleRequest): ApiResult<String> {
        val roleExisting = roleRepository.findByName(request.name)
        if (roleExisting != null) {
            return ApiResult.failed(
                HttpStatus.BAD_REQUEST.value(), ApplicationMessage.EXISTING
            )
        }
        val requestRole = RoleEntity(
            name = request.name,
            description = request.description,
            code = NumGenerator.generateCode("R")
        )
        val result = roleRepository.save(requestRole)
        if (result == null) {
            return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), ApplicationMessage.FAILED
            )
        }
        return ApiResult.success(
            result.id, ApplicationMessage.SAVED
        )
    }

    override fun updateRole(id: String, request: RoleRequest): ApiResult<String> {
        val role = roleRepository.findById(id)
        if (role == null) {
            return ApiResult.failed(
                HttpStatus.NOT_FOUND.value(), ApplicationMessage.NOT_FOUND
            )
        }

        val requestRole = role.get()
        requestRole.name = request.name
        requestRole.description = request.description
        val result = roleRepository.save(requestRole)
        if (result == null) {
            return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), ApplicationMessage.FAILED
            )
        }
        return ApiResult.success(
            result.id, ApplicationMessage.UPDATED
        )
    }

    override fun deleteRole(id: String): ApiResult<String> {
        val role = roleRepository.findById(id)
        if (role == null) {
            return ApiResult.failed(
                HttpStatus.NOT_FOUND.value(), ApplicationMessage.NOT_FOUND
            )
        }
        roleRepository.deleteById(id)
        return ApiResult.success(
            id, ApplicationMessage.DELETED
        )
    }

    override fun getRoleById(id: String): ApiResult<RoleResponse> {
        val role = roleRepository.findById(id)
        if (role == null) {
            return ApiResult.failed(
                HttpStatus.NOT_FOUND.value(), ApplicationMessage.NOT_FOUND
            )
        }
        return ApiResult.success(
            RoleResponse.from(role.get()), ApplicationMessage.SUCCESS
        )
    }

    override fun getRoles(
        pageNumber: Int, pageSize: Int, searchString: String?
    ): PaginatedResult<RoleResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = RoleFilterSpecification.roleFilterSpecification(searchString)
        val rolesPage = roleRepository.findAll(spec, pageable)
        val data = rolesPage
            .sortedByDescending { it.createdAt }
            .map(RoleResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = rolesPage.totalElements.toInt()
        )
        return response
    }
}