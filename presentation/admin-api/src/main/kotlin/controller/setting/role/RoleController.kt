package com.gamesystem.controller.setting.role

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import request.role.RoleRequest
import response.role.RoleResponse
import service.role.RoleService
import wrapper.ApiResult
import wrapper.PaginatedResult

@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class RoleController(private val roleService: RoleService) {

    // Create a new role
    @PostMapping("role")
    fun createRole(@RequestBody request: RoleRequest): ResponseEntity<ApiResult<String>> {
        val response = roleService.createRole(request)
        return if (response.statusCode != 200) {
            ResponseEntity.status(response.statusCode).body(response)
        } else {
            ResponseEntity.status(response.statusCode).body(response)
        }
    }

    // Update a role
    @PutMapping("role/{id}")
    fun updateRole(@PathVariable id: String, @RequestBody request: RoleRequest): ResponseEntity<ApiResult<String>> {
        val response = roleService.updateRole(id, request)
        return if (response.statusCode != 200) {
            ResponseEntity.status(response.statusCode).body(response)
        } else {
            ResponseEntity.status(response.statusCode).body(response)
        }
    }

    // Delete a role
    @DeleteMapping("role/{id}")
    fun deleteRole(@PathVariable id: String): ResponseEntity<ApiResult<String>> {
        val response = roleService.deleteRole(id)
        return if (response.statusCode != 200) {
            ResponseEntity.status(response.statusCode).body(response)
        } else {
            ResponseEntity.status(response.statusCode).body(response)
        }
    }

    // Get a role
    @GetMapping("role/{id}")
    fun getRole(@PathVariable id: String): ResponseEntity<ApiResult<RoleResponse>> {
        val response = roleService.getRoleById(id)
        return if (response.statusCode != 200) {
            ResponseEntity.status(response.statusCode).body(response)
        } else {
            ResponseEntity.status(response.statusCode).body(response)
        }
    }

    // Get all roles
    @GetMapping("roles")
    fun getAllRoles(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): ResponseEntity<PaginatedResult<RoleResponse>> {
        val response = roleService.getRoles(pageNumber, pageSize, searchString)
        return if (response.statusCode != 200) {
            ResponseEntity.status(response.statusCode).body(response)
        } else {
            ResponseEntity.status(response.statusCode).body(response)
        }
    }
}