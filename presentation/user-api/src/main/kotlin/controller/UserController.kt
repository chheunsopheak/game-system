package com.gamesystem.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import response.user.UserResponse
import service.UserService
import wrapper.PaginatedResult

@RestController
@RequestMapping("/api/v1")
class UserController(private val userService: UserService) {

    //Get all users
    @GetMapping("/users")
    suspend fun getAllUsers(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ): PaginatedResult<UserResponse> {
        return userService.getAllUsers(pageNumber, pageSize, searchString)
    }

}