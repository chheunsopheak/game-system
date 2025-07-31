package service.role

import request.role.RoleRequest
import response.role.RoleResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface RoleService {
    fun createRole(request: RoleRequest): ApiResult<String>
    fun updateRole(id: String, request: RoleRequest): ApiResult<String>
    fun deleteRole(id: String): ApiResult<String>
    fun getRoleById(id: String): ApiResult<RoleResponse>
    fun getRoles(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<RoleResponse>
}