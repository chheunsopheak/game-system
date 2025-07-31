package com.gamesystem.controller.notification

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import response.notification.NotificationResponse
import service.notification.NotificationService
import wrapper.ApiResult
import wrapper.PaginatedResult

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class NotificationController(private val notification: NotificationService) {

    //get all notifications
    @GetMapping("/notifications")
    fun getNotifications(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ): ResponseEntity<PaginatedResult<NotificationResponse>> {
        val response = notification.getNotifications(pageNumber, pageSize, searchString)
        if (response.succeeded) {
            return ResponseEntity.ok(response)
        }
        return ResponseEntity.status(response.statusCode).body(response)
    }

    //get notification by id
    @GetMapping("/notification/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<ApiResult<NotificationResponse>> {
        val response = notification.getById(id)
        if (response.success) {
            return ResponseEntity.ok(response)
        }
        return ResponseEntity.status(response.statusCode).body(response)
    }

    //mark notification as seen
    @PutMapping("/notification/{id}/seen")
    fun markAsSeen(@PathVariable id: String): ResponseEntity<ApiResult<String>> {
        val response = notification.markAsSeen(id)
        if (response.success) {
            return ResponseEntity.ok(response)
        }
        return ResponseEntity.status(response.statusCode).body(response)
    }

    //get unseen notification count
    @GetMapping("/notification/unseen-count")
    fun getUnseenCount(): ResponseEntity<ApiResult<Int>> {
        val response = notification.getUnSeenCount()
        if (response.success) {
            return ResponseEntity.ok(response)
        }
        return ResponseEntity.status(response.statusCode).body(response)
    }

    //delete notification by id
    @DeleteMapping("/notification/{id}")
    fun deleteById(@PathVariable id: String): ResponseEntity<ApiResult<String>> {
        val response = notification.deleteById(id)
        if (response.success) {
            return ResponseEntity.ok(response)
        }
        return ResponseEntity.status(response.statusCode).body(response)
    }
}