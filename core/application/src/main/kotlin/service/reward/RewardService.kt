package service.reward

import request.reward.ClaimRewardRequest
import request.reward.UserRewardRequest
import response.reward.*
import wrapper.ApiResult
import wrapper.PaginatedResult

interface RewardService {
    suspend fun getAllReward(): ApiResult<List<RewardResponse>>
    suspend fun getUserReward(
        filterBy: String?,
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<UserRewardResponse>

    suspend fun getRewardById(id: String): ApiResult<RewardDetailResponse>
    suspend fun getAllRewardByUser(
        userId: String,
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<RewardByUserResponse>

    suspend fun getAllRewards(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<RewardHistoryResponse>

    suspend fun saveReward(request: UserRewardRequest): ApiResult<String>

    suspend fun claimReward(request: ClaimRewardRequest): ApiResult<String>
    suspend fun rewardPicker(threshold: Int, playCount: Int, rewards: List<RewardResponse>): ApiResult<RewardResponse>
}