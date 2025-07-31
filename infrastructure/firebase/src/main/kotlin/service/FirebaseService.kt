package service

import request.FirebaseRequest
import request.FirebaseBroadcastRequest
import wrapper.ApiResult

interface FirebaseService {
    fun sendNotification(request: FirebaseRequest): ApiResult<String>
    fun sendBroadcastNotification(request: FirebaseBroadcastRequest): ApiResult<String>
}