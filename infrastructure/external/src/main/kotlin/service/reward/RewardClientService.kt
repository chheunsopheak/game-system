package service.reward

import request.reward.ClaimRewardRequest
import response.reward.RewardClaimResponse
import response.reward.RewardClientResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RewardClientService {
    @POST("partner/v1/lucky-reward")
    suspend fun getAllReward(@Header("Authorization") token: String): RewardClientResponse

    @POST("partner/v1/lucky-reward/claim")
    suspend fun claimReward(
        @Header("Authorization") token: String,
        @Body request: ClaimRewardRequest
    ): RewardClaimResponse
}