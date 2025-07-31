package service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import request.FirebaseBroadcastRequest
import request.FirebaseRequest
import wrapper.ApiResult

@Service
class FirebaseServiceImpl : FirebaseService {
    override fun sendNotification(request: FirebaseRequest): ApiResult<String> {
        return try {
            val notification = Notification
                .builder()
                .setTitle(request.title)
                .setBody(request.body)
                .build()

            if (request.token.size == 1) {
                val message = Message
                    .builder()
                    .setToken(request.token.first())
                    .setNotification(notification)
                    .putAllData(request.data)
                    .build()

                val response = FirebaseMessaging
                    .getInstance()
                    .sendAsync(message)
                    .get()

                ApiResult.success(
                    null,
                    "Sent to one token, messageId: $response"
                )
            } else {
                val message = MulticastMessage
                    .builder()
                    .addAllTokens(request.token)
                    .setNotification(notification)
                    .putAllData(request.data)
                    .build()

                val response = FirebaseMessaging
                    .getInstance()
                    .sendEachForMulticastAsync(message)
                    .get()
                ApiResult.success(
                    null,
                    "Sent to ${response.successCount} devices out of ${request.token.size}"
                )
            }

        } catch (e: Exception) {
            ApiResult.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to send notification: ${e.message}"
            )
        }
    }

    override fun sendBroadcastNotification(request: FirebaseBroadcastRequest): ApiResult<String> {
        return try {

            val notification = Notification
                .builder()
                .setTitle(request.title)
                .setBody(request.body)
                .build()

            val message = Message
                .builder()
                .setTopic(request.topic)
                .setNotification(notification)
                .putAllData(request.data)
                .build()

            val response = FirebaseMessaging
                .getInstance()
                .sendAsync(message)
                .get()
            ApiResult.success(
                null,
                "Sent to topic '${request.topic}', messageId: $response"
            )

        } catch (e: Exception) {
            ApiResult.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to send topic notification: ${e.message}"
            )
        }
    }
}