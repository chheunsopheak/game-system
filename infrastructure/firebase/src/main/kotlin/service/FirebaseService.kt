package service

import request.FirebaseBroadcastRequest
import request.FirebaseRequest
import wrapper.ApiResult

interface FirebaseService {
    fun sendNotification(request: FirebaseRequest): ApiResult<String>
    fun sendBroadcastNotification(request: FirebaseBroadcastRequest): ApiResult<String>
}