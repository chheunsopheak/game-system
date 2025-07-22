package service.reward

import constant.RetrofitConstant.REWARD_BASE_URL
import network.RetrofitManager
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import request.reward.ClaimRewardRequest
import response.reward.RewardClaimResponse
import response.reward.RewardClientResponse

@Service
class RewardClientServiceImpl(retrofitManager: RetrofitManager) : RewardClientService {
    val client = retrofitManager.createClient(REWARD_BASE_URL, RewardClientService::class.java)
    override suspend fun getAllReward(token: String): RewardClientResponse {
        val data = client.getAllReward("Bearer $token")
        print("data: $data")

        if (data.response.status != HttpStatus.OK) {
            throw Exception("Failed to get rewards")
        }
        return data
    }

    override suspend fun claimReward(
        token: String,
        request: ClaimRewardRequest
    ): RewardClaimResponse {
        val data = client.claimReward("Bearer $token", request)
        print("data: $data")
        if (data.response.status != HttpStatus.OK) {
            throw Exception("Failed to claim reward")
        }
        return data
    }
}